package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class MapperXmlTest {
    @Test
    public void updateDrugsOnlyTouchesLatestSeekAndHandlesNullPrice() throws Exception {
        Configuration configuration = parseMapper("mapper/SeekMapper.xml");
        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("10.00"));

        BoundSql boundSql = statement.getBoundSql(seek);
        String compactSql = boundSql.getSql().replaceAll("\\s+", "").toLowerCase();

        assertTrue(compactSql.contains("price=ifnull(price,0)+?"));
        assertTrue(compactSql.contains("wherepatientid=?"));
        assertTrue(compactSql.endsWith("orderbyiddesclimit1"));
    }

    @Test
    public void drugsMapperPrimaryKeyResultMapIncludesDispensingFields() throws Exception {
        Configuration configuration = parseMapper("mapper/DrugsMapper.xml");
        ResultMap resultMap = configuration.getResultMap("com.hospital.dao.DrugsMapper.BaseResultMap");

        Set<String> mappedProperties = resultMap.getResultMappings().stream()
                .map(ResultMapping::getProperty)
                .collect(Collectors.toSet());

        assertTrue(mappedProperties.contains("price"));
        assertTrue(mappedProperties.contains("number"));
        assertTrue(mappedProperties.contains("text"));
    }

    private Configuration parseMapper(String resource) throws Exception {
        Configuration configuration = new Configuration();
        try (Reader reader = Resources.getResourceAsReader(resource)) {
            XMLMapperBuilder builder = new XMLMapperBuilder(reader, configuration, resource, configuration.getSqlFragments());
            builder.parse();
        }
        return configuration;
    }
}

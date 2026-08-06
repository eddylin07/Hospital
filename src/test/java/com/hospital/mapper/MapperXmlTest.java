package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class MapperXmlTest {
    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndHandlesNullPrice() throws Exception {
        Configuration configuration = parseMapper("mapper/SeekMapper.xml");
        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        Seek seek = new Seek();
        seek.setPatientid(42);
        seek.setDrugs("7@3");
        seek.setPrice(new BigDecimal("31.50"));

        BoundSql boundSql = statement.getBoundSql(seek);
        String compactSql = boundSql.getSql().replaceAll("\\s+", "").toLowerCase();

        assertTrue(compactSql.contains("price=ifnull(price,0)+?"));
        assertTrue(compactSql.contains("wherepatientid=?orderbyiddesclimit1"));
    }

    @Test
    public void drugsMapperSelectByPrimaryKeyMapsBusinessFields() throws Exception {
        Configuration configuration = parseMapper("mapper/DrugsMapper.xml");
        ResultMap resultMap = configuration.getResultMap("com.hospital.dao.DrugsMapper.BaseResultMap");

        Set<String> properties = new HashSet<>();
        for (ResultMapping mapping : resultMap.getResultMappings()) {
            properties.add(mapping.getProperty());
        }

        assertTrue(properties.contains("price"));
        assertTrue(properties.contains("number"));
        assertTrue(properties.contains("text"));
    }

    private Configuration parseMapper(String resource) throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
            mapperParser.parse();
        }
        return configuration;
    }
}

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
    public void updateDrugsOnlyUpdatesLatestSeekAndKeepsNullPriceBillable() throws Exception {
        Configuration configuration = parseMapper("mapper/SeekMapper.xml");
        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        Seek seek = new Seek();
        seek.setPatientid(11);
        seek.setDrugs("7@2");
        seek.setPrice(new BigDecimal("7.00"));

        BoundSql boundSql = statement.getBoundSql(seek);
        String sql = boundSql.getSql().replaceAll("\\s+", "").toLowerCase();

        assertTrue(sql.contains("price=ifnull(price,0)+?"));
        assertTrue(sql.endsWith("wherepatientid=?orderbyiddesclimit1"));
    }

    @Test
    public void drugsResultMapIncludesInventoryAndPricingFields() throws Exception {
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
        InputStream inputStream = Resources.getResourceAsStream(resource);
        try {
            XMLMapperBuilder parser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
            parser.parse();
            return configuration;
        } finally {
            inputStream.close();
        }
    }
}

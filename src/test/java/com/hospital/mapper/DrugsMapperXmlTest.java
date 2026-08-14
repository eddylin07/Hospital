package com.hospital.mapper;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class DrugsMapperXmlTest {
    @Test
    public void selectByPrimaryKeyMapsDispensingFields() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream mapperXml = Resources.getResourceAsStream("mapper/DrugsMapper.xml")) {
            XMLMapperBuilder parser = new XMLMapperBuilder(mapperXml, configuration, "mapper/DrugsMapper.xml", configuration.getSqlFragments());
            parser.parse();
        }

        ResultMap resultMap = configuration.getResultMap("com.hospital.dao.DrugsMapper.BaseResultMap");
        Set<String> mappedProperties = new HashSet<>();
        for (ResultMapping mapping : resultMap.getResultMappings()) {
            mappedProperties.add(mapping.getProperty());
        }

        assertTrue(mappedProperties.contains("price"));
        assertTrue(mappedProperties.contains("number"));
        assertTrue(mappedProperties.contains("text"));
    }
}

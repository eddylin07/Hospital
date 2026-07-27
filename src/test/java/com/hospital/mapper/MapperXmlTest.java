package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapperXmlTest {

    @Test
    public void updateDrugsTargetsLatestSeekAndAccumulatesNullSafePrice() throws Exception {
        Configuration configuration = loadMapper("mapper/SeekMapper.xml");
        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");

        Seek seek = new Seek();
        seek.setPatientid(11);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("7.00"));
        BoundSql boundSql = statement.getBoundSql(seek);

        String compactSql = boundSql.getSql()
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase(Locale.ENGLISH)
                .replace(" ", "");

        assertTrue(compactSql.contains("updatesek"));
        assertTrue(compactSql.contains("setdrugs=?,price=ifnull(price,0)+?"));
        assertTrue(compactSql.contains("wherepatientid=?orderbyiddesclimit1"));
    }

    @Test
    public void drugsBaseResultMapIncludesDispensingFields() throws Exception {
        Configuration configuration = loadMapper("mapper/DrugsMapper.xml");
        ResultMap resultMap = configuration.getResultMap("com.hospital.dao.DrugsMapper.BaseResultMap");

        Set<String> mappedProperties = new HashSet<>();
        for (ResultMapping mapping : resultMap.getResultMappings()) {
            mappedProperties.add(mapping.getProperty());
        }

        assertTrue(mappedProperties.contains("price"));
        assertTrue(mappedProperties.contains("number"));
        assertTrue(mappedProperties.contains("text"));
    }

    private Configuration loadMapper(String resource) throws Exception {
        Configuration configuration = new Configuration();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource);
        assertNotNull("Missing mapper resource " + resource, inputStream);
        try {
            XMLMapperBuilder parser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
            parser.parse();
            return configuration;
        } finally {
            inputStream.close();
        }
    }
}

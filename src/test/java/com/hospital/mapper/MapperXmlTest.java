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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MapperXmlTest {
    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndAccumulatesPriceSafely() throws Exception {
        Configuration configuration = parseMapper("mapper/SeekMapper.xml");
        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        BoundSql boundSql = statement.getBoundSql(new Seek());

        String sql = normalize(boundSql.getSql()).toLowerCase();

        assertTrue(sql.contains("set drugs=?,price=ifnull(price,0)+?"));
        assertTrue(sql.contains("where id=("));
        assertTrue(sql.contains("select id from seek where patientid=? order by id desc limit 1"));
        assertTrue(sql.contains("latest_seek"));
        assertEquals(Arrays.asList("drugs", "price", "patientid"), parameterNames(boundSql));
    }

    @Test
    public void drugResultMapIncludesDispensingFields() throws Exception {
        Configuration configuration = parseMapper("mapper/DrugsMapper.xml");
        ResultMap resultMap = configuration.getResultMap("com.hospital.dao.DrugsMapper.BaseResultMap");

        List<String> properties = new ArrayList<>();
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
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
            mapperParser.parse();
        } finally {
            inputStream.close();
        }
        return configuration;
    }

    private List<String> parameterNames(BoundSql boundSql) {
        List<String> names = new ArrayList<>();
        for (org.apache.ibatis.mapping.ParameterMapping mapping : boundSql.getParameterMappings()) {
            names.add(mapping.getProperty());
        }
        return names;
    }

    private String normalize(String sql) {
        return sql.replaceAll("\\s+", " ").replace(" = ", "=").replace(" + ", "+").trim();
    }
}

package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class MapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndHandlesNullPrice() throws Exception {
        Configuration configuration = parseMapper("mapper/SeekMapper.xml");
        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");

        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("20.00"));

        BoundSql boundSql = statement.getBoundSql(seek);
        String sql = normalizeSql(boundSql.getSql()).toLowerCase(Locale.ENGLISH);

        assertTrue(sql, sql.contains("set drugs=?,price=ifnull(price,0)+?"));
        assertTrue(sql, sql.contains("where patientid=?"));
        assertTrue(sql, sql.endsWith("order by id desc limit 1"));
    }

    @Test
    public void selectDrugByPrimaryKeyMapsDispensingFields() throws Exception {
        Configuration configuration = parseMapper("mapper/DrugsMapper.xml");
        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.DrugsMapper.selectByPrimaryKey");

        Set<String> properties = new HashSet<String>();
        for (ResultMapping mapping : statement.getResultMaps().get(0).getResultMappings()) {
            properties.add(mapping.getProperty());
        }

        assertTrue(properties.toString(), properties.containsAll(Arrays.asList(
                "id", "name", "type", "price", "number", "text")));
    }

    private Configuration parseMapper(String resource) throws IOException {
        Configuration configuration = new Configuration();
        Reader reader = Resources.getResourceAsReader(resource);
        try {
            XMLMapperBuilder builder = new XMLMapperBuilder(reader, configuration, resource, configuration.getSqlFragments());
            builder.parse();
        } finally {
            reader.close();
        }
        return configuration;
    }

    private String normalizeSql(String sql) {
        return sql.replaceAll("\\s+", " ").trim();
    }
}

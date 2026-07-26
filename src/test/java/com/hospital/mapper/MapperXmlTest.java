package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
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
        seek.setDrugs("7@2");
        seek.setPrice(new BigDecimal("7.00"));

        BoundSql boundSql = statement.getBoundSql(seek);
        String sql = normalize(boundSql.getSql());

        assertTrue(sql.contains("set drugs=?,price=ifnull(price,0)+?"));
        assertTrue(sql.contains("where patientid=?"));
        assertTrue(sql.contains("order by id desc limit 1"));
    }

    @Test
    public void selectDrugByPrimaryKeyMapsPriceAndNumberForDispensing() throws Exception {
        Configuration configuration = parseMapper("mapper/DrugsMapper.xml");
        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.DrugsMapper.selectByPrimaryKey");

        Set<String> mappedProperties = new HashSet<String>();
        for (ResultMapping mapping : statement.getResultMaps().get(0).getResultMappings()) {
            mappedProperties.add(mapping.getProperty());
        }

        assertTrue(mappedProperties.contains("price"));
        assertTrue(mappedProperties.contains("number"));
    }

    private Configuration parseMapper(String resource) throws Exception {
        Configuration configuration = new Configuration();
        InputStream inputStream = Resources.getResourceAsStream(resource);
        XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
        mapperParser.parse();
        return configuration;
    }

    private String normalize(String sql) {
        return sql.replaceAll("\\s+", " ").trim().toLowerCase();
    }
}

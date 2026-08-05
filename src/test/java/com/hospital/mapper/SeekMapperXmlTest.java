package com.hospital.mapper;

import com.hospital.dao.SeekMapper;
import com.hospital.dao.DrugsMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyTargetsLatestSeekForPatient() {
        Configuration configuration = loadMapperConfiguration(SeekMapper.class, "/mapper/SeekMapper.xml");
        Seek seek = new Seek();
        seek.setPatientid(1);

        BoundSql boundSql = configuration
                .getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs")
                .getBoundSql(seek);

        String normalizedSql = boundSql.getSql().toLowerCase().replaceAll("\\s+", " ").trim();
        assertTrue(normalizedSql.contains("where patientid=?"));
        assertTrue(normalizedSql.contains("order by id desc"));
        assertTrue(normalizedSql.endsWith("limit 1"));
    }

    @Test
    public void updateNumberRequiresEnoughRemainingStock() {
        Configuration configuration = loadMapperConfiguration(DrugsMapper.class, "/mapper/DrugsMapper.xml");
        Drugs drugs = new Drugs();
        drugs.setId(1);
        drugs.setNumber(3);

        BoundSql boundSql = configuration
                .getMappedStatement("com.hospital.dao.DrugsMapper.updateNumber")
                .getBoundSql(drugs);

        String normalizedSql = boundSql.getSql().toLowerCase().replaceAll("\\s+", " ").trim();
        assertTrue(normalizedSql.contains("set number=number-?"));
        assertTrue(normalizedSql.contains("where id=? and number >= ?"));
    }

    private Configuration loadMapperConfiguration(Class<?> mapperClass, String resourcePath) {
        Configuration configuration = new Configuration();
        configuration.addMapper(mapperClass);

        InputStream mapperXml = getClass().getResourceAsStream(resourcePath);
        assertNotNull(mapperXml);

        XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                mapperXml,
                configuration,
                resourcePath,
                configuration.getSqlFragments());
        mapperBuilder.parse();
        return configuration;
    }
}

package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndAccumulatesNullSafePrice() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream mapperXml = getClass().getClassLoader().getResourceAsStream("mapper/SeekMapper.xml")) {
            assertNotNull("mapper/SeekMapper.xml should be available on the test classpath", mapperXml);
            new XMLMapperBuilder(mapperXml, configuration, "mapper/SeekMapper.xml", configuration.getSqlFragments()).parse();
        }

        Seek seek = new Seek();
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("24.50"));
        seek.setPatientid(7);

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        BoundSql boundSql = statement.getBoundSql(seek);
        String sql = boundSql.getSql().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT).trim();
        String compactSql = sql.replace(" ", "");

        assertTrue(sql, compactSql.contains("setdrugs=?,price=ifnull(price,0)+?"));
        assertTrue(sql, sql.contains("where patientid=?"));
        assertTrue(sql, sql.contains("order by id desc"));
        assertTrue(sql, sql.endsWith("limit 1"));
        assertEquals(3, boundSql.getParameterMappings().size());
        assertEquals("drugs", boundSql.getParameterMappings().get(0).getProperty());
        assertEquals("price", boundSql.getParameterMappings().get(1).getProperty());
        assertEquals("patientid", boundSql.getParameterMappings().get(2).getProperty());
    }
}

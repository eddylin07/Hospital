package com.hospital.mapper;

import com.hospital.dao.SeekMapper;
import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyTouchesLatestSeekForPatient() throws Exception {
        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("1@2,3@1");
        seek.setPrice(new BigDecimal("27.50"));

        BoundSql boundSql = boundSql("updateDrugs", seek);
        String sql = normalize(boundSql.getSql());

        assertTrue(sql, sql.startsWith("update seek"));
        assertTrue(sql, sql.contains("set drugs=?,price=coalesce(price,0)+?"));
        assertTrue(sql, sql.contains("where patientid=?"));
        assertTrue(sql, sql.contains("order by id desc"));
        assertTrue(sql, sql.endsWith("limit 1"));

        List<ParameterMapping> mappings = boundSql.getParameterMappings();
        assertEquals("drugs", mappings.get(0).getProperty());
        assertEquals("price", mappings.get(1).getProperty());
        assertEquals("patientid", mappings.get(2).getProperty());
    }

    @Test
    public void getSeekByPatientIdReturnsLatestSeekRecord() throws Exception {
        BoundSql boundSql = boundSql("getSeekByPatientId", 7);
        String sql = normalize(boundSql.getSql());

        assertTrue(sql, sql.startsWith("select * from seek"));
        assertTrue(sql, sql.contains("where patientid=?"));
        assertTrue(sql, sql.contains("order by id desc"));
        assertTrue(sql, sql.endsWith("limit 0,1"));

        List<ParameterMapping> mappings = boundSql.getParameterMappings();
        assertEquals(1, mappings.size());
        assertEquals("patientid", mappings.get(0).getProperty());
    }

    private static BoundSql boundSql(String statementName, Object parameterObject) throws Exception {
        Configuration configuration = new Configuration();
        configuration.addMapper(SeekMapper.class);
        try (InputStream inputStream = Resources.getResourceAsStream("mapper/SeekMapper.xml")) {
            XMLMapperBuilder parser = new XMLMapperBuilder(
                    inputStream,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments());
            parser.parse();
        }
        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper." + statementName);
        return statement.getBoundSql(parameterObject);
    }

    private static String normalize(String sql) {
        return sql.replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
    }
}

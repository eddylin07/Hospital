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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekForPatient() {
        BoundSql boundSql = updateDrugsStatement().getBoundSql(seekPrescription());
        String sql = normalizeSql(boundSql.getSql());

        assertTrue(sql, sql.startsWith("update seek set drugs=?,price=ifnull(price,0)+?"));
        assertTrue(sql, sql.contains("where patientid=?"));
        assertTrue(sql, sql.contains("order by id desc"));
        assertTrue(sql, sql.endsWith("limit 1"));
    }

    private static MappedStatement updateDrugsStatement() {
        Configuration configuration = new Configuration();
        try (InputStream mapperXml = SeekMapperXmlTest.class.getClassLoader().getResourceAsStream("mapper/SeekMapper.xml")) {
            assertNotNull("SeekMapper.xml must be available on the test classpath", mapperXml);

            XMLMapperBuilder parser = new XMLMapperBuilder(
                    mapperXml,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments());
            parser.parse();
        } catch (Exception ex) {
            throw new AssertionError("SeekMapper.xml should parse as a MyBatis mapper", ex);
        }

        return configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
    }

    private static Seek seekPrescription() {
        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("12@2");
        seek.setPrice(new BigDecimal("19.98"));
        return seek;
    }

    private static String normalizeSql(String sql) {
        return sql.replaceAll("\\s+", " ").trim().toLowerCase(Locale.ENGLISH);
    }
}

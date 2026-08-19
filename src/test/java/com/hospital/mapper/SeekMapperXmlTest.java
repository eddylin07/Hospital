package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestVisitAndAddsToNullSafePrice() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream mapperXml = Resources.getResourceAsStream("mapper/SeekMapper.xml")) {
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                    mapperXml,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments()
            );
            mapperBuilder.parse();
        }
        Seek seek = new Seek();
        seek.setPatientid(12);
        seek.setDrugs("3@2");
        seek.setPrice(new BigDecimal("16.00"));

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        BoundSql boundSql = statement.getBoundSql(seek);
        String sql = boundSql.getSql().replaceAll("\\s+", " ").trim().toLowerCase();

        assertTrue(sql, sql.contains("update seek"));
        assertTrue(sql, sql.contains("set drugs=?,price=ifnull(price,0)+?"));
        assertTrue(sql, sql.contains("where patientid=?"));
        assertTrue(sql, sql.contains("order by id desc"));
        assertTrue(sql, sql.contains("limit 1"));
    }
}

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
    public void updateDrugsOnlyUpdatesLatestSeekAndHandlesNullPrice() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream mapper = Resources.getResourceAsStream("mapper/SeekMapper.xml")) {
            XMLMapperBuilder builder = new XMLMapperBuilder(
                    mapper,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments());
            builder.parse();
        }

        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("12.50"));

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        BoundSql boundSql = statement.getBoundSql(seek);
        String sql = boundSql.getSql().replaceAll("\\s+", " ").trim().toLowerCase();

        assertTrue("price accumulation should not be skipped when the current price is null",
                sql.contains("price=ifnull(price,0)+?"));
        assertTrue("prescriptions should target the newest seek row for the patient",
                sql.contains("where patientid=? order by id desc limit 1"));
    }
}

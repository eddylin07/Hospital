package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekRowAndHandlesNullPrice() throws Exception {
        Configuration configuration = new Configuration();
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/mapper/SeekMapper.xml")) {
            new XMLMapperBuilder(inputStream, configuration, "mapper/SeekMapper.xml", configuration.getSqlFragments()).parse();
        }

        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("19.80"));

        BoundSql boundSql = configuration
                .getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs")
                .getBoundSql(seek);
        String sql = normalize(boundSql.getSql());

        assertTrue("Existing null seek prices must still accumulate the dispensed amount",
                sql.contains("price=ifnull(price,0)+?"));
        assertTrue("Dispensing must stay scoped to the current patient",
                sql.contains("where patientid=?"));
        assertTrue("Only the latest seek row should receive prescribed drugs",
                sql.contains("order by id desc"));
        assertTrue("The latest-row ordering must be paired with a single-row limit",
                sql.endsWith("limit 1"));
    }

    private static String normalize(String sql) {
        return sql.toLowerCase(Locale.ROOT).replaceAll("\\s+", " ").trim();
    }
}

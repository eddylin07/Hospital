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

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsUpdatesOnlyLatestSeekAndAddsPriceNullSafely() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream mapperXml = getClass().getClassLoader().getResourceAsStream("mapper/SeekMapper.xml")) {
            assertTrue("SeekMapper.xml should be on the test classpath", mapperXml != null);
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(
                    mapperXml,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments()
            );
            mapperParser.parse();
        }

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("1@2,3@4");
        seek.setPrice(new BigDecimal("32.50"));

        BoundSql boundSql = statement.getBoundSql(seek);
        String sql = boundSql.getSql().replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);

        assertTrue(sql.contains("update seek"));
        assertTrue("existing null prices should still become billable totals", sql.contains("price=ifnull(price,0)+?"));
        assertTrue(sql.contains("where patientid=?"));
        assertTrue("prescriptions should attach to the latest visit only", sql.contains("order by id desc"));
        assertTrue("only one seek row should be updated", sql.contains("limit 1"));
    }
}

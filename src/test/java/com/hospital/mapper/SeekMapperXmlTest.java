package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekForPatient() throws Exception {
        String sql = updateDrugsSql();

        assertTrue(sql.contains("where patientid=?"));
        assertTrue(sql.contains("order by id desc"));
        assertTrue(sql.endsWith("limit 1"));
        assertTrue(sql.indexOf("where patientid=?") < sql.indexOf("order by id desc"));
    }

    @Test
    public void updateDrugsAddsPriceWhenExistingPriceIsNull() throws Exception {
        String compactSql = updateDrugsSql().replace(" ", "");

        assertTrue(compactSql.contains("price=ifnull(price,0)+?"));
    }

    private String updateDrugsSql() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream mapperXml = getClass().getResourceAsStream("/mapper/SeekMapper.xml")) {
            assertNotNull(mapperXml);
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                    mapperXml,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments()
            );
            mapperBuilder.parse();
        }

        Seek seek = new Seek();
        seek.setPatientid(1);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("10.00"));

        BoundSql boundSql = configuration
                .getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs")
                .getBoundSql(seek);
        return boundSql.getSql().replaceAll("\\s+", " ").trim().toLowerCase();
    }
}

package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {
    @Test
    public void updateDrugsOnlyUpdatesLatestSeekRowAndKeepsNullPriceBillable() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream mapper = Resources.getResourceAsStream("mapper/SeekMapper.xml")) {
            XMLMapperBuilder parser = new XMLMapperBuilder(
                    mapper,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments());
            parser.parse();
        }

        Seek seek = new Seek();
        seek.setPatientid(9);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("20.00"));

        String sql = configuration
                .getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs")
                .getBoundSql(seek)
                .getSql()
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase();
        String compactSql = sql.replace(" ", "");

        assertTrue(compactSql.contains("price=ifnull(price,0)+?"));
        assertTrue(sql.contains("order by id desc"));
        assertTrue(sql.contains("limit 1"));
        assertTrue(compactSql.contains("whereid=(selectidfrom(selectidfromseekwherepatientid=?orderbyiddesclimit1)latest_seek)"));
    }
}

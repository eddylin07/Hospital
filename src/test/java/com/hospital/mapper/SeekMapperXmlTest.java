package com.hospital.mapper;

import com.hospital.dao.SeekMapper;
import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekForPatient() throws Exception {
        String compactSql = updateDrugsSql().replace(" ", "");

        assertTrue(compactSql.contains("wherepatientid=?orderbyiddesclimit1"));
    }

    @Test
    public void updateDrugsAddsPriceWhenExistingPriceIsNull() throws Exception {
        String compactSql = updateDrugsSql().replace(" ", "");

        assertTrue(compactSql.contains("price=ifnull(price,0)+?"));
    }

    private String updateDrugsSql() throws Exception {
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

        Seek seek = new Seek();
        seek.setPatientid(42);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("12.50"));
        BoundSql boundSql = configuration
                .getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs")
                .getBoundSql(seek);
        return boundSql.getSql().replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
    }
}

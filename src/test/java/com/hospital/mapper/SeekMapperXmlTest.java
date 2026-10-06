package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
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
    public void updateDrugsOnlyTargetsLatestSeekForPatient() throws Exception {
        String sql = updateDrugsSql();

        assertTrue(sql.contains("order by id desc limit 1"));
        assertTrue(sql.matches("update\\s+seek\\s+set\\s+drugs=\\?,price=.*\\s+where\\s+id\\s*=.*"));
    }

    @Test
    public void updateDrugsAddsPriceWhenExistingPriceIsNull() throws Exception {
        String sql = updateDrugsSql();

        assertTrue(sql.contains("ifnull(price,0)+?"));
    }

    private String updateDrugsSql() throws Exception {
        Configuration configuration = new Configuration();
        InputStream mapperXml = getClass().getClassLoader().getResourceAsStream("mapper/SeekMapper.xml");
        assertNotNull("mapper/SeekMapper.xml should be available on the test classpath", mapperXml);
        try {
            XMLMapperBuilder parser = new XMLMapperBuilder(
                    mapperXml,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments());
            parser.parse();
        } finally {
            mapperXml.close();
        }

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("12.34"));

        return statement.getBoundSql(seek).getSql()
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase(Locale.ENGLISH);
    }
}

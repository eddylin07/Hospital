package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndTreatsNullPriceAsZero() throws Exception {
        Configuration configuration = new Configuration();
        try (Reader reader = Resources.getResourceAsReader("mapper/SeekMapper.xml")) {
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                    reader,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments());
            mapperBuilder.parse();
        }

        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("12.50"));

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        String sql = statement.getBoundSql(seek).getSql()
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase(Locale.ROOT);

        assertTrue(sql.contains("price=ifnull(price,0)+?"));
        assertTrue(sql.contains("where patientid=?"));
        assertTrue(sql.endsWith("order by id desc limit 1"));
    }
}

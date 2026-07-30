package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndKeepsNullPriceBillable() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream inputStream = Files.newInputStream(Paths.get("src/main/resources/mapper/SeekMapper.xml"))) {
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                    inputStream,
                    configuration,
                    "src/main/resources/mapper/SeekMapper.xml",
                    configuration.getSqlFragments());
            mapperBuilder.parse();
        }

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        Seek seek = new Seek();
        seek.setPatientid(12);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("7.00"));

        String sql = statement.getBoundSql(seek).getSql()
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase(Locale.ROOT);

        assertTrue(sql.contains("price=ifnull(price,0)+?"));
        assertTrue(sql.endsWith("where patientid=? order by id desc limit 1"));
    }
}

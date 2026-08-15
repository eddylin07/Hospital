package com.hospital.mapper;

import com.hospital.dao.SeekMapper;
import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {
    @Test
    public void updateDrugsOnlyUpdatesLatestSeekRecordAndHandlesNullPrice() throws Exception {
        Configuration configuration = new Configuration();
        configuration.addMapper(SeekMapper.class);
        try (InputStream mapper = Resources.getResourceAsStream("mapper/SeekMapper.xml")) {
            XMLMapperBuilder parser = new XMLMapperBuilder(
                    mapper,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments());
            parser.parse();
        }

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("3@2");
        seek.setPrice(new BigDecimal("9.00"));

        String sql = statement.getBoundSql(seek).getSql()
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase(Locale.ENGLISH);

        assertTrue(sql.contains("set drugs=?,price=ifnull(price,0)+?"));
        assertTrue(sql.contains("where patientid=? order by id desc limit 1"));
    }
}

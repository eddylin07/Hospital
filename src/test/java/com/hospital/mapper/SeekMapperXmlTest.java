package com.hospital.mapper;

import com.hospital.dao.SeekMapper;
import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndHandlesNullPrice() throws Exception {
        Configuration configuration = new Configuration();
        configuration.addMapper(SeekMapper.class);

        InputStream mapperXml = new FileInputStream("src/main/resources/mapper/SeekMapper.xml");
        XMLMapperBuilder mapperParser = new XMLMapperBuilder(
                mapperXml,
                configuration,
                "src/main/resources/mapper/SeekMapper.xml",
                configuration.getSqlFragments());
        mapperParser.parse();

        Seek seek = new Seek();
        seek.setPatientid(42);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("12.50"));

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        String sql = normalize(statement.getBoundSql(seek).getSql());

        assertTrue(sql.contains("set drugs=?,price=ifnull(price,0)+?"));
        assertTrue(sql.contains("where patientid=?"));
        assertTrue(sql.contains("order by id desc limit 1"));
    }

    private String normalize(String sql) {
        return sql.replaceAll("\\s+", " ").trim().toLowerCase();
    }
}


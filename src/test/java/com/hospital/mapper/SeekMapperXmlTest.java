package com.hospital.mapper;

import com.hospital.dao.SeekMapper;
import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndHandlesNullPrice() throws Exception {
        Configuration configuration = new Configuration();
        configuration.addMapper(SeekMapper.class);

        try (InputStream mapperXml = Resources.getResourceAsStream("mapper/SeekMapper.xml")) {
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(
                    mapperXml,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments()
            );
            xmlMapperBuilder.parse();
        }

        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("12.50"));

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        BoundSql boundSql = statement.getBoundSql(seek);
        String normalizedSql = boundSql.getSql().replaceAll("\\s+", " ").trim().toLowerCase();

        assertTrue(normalizedSql.contains("price=ifnull(price,0)+?"));
        assertTrue(normalizedSql.contains("where patientid=? order by id desc limit 1"));
        assertEquals("drugs", boundSql.getParameterMappings().get(0).getProperty());
        assertEquals("price", boundSql.getParameterMappings().get(1).getProperty());
        assertEquals("patientid", boundSql.getParameterMappings().get(2).getProperty());
    }
}

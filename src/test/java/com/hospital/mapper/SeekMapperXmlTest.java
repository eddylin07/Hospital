package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestVisitAndAccumulatesNullPrice() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream mapperXml = Resources.getResourceAsStream("mapper/SeekMapper.xml")) {
            new XMLMapperBuilder(
                    mapperXml,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments()
            ).parse();
        }

        Seek seek = new Seek();
        seek.setDrugs("7@2");
        seek.setPrice(new BigDecimal("19.50"));
        seek.setPatientid(42);

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        BoundSql boundSql = statement.getBoundSql(seek);
        String compactSql = boundSql.getSql()
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase(Locale.ROOT)
                .replace(" ", "");

        assertTrue(compactSql.startsWith("updateseekset"));
        assertTrue(compactSql.contains("drugs=?"));
        assertTrue(compactSql.contains("price=ifnull(price,0)+?"));
        assertTrue(compactSql.contains("wherepatientid=?"));
        assertTrue(compactSql.contains("orderbyiddesc"));
        assertTrue(compactSql.endsWith("limit1"));
        assertEquals("drugs", boundSql.getParameterMappings().get(0).getProperty());
        assertEquals("price", boundSql.getParameterMappings().get(1).getProperty());
        assertEquals("patientid", boundSql.getParameterMappings().get(2).getProperty());
    }
}

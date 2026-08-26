package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndAccumulatesNullPriceSafely() throws Exception {
        Configuration configuration = new Configuration();
        InputStream mapperXml = getClass().getResourceAsStream("/mapper/SeekMapper.xml");
        XMLMapperBuilder builder = new XMLMapperBuilder(
                mapperXml,
                configuration,
                "mapper/SeekMapper.xml",
                configuration.getSqlFragments()
        );
        builder.parse();

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        Seek seek = new Seek();
        seek.setPatientid(42);
        seek.setDrugs("7@2");
        BoundSql boundSql = statement.getBoundSql(seek);
        String normalizedSql = boundSql.getSql().replaceAll("\\s+", " ").trim();

        assertTrue(normalizedSql.contains("set drugs=?,price=ifnull(price,0)+?"));
        assertTrue(normalizedSql.endsWith("where patientid=? order by id desc limit 1"));
        assertEquals(
                "drugs,price,patientid",
                boundSql.getParameterMappings().stream()
                        .map(mapping -> mapping.getProperty())
                        .collect(Collectors.joining(","))
        );
    }
}

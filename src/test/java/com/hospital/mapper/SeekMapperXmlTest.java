package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndAccumulatesNullSafePrice() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream mapperXml = getClass().getClassLoader().getResourceAsStream("mapper/SeekMapper.xml")) {
            new XMLMapperBuilder(mapperXml, configuration, "mapper/SeekMapper.xml", configuration.getSqlFragments()).parse();
        }

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        Seek seek = new Seek();
        seek.setDrugs("3@2");
        seek.setPrice(new BigDecimal("19.80"));
        seek.setPatientid(42);

        BoundSql boundSql = statement.getBoundSql(seek);
        String normalizedSql = boundSql.getSql().replaceAll("\\s+", " ").trim().toLowerCase();

        assertTrue(normalizedSql.contains("set drugs=?,price=ifnull(price,0)+?"));
        assertTrue(normalizedSql.contains("where patientid=?"));
        assertTrue(normalizedSql.contains("order by id desc limit 1"));

        List<String> parameterNames = boundSql.getParameterMappings().stream()
                .map(ParameterMapping::getProperty)
                .collect(Collectors.toList());
        assertEquals(java.util.Arrays.asList("drugs", "price", "patientid"), parameterNames);
    }
}

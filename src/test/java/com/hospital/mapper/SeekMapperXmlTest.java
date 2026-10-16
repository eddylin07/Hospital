package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyTouchesLatestSeekAndAddsNullSafePrice() throws Exception {
        Configuration configuration = new Configuration();
        try (Reader reader = Resources.getResourceAsReader("mapper/SeekMapper.xml")) {
            XMLMapperBuilder parser = new XMLMapperBuilder(
                    reader,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments());
            parser.parse();
        }

        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("3@2");
        seek.setPrice(new BigDecimal("12.50"));

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        BoundSql boundSql = statement.getBoundSql(seek);
        String compactSql = boundSql.getSql().replaceAll("\\s+", "").toLowerCase();
        List<String> parameterNames = boundSql.getParameterMappings()
                .stream()
                .map(mapping -> mapping.getProperty())
                .collect(Collectors.toList());

        assertTrue(compactSql.contains("updateseeksetdrugs=?,price=ifnull(price,0)+?wherepatientid=?"));
        assertTrue(compactSql.contains("orderbyiddesc"));
        assertTrue(compactSql.endsWith("limit1"));
        assertEquals(Arrays.asList("drugs", "price", "patientid"), parameterNames);
    }
}

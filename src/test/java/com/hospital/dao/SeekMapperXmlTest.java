package com.hospital.dao;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsStatementAddsMedicationCostForPatient() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream inputStream = Resources.getResourceAsStream("mapper/SeekMapper.xml")) {
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                    inputStream,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments());
            mapperBuilder.parse();
        }

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        Seek seek = new Seek();
        seek.setPatientid(42);
        seek.setDrugs("1@2,3@1");
        seek.setPrice(new BigDecimal("31.50"));

        BoundSql boundSql = statement.getBoundSql(seek);

        String sql = boundSql.getSql().replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
        assertEquals("update seek set drugs=?,price=price+? where patientid=?", sql);
        assertEquals(Arrays.asList("drugs", "price", "patientid"), parameterProperties(boundSql));
    }

    private List<String> parameterProperties(BoundSql boundSql) {
        List<String> properties = new ArrayList<>();
        for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
            properties.add(parameterMapping.getProperty());
        }
        return properties;
    }
}

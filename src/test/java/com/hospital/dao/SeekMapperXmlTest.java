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

import static org.junit.Assert.assertEquals;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsStatementUpdatesDrugsAndAddsPriceForPatient() throws Exception {
        Configuration configuration = new Configuration();
        String resource = "mapper/SeekMapper.xml";
        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
            new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments()).parse();
        }

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        Seek seek = new Seek();
        seek.setDrugs("7@2,9@3");
        seek.setPrice(new BigDecimal("34.00"));
        seek.setPatientid(42);

        BoundSql boundSql = statement.getBoundSql(seek);

        assertEquals("update seek set drugs=?,price=price+? where patientid=?", normalize(boundSql.getSql()));
        assertEquals(Arrays.asList("drugs", "price", "patientid"), propertyNames(boundSql));
    }

    private String normalize(String sql) {
        return sql.replaceAll("\\s+", " ").trim();
    }

    private List<String> propertyNames(BoundSql boundSql) {
        List<String> names = new ArrayList<>();
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            names.add(mapping.getProperty());
        }
        return names;
    }

}

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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyTargetsLatestSeekRecordForPatient() throws Exception {
        Configuration configuration = loadSeekMapper();
        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");

        Seek seek = new Seek();
        seek.setPatientid(17);
        seek.setDrugs("3@2");
        seek.setPrice(new BigDecimal("12.50"));

        BoundSql boundSql = statement.getBoundSql(seek);

        assertEquals(
                "update seek set drugs=?,price=ifnull(price,0)+? where patientid=? order by id desc limit 1",
                normalizeSql(boundSql.getSql()));
        assertEquals(Arrays.asList("drugs", "price", "patientid"), parameterProperties(boundSql));
    }

    private Configuration loadSeekMapper() throws Exception {
        Configuration configuration = new Configuration();
        InputStream mapperXml = getClass().getResourceAsStream("/mapper/SeekMapper.xml");
        assertNotNull("SeekMapper.xml must be available on the test classpath", mapperXml);
        try {
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                    mapperXml,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments());
            mapperBuilder.parse();
        } finally {
            mapperXml.close();
        }
        return configuration;
    }

    private String normalizeSql(String sql) {
        return sql.replaceAll("\\s+", " ").trim();
    }

    private List<String> parameterProperties(BoundSql boundSql) {
        List<String> properties = new java.util.ArrayList<String>();
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            properties.add(mapping.getProperty());
        }
        return properties;
    }
}

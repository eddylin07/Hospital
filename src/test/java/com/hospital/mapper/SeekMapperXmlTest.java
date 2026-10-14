package com.hospital.mapper;

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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsPersistsDispensedDrugsAndPriceForPatient() throws Exception {
        MappedStatement statement = mapperStatement("com.hospital.dao.SeekMapper.updateDrugs");

        Seek seek = new Seek();
        seek.setPatientid(18);
        seek.setDrugs("3@2,5@1");
        seek.setPrice(new BigDecimal("42.50"));

        BoundSql boundSql = statement.getBoundSql(seek);

        assertEquals("update seek set drugs=?,price=price+? where patientid=?",
                normalizeSql(boundSql.getSql()));

        List<ParameterMapping> parameters = boundSql.getParameterMappings();
        assertEquals(3, parameters.size());
        assertEquals("drugs", parameters.get(0).getProperty());
        assertEquals("price", parameters.get(1).getProperty());
        assertEquals("patientid", parameters.get(2).getProperty());
    }

    @Test
    public void updateDrugsRemainsPatientScoped() throws Exception {
        MappedStatement statement = mapperStatement("com.hospital.dao.SeekMapper.updateDrugs");

        String sql = normalizeSql(statement.getBoundSql(new Seek()).getSql());

        assertTrue(sql.startsWith("update seek set"));
        assertTrue(sql.contains(" where patientid=?"));
    }

    private MappedStatement mapperStatement(String statementId) throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream mapperXml = Resources.getResourceAsStream("mapper/SeekMapper.xml")) {
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                    mapperXml,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments());
            mapperBuilder.parse();
        }
        return configuration.getMappedStatement(statementId);
    }

    private String normalizeSql(String sql) {
        return sql.replaceAll("\\s+", " ").trim();
    }
}

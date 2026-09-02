package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {
    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndAccumulatesNullPrice() throws Exception {
        MappedStatement mappedStatement = mappedStatement("com.hospital.dao.SeekMapper.updateDrugs");

        Seek seek = new Seek();
        seek.setPatientid(42);
        seek.setDrugs("7@2");
        seek.setPrice(new BigDecimal("19.80"));

        BoundSql boundSql = mappedStatement.getBoundSql(seek);
        String sql = normalize(boundSql.getSql());

        assertTrue(sql.contains("update seek set drugs=?,price=ifnull(price,0)+? where patientid=?"));
        assertTrue(sql.endsWith("order by id desc limit 1"));
        assertParameterOrder(boundSql.getParameterMappings(), "drugs", "price", "patientid");
    }

    private MappedStatement mappedStatement(String statementId) throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream inputStream = new FileInputStream("src/main/resources/mapper/SeekMapper.xml")) {
            XMLMapperBuilder parser = new XMLMapperBuilder(
                    inputStream,
                    configuration,
                    "src/main/resources/mapper/SeekMapper.xml",
                    configuration.getSqlFragments()
            );
            parser.parse();
        }
        return configuration.getMappedStatement(statementId);
    }

    private String normalize(String sql) {
        return sql.replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
    }

    private void assertParameterOrder(List<ParameterMapping> mappings, String... properties) {
        assertEquals(properties.length, mappings.size());
        for (int i = 0; i < properties.length; i++) {
            assertEquals(properties[i], mappings.get(i).getProperty());
        }
    }
}

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekRowAndAccumulatesNullPrice() throws Exception {
        Seek seek = new Seek();
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("12.50"));
        seek.setPatientid(7);

        BoundSql boundSql = updateDrugsStatement().getBoundSql(seek);
        String sql = normalizeSql(boundSql.getSql());

        assertTrue(sql.contains("update seek set drugs=?,price=ifnull(price,0)+?"));
        assertTrue(sql.contains("where id in"));
        assertTrue(sql.contains("select id from seek where patientid=? order by id desc limit 1"));

        List<ParameterMapping> mappings = boundSql.getParameterMappings();
        assertEquals(3, mappings.size());
        assertEquals("drugs", mappings.get(0).getProperty());
        assertEquals("price", mappings.get(1).getProperty());
        assertEquals("patientid", mappings.get(2).getProperty());
    }

    private MappedStatement updateDrugsStatement() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream inputStream = getClass().getResourceAsStream("/mapper/SeekMapper.xml")) {
            assertNotNull("SeekMapper.xml should be available on the test classpath", inputStream);
            XMLMapperBuilder parser = new XMLMapperBuilder(
                    inputStream,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments());
            parser.parse();
        }
        return configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
    }

    private String normalizeSql(String sql) {
        return sql.replaceAll("\\s+", " ").trim().toLowerCase();
    }
}

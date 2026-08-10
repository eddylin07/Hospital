package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndAccumulatesNullPrice() throws Exception {
        MappedStatement statement = mappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        Seek seek = new Seek();
        seek.setPatientid(42);
        seek.setDrugs("7@2");
        seek.setPrice(new BigDecimal("19.50"));

        BoundSql boundSql = statement.getBoundSql(seek);
        String sql = normalize(boundSql.getSql());

        assertTrue(sql.contains("set drugs=?,price=ifnull(price,0)+?"));
        assertTrue(sql.contains("where patientid=? order by id desc limit 1"));
    }

    private MappedStatement mappedStatement(String id) throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream mapper = getClass().getClassLoader().getResourceAsStream("mapper/SeekMapper.xml")) {
            XMLMapperBuilder builder = new XMLMapperBuilder(mapper, configuration, "mapper/SeekMapper.xml", configuration.getSqlFragments());
            builder.parse();
        }
        return configuration.getMappedStatement(id);
    }

    private String normalize(String sql) {
        return sql.replaceAll("\\s+", " ").replace(" ,", ",").replace(", ", ",").trim().toLowerCase();
    }
}

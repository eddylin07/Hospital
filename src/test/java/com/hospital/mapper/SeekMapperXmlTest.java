package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyTargetsLatestSeekAndKeepsNullPricesBillable() throws Exception {
        BoundSql boundSql = updateDrugsStatement().getBoundSql(sampleSeek());
        String sql = normalizeSql(boundSql.getSql());

        assertTrue(sql, sql.contains("price=ifnull(price,0)+?"));
        assertTrue(sql, sql.contains("where patientid=? order by id desc limit 1"));
    }

    private MappedStatement updateDrugsStatement() throws Exception {
        Configuration configuration = new Configuration();
        String resource = "mapper/SeekMapper.xml";
        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
            mapperParser.parse();
        }
        return configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
    }

    private Seek sampleSeek() {
        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("3@2");
        seek.setPrice(new BigDecimal("16.00"));
        return seek;
    }

    private String normalizeSql(String sql) {
        return sql.replaceAll("\\s+", " ")
                .replace(" = ", "=")
                .replace(" + ", "+")
                .trim()
                .toLowerCase(Locale.ENGLISH);
    }
}

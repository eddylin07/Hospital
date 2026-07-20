package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsTargetsLatestSeekAndAccumulatesNullSafePrice() throws Exception {
        Configuration configuration = new Configuration();
        InputStream mapperStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("mapper/SeekMapper.xml");
        assertNotNull("SeekMapper.xml should be available on the test classpath", mapperStream);

        XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                mapperStream,
                configuration,
                "mapper/SeekMapper.xml",
                configuration.getSqlFragments());
        mapperBuilder.parse();

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        Seek seek = new Seek();
        seek.setPatientid(42);
        seek.setDrugs("3@2");
        seek.setPrice(new BigDecimal("20.00"));

        BoundSql boundSql = statement.getBoundSql(seek);
        String sql = normalizeSql(boundSql.getSql());

        assertTrue(sql, sql.contains("set drugs=?,price=ifnull(price,0)+?"));
        assertTrue(sql, sql.contains("where id = ( select id from ( select id from seek where patientid=? order by id desc limit 1 ) latest_seek )"));
    }

    private static String normalizeSql(String sql) {
        return sql.replaceAll("\\s+", " ").trim().toLowerCase();
    }
}

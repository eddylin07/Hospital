package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsAddsPriceFromZeroWhenExistingPriceIsNull() {
        String sql = updateDrugsSql();

        assertTrue(sql, sql.contains("price=ifnull(price,0)+?"));
    }

    @Test
    public void updateDrugsTargetsOnlyLatestSeekForPatient() {
        String sql = updateDrugsSql();

        assertTrue(sql, sql.contains("where id = ( select id from ( select id from seek where patientid=? order by id desc limit 1 ) latest_seek )"));
    }

    private String updateDrugsSql() {
        Configuration configuration = new Configuration();
        InputStream mapperXml = SeekMapperXmlTest.class.getResourceAsStream("/mapper/SeekMapper.xml");
        assertNotNull("SeekMapper.xml should be available on the test classpath", mapperXml);

        XMLMapperBuilder parser = new XMLMapperBuilder(
                mapperXml,
                configuration,
                "mapper/SeekMapper.xml",
                configuration.getSqlFragments()
        );
        parser.parse();

        BoundSql boundSql = configuration
                .getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs")
                .getBoundSql(new Seek());
        return normalizeSql(boundSql.getSql());
    }

    private String normalizeSql(String sql) {
        return sql.toLowerCase().replaceAll("\\s+", " ").trim();
    }
}

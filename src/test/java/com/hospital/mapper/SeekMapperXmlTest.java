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
    public void updateDrugsUpdatesOnlyLatestSeekAndHandlesNullPrice() throws Exception {
        Configuration configuration = new Configuration();
        InputStream mapperStream = getClass().getClassLoader().getResourceAsStream("mapper/SeekMapper.xml");
        XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                mapperStream,
                configuration,
                "mapper/SeekMapper.xml",
                configuration.getSqlFragments()
        );
        mapperBuilder.parse();

        Seek seek = new Seek();
        seek.setPatientid(42);
        seek.setDrugs("7@2");
        seek.setPrice(new BigDecimal("19.90"));

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        BoundSql boundSql = statement.getBoundSql(seek);
        String sql = boundSql.getSql().replaceAll("\\s+", " ").trim().toLowerCase();

        assertTrue(sql.contains("update seek set drugs=?,price=ifnull(price,0)+? where patientid=?"));
        assertTrue(sql.contains("order by id desc"));
        assertTrue(sql.endsWith("limit 1"));
    }
}

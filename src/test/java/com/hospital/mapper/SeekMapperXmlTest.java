package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {
    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndAccumulatesPriceSafely() throws Exception {
        Configuration configuration = new Configuration();
        String resource = "mapper/SeekMapper.xml";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource);

        XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
        mapperParser.parse();

        Seek seek = new Seek();
        seek.setPatientid(42);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("9.50"));

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        BoundSql boundSql = statement.getBoundSql(seek);
        String sql = boundSql.getSql().replaceAll("\\s+", " ").trim();

        assertTrue(sql.contains("set drugs=?,price=ifnull(price,0)+?"));
        assertTrue(sql.contains("where id = ( select id from ( select id from seek where patientid=? order by id desc limit 1 ) latest_seek )"));
        assertEquals("drugs", boundSql.getParameterMappings().get(0).getProperty());
        assertEquals("price", boundSql.getParameterMappings().get(1).getProperty());
        assertEquals("patientid", boundSql.getParameterMappings().get(2).getProperty());
    }
}

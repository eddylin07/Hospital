package com.hospital.mapper;

import com.hospital.dao.SeekMapper;
import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndKeepsNullPriceBillable() throws Exception {
        Configuration configuration = parseMapperXml("mapper/SeekMapper.xml");
        MappedStatement statement = configuration.getMappedStatement(SeekMapper.class.getName() + ".updateDrugs");

        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("3@2");
        seek.setPrice(new BigDecimal("12.50"));

        BoundSql boundSql = statement.getBoundSql(seek);

        assertEquals(
                "update seek set drugs=?,price=ifnull(price,0)+? where patientid=? order by id desc limit 1",
                normalizeSql(boundSql.getSql())
        );
        assertEquals("drugs", boundSql.getParameterMappings().get(0).getProperty());
        assertEquals("price", boundSql.getParameterMappings().get(1).getProperty());
        assertEquals("patientid", boundSql.getParameterMappings().get(2).getProperty());
    }

    private static Configuration parseMapperXml(String resource) throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream mapperXml = Resources.getResourceAsStream(resource)) {
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                    mapperXml,
                    configuration,
                    resource,
                    configuration.getSqlFragments()
            );
            mapperBuilder.parse();
        }
        return configuration;
    }

    private static String normalizeSql(String sql) {
        return sql.replaceAll("\\s+", " ").trim();
    }
}

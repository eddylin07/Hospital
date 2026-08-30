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
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsUpdatesOnlyLatestSeekAndAccumulatesNullPrice() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream inputStream = Resources.getResourceAsStream("mapper/SeekMapper.xml")) {
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                    inputStream,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments()
            );
            mapperBuilder.parse();
        }

        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("20.00"));

        MappedStatement mappedStatement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        BoundSql boundSql = mappedStatement.getBoundSql(seek);

        assertEquals(
                "update seek set drugs=?,price=ifnull(price,0)+? where patientid=? order by id desc limit 1",
                normalizeSql(boundSql.getSql())
        );
        assertEquals(
                "drugs,price,patientid",
                boundSql.getParameterMappings().stream()
                        .map(parameterMapping -> parameterMapping.getProperty())
                        .collect(Collectors.joining(","))
        );
    }

    private static String normalizeSql(String sql) {
        return sql.replaceAll("\\s+", " ").trim();
    }
}

package com.hospital.mapper;

import com.hospital.dao.SeekMapper;
import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndAccumulatesFromNullPrice() throws Exception {
        MappedStatement statement = loadSeekMapper().getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        BoundSql boundSql = statement.getBoundSql(seekForPrescriptionUpdate());

        String sql = normalizeSql(boundSql.getSql());

        assertTrue(sql.contains("update seek"));
        assertTrue(sql.contains("set drugs=?,price=ifnull(price,0)+?"));
        assertTrue(sql.contains("where patientid=?"));
        assertTrue(sql.contains("order by id desc limit 1"));
        assertEquals(parameterNames(boundSql), "drugs,price,patientid");
    }

    private Configuration loadSeekMapper() throws Exception {
        Configuration configuration = new Configuration();
        configuration.addMapper(SeekMapper.class);

        String resource = "mapper/SeekMapper.xml";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource)) {
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
            mapperParser.parse();
        }

        return configuration;
    }

    private Seek seekForPrescriptionUpdate() {
        Seek seek = new Seek();
        seek.setPatientid(8);
        seek.setDrugs("1@2,3@1");
        seek.setPrice(new BigDecimal("25.50"));
        return seek;
    }

    private String normalizeSql(String sql) {
        return sql.replaceAll("\\s+", " ").replace(" ,", ",").trim().toLowerCase(Locale.ROOT);
    }

    private String parameterNames(BoundSql boundSql) {
        List<String> names = boundSql.getParameterMappings()
                .stream()
                .map(parameterMapping -> parameterMapping.getProperty())
                .collect(Collectors.toList());
        return String.join(",", names);
    }
}

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
import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsUpdatesOnlyLatestSeekAndAddsPriceNullSafely() throws Exception {
        Configuration configuration = new Configuration();
        configuration.addMapper(SeekMapper.class);

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
        seek.setPatientid(9);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("12.50"));

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        BoundSql boundSql = statement.getBoundSql(seek);
        String sql = normalize(boundSql.getSql());

        assertTrue(sql.contains("set drugs=?,price=ifnull(price,0)+?"));
        assertTrue(sql.contains("where patientid=?"));
        assertTrue(sql.contains("order by id desc limit 1"));
    }

    private String normalize(String sql) {
        return sql.toLowerCase(Locale.ENGLISH).replaceAll("\\s+", " ").trim();
    }
}

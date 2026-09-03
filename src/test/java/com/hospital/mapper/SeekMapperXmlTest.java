package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndAccumulatesNullSafePrice() throws Exception {
        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("3@2");
        seek.setPrice(new BigDecimal("12.50"));

        BoundSql boundSql = loadSeekMapperConfiguration()
                .getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs")
                .getBoundSql(seek);

        String sql = normalizeSql(boundSql.getSql());
        assertTrue(sql.contains("update seek"));
        assertTrue(sql.contains("set drugs=?,price=ifnull(price,0)+?"));
        assertTrue(sql.contains("where patientid=?"));
        assertTrue(sql.contains("order by id desc limit 1"));
        assertFalse(sql.contains("price=price+"));
    }

    private Configuration loadSeekMapperConfiguration() throws Exception {
        Configuration configuration = new Configuration();
        try (Reader reader = Resources.getResourceAsReader("mapper/SeekMapper.xml")) {
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                    reader,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments());
            mapperBuilder.parse();
        }
        return configuration;
    }

    private String normalizeSql(String sql) {
        return sql.replaceAll("\\s+", " ")
                .replace(" ,", ",")
                .replace(", ", ",")
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}

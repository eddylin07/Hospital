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

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndHandlesNullPrice() throws Exception {
        Configuration configuration = new Configuration();
        configuration.addMapper(SeekMapper.class);
        try (InputStream mapperXml = getClass().getClassLoader().getResourceAsStream("mapper/SeekMapper.xml")) {
            XMLMapperBuilder builder = new XMLMapperBuilder(
                    mapperXml,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments());
            builder.parse();
        }

        Seek seek = new Seek();
        seek.setPatientid(42);
        seek.setDrugs("7@2");
        seek.setPrice(new BigDecimal("13.50"));

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        BoundSql boundSql = statement.getBoundSql(seek);
        String sql = boundSql.getSql().replaceAll("\\s+", " ").trim().toLowerCase();
        String compactSql = sql.replace(" ", "");

        assertTrue(sql, compactSql.contains("price=ifnull(price,0)+?"));
        assertTrue(sql, compactSql.contains("whereid=(selectidfrom(selectidfromseekwherepatientid=?orderbyiddesclimit1)"));
    }
}

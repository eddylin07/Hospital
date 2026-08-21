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
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekForPatient() throws Exception {
        String sql = updateDrugsSql();

        assertTrue(sql, sql.contains("where patientid=? order by id desc limit 1"));
    }

    @Test
    public void updateDrugsAccumulatesPriceWhenExistingPriceIsNull() throws Exception {
        String sql = updateDrugsSql();

        assertTrue(sql, sql.contains("price=ifnull(price,0)+?"));
    }

    @Test
    public void updateDrugsBindsDrugsPriceAndPatientId() throws Exception {
        MappedStatement statement = updateDrugsStatement();
        BoundSql boundSql = statement.getBoundSql(seek());

        assertEquals(3, boundSql.getParameterMappings().size());
        assertEquals("drugs", boundSql.getParameterMappings().get(0).getProperty());
        assertEquals("price", boundSql.getParameterMappings().get(1).getProperty());
        assertEquals("patientid", boundSql.getParameterMappings().get(2).getProperty());
    }

    private String updateDrugsSql() throws Exception {
        BoundSql boundSql = updateDrugsStatement().getBoundSql(seek());
        return boundSql.getSql().replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
    }

    private MappedStatement updateDrugsStatement() throws Exception {
        Configuration configuration = new Configuration();
        configuration.addMapper(SeekMapper.class);
        try (InputStream mapperXml = getClass().getResourceAsStream("/mapper/SeekMapper.xml")) {
            assertNotNull("SeekMapper.xml must be available on the test classpath", mapperXml);

            XMLMapperBuilder builder = new XMLMapperBuilder(
                    mapperXml,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments()
            );
            builder.parse();
        }

        return configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
    }

    private Seek seek() {
        Seek seek = new Seek();
        seek.setPatientid(42);
        seek.setDrugs("7@2");
        seek.setPrice(new BigDecimal("18.50"));
        return seek;
    }
}

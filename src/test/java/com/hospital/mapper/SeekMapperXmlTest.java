package com.hospital.mapper;

import com.hospital.dao.SeekMapper;
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
    public void updateDrugsOnlyTargetsLatestSeekForPatient() {
        Configuration configuration = loadSeekMapperConfiguration();
        Seek seek = new Seek();
        seek.setPatientid(1);

        BoundSql boundSql = configuration
                .getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs")
                .getBoundSql(seek);

        String normalizedSql = boundSql.getSql().toLowerCase().replaceAll("\\s+", " ").trim();
        assertTrue(normalizedSql.contains("where patientid=?"));
        assertTrue(normalizedSql.contains("order by id desc"));
        assertTrue(normalizedSql.endsWith("limit 1"));
    }

    private Configuration loadSeekMapperConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addMapper(SeekMapper.class);

        InputStream mapperXml = getClass().getResourceAsStream("/mapper/SeekMapper.xml");
        assertNotNull(mapperXml);

        XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                mapperXml,
                configuration,
                "mapper/SeekMapper.xml",
                configuration.getSqlFragments());
        mapperBuilder.parse();
        return configuration;
    }
}

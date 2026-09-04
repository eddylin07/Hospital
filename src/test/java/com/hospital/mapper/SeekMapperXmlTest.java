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

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndTreatsMissingPriceAsZero() throws Exception {
        Configuration configuration = new Configuration();
        configuration.addMapper(SeekMapper.class);
        InputStream mapper = Resources.getResourceAsStream("mapper/SeekMapper.xml");
        XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                mapper,
                configuration,
                "mapper/SeekMapper.xml",
                configuration.getSqlFragments());
        mapperBuilder.parse();

        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("9.00"));
        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        BoundSql boundSql = statement.getBoundSql(seek);

        String sql = boundSql.getSql().replaceAll("\\s+", " ").trim().toLowerCase();
        assertThat(sql, containsString("set drugs=?,price=ifnull(price,0)+?"));
        assertThat(sql, containsString("where patientid=? order by id desc limit 1"));
    }
}

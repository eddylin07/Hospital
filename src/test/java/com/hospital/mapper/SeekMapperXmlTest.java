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
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekRowAndAccumulatesNullPrice() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream inputStream = Resources.getResourceAsStream("mapper/SeekMapper.xml")) {
            new XMLMapperBuilder(inputStream, configuration, "mapper/SeekMapper.xml",
                    configuration.getSqlFragments()).parse();
        }

        Seek seek = new Seek();
        seek.setDrugs("12@2");
        seek.setPrice(new BigDecimal("19.80"));
        seek.setPatientid(7);

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        BoundSql boundSql = statement.getBoundSql(seek);
        String compactSql = boundSql.getSql().replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
        List<String> parameterProperties = boundSql.getParameterMappings().stream()
                .map(parameterMapping -> parameterMapping.getProperty())
                .collect(Collectors.toList());

        assertThat(compactSql, containsString("setdrugs=?,price=ifnull(price,0)+?"));
        assertThat(compactSql, containsString("whereid=(selectidfrom(selectidfromseek"));
        assertThat(compactSql, containsString("wherepatientid=?orderbyiddesclimit1"));
        assertThat(compactSql, containsString("latest_seek)"));
        assertThat(parameterProperties.toString(), is("[drugs, price, patientid]"));
    }
}

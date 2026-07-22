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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndAccumulatesNullSafePrice() throws Exception {
        MappedStatement statement = mappedStatement("com.hospital.dao.SeekMapper.updateDrugs");

        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("12.50"));

        BoundSql boundSql = statement.getBoundSql(seek);

        assertEquals(
                "update seek set drugs=?,price=ifnull(price,0)+? where patientid=? order by id desc limit 1",
                normalize(boundSql.getSql())
        );
        assertEquals(
                Arrays.asList("drugs", "price", "patientid"),
                parameterNames(boundSql)
        );
    }

    private MappedStatement mappedStatement(String id) throws Exception {
        Configuration configuration = new Configuration();
        configuration.addMapper(SeekMapper.class);

        InputStream inputStream = Resources.getResourceAsStream("mapper/SeekMapper.xml");
        try {
            XMLMapperBuilder parser = new XMLMapperBuilder(
                    inputStream,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments()
            );
            parser.parse();
        } finally {
            inputStream.close();
        }

        return configuration.getMappedStatement(id);
    }

    private String normalize(String sql) {
        return sql.replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
    }

    private List<String> parameterNames(BoundSql boundSql) {
        List<String> names = new java.util.ArrayList<String>();
        for (org.apache.ibatis.mapping.ParameterMapping mapping : boundSql.getParameterMappings()) {
            names.add(mapping.getProperty());
        }
        return names;
    }
}

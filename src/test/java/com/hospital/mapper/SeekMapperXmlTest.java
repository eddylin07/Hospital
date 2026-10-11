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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestPatientSeekAndAddsToNullPrice() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream mapper = Resources.getResourceAsStream("mapper/SeekMapper.xml")) {
            XMLMapperBuilder parser = new XMLMapperBuilder(
                    mapper,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments()
            );
            parser.parse();
        }

        Seek seek = new Seek();
        seek.setPatientid(42);
        seek.setDrugs("3@2");
        seek.setPrice(new BigDecimal("19.50"));

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        BoundSql boundSql = statement.getBoundSql(seek);

        assertThat(normalize(boundSql.getSql()), containsString(
                "update seek set drugs=?,price=ifnull(price,0)+? where patientid=? order by id desc limit 1"
        ));
        assertThat(boundSql.getParameterMappings().get(0).getProperty(), equalTo("drugs"));
        assertThat(boundSql.getParameterMappings().get(1).getProperty(), equalTo("price"));
        assertThat(boundSql.getParameterMappings().get(2).getProperty(), equalTo("patientid"));
    }

    private static String normalize(String sql) {
        return sql.replaceAll("\\s+", " ").trim();
    }
}

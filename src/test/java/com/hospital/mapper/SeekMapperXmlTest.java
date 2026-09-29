package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyTargetsLatestSeekForPatient() throws Exception {
        BoundSql boundSql = updateDrugsBoundSql();
        String sql = normalize(boundSql.getSql());

        assertThat(sql)
                .contains("update seek")
                .contains("where patientid=?")
                .contains("order by id desc")
                .contains("limit 1");

        assertThat(parameterNames(boundSql))
                .containsExactly("drugs", "price", "patientid");
    }

    @Test
    public void updateDrugsAddsChargesWhenExistingPriceIsNull() throws Exception {
        String sql = normalize(updateDrugsBoundSql().getSql());

        assertThat(sql)
                .contains("price=ifnull(price,0)+?")
                .doesNotContain("price=price+?");
    }

    private BoundSql updateDrugsBoundSql() throws Exception {
        Configuration configuration = new Configuration();
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
        seek.setPatientid(7);
        seek.setDrugs("3@2");
        seek.setPrice(new BigDecimal("12.50"));

        MappedStatement statement = configuration.getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs");
        return statement.getBoundSql(seek);
    }

    private List<String> parameterNames(BoundSql boundSql) {
        return boundSql.getParameterMappings().stream()
                .map(ParameterMapping::getProperty)
                .collect(Collectors.toList());
    }

    private String normalize(String sql) {
        return sql.replaceAll("\\s+", " ")
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}

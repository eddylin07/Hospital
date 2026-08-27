package com.hospital.mapper;

import com.hospital.entity.Seek;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekForPatient() {
        String sql = updateDrugsSql();

        assertThat(sql).contains("where patientid=?");
        assertThat(sql).contains("order by id desc");
        assertThat(sql).contains("limit 1");
    }

    @Test
    public void updateDrugsTreatsMissingExistingPriceAsZero() {
        String sql = updateDrugsSql();

        assertThat(sql).contains("price=ifnull(price,0)+?");
    }

    private String updateDrugsSql() {
        Configuration configuration = new Configuration();
        try (InputStream mapperXml = new FileInputStream("src/main/resources/mapper/SeekMapper.xml")) {
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                    mapperXml,
                    configuration,
                    "mapper/SeekMapper.xml",
                    configuration.getSqlFragments()
            );
            mapperBuilder.parse();
        } catch (Exception e) {
            throw new AssertionError("Unable to parse SeekMapper.xml", e);
        }

        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("12.50"));
        BoundSql boundSql = configuration
                .getMappedStatement("com.hospital.dao.SeekMapper.updateDrugs")
                .getBoundSql(seek);
        return normalizeSql(boundSql.getSql());
    }

    private String normalizeSql(String sql) {
        return sql.replaceAll("\\s+", " ").replace(" ,", ",").replace(", ", ",").trim().toLowerCase();
    }
}

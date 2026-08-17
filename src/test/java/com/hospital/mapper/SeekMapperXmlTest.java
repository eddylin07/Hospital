package com.hospital.mapper;

import com.hospital.entity.Seek;
import java.io.InputStream;
import java.math.BigDecimal;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {
    private static final String MAPPER_RESOURCE = "mapper/SeekMapper.xml";
    private static final String UPDATE_DRUGS_STATEMENT = "com.hospital.dao.SeekMapper.updateDrugs";

    @Test
    public void updateDrugsTargetsOnlyTheLatestSeekForPatient() throws Exception {
        String sql = updateDrugsSql();

        assertTrue("updateDrugs must stay scoped to the selected patient", sql.contains("wherepatientid=?"));
        assertTrue("updateDrugs must update the newest seek row, not every historical seek", sql.contains("orderbyiddesclimit1"));
    }

    @Test
    public void updateDrugsAddsPriceWhenExistingPriceIsNull() throws Exception {
        String sql = updateDrugsSql();

        assertTrue("updateDrugs must treat a null existing price as zero before adding", sql.contains("price=ifnull(price,0)+?"));
    }

    private String updateDrugsSql() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream mapperXml = Resources.getResourceAsStream(MAPPER_RESOURCE)) {
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                    mapperXml,
                    configuration,
                    MAPPER_RESOURCE,
                    configuration.getSqlFragments());
            mapperBuilder.parse();
        }

        Seek seek = new Seek();
        seek.setPatientid(7);
        seek.setDrugs("1@2");
        seek.setPrice(new BigDecimal("12.50"));

        BoundSql boundSql = configuration.getMappedStatement(UPDATE_DRUGS_STATEMENT).getBoundSql(seek);
        return boundSql.getSql().replaceAll("\\s+", "").toLowerCase();
    }
}

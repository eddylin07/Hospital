package com.hospital.dao;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyTargetsLatestSeekForPatient() throws Exception {
        String mapperXml = new String(
                Files.readAllBytes(Paths.get("src/main/resources/mapper/SeekMapper.xml")),
                StandardCharsets.UTF_8);
        String updateSql = mapperXml.substring(
                mapperXml.indexOf("<update id=\"updateDrugs\""),
                mapperXml.indexOf("</update>", mapperXml.indexOf("<update id=\"updateDrugs\"")));
        String normalizedSql = updateSql.replaceAll("\\s+", " ").toLowerCase();

        assertTrue(normalizedSql.contains("where id = ("));
        assertTrue(normalizedSql.contains("order by id desc"));
        assertTrue(normalizedSql.contains("limit 0,1"));
        assertFalse(normalizedSql.matches(".*set drugs=#\\{drugs\\},price=price\\+#\\{price\\} where patientid=#\\{patientid\\}.*"));
    }
}

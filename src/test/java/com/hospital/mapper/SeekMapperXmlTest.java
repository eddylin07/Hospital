package com.hospital.mapper;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyTargetsLatestSeekForPatient() throws Exception {
        String xml = new String(
                Files.readAllBytes(Paths.get("src/main/resources/mapper/SeekMapper.xml")),
                StandardCharsets.UTF_8);
        String updateDrugs = xml.substring(xml.indexOf("<update id=\"updateDrugs\""), xml.indexOf("</update>", xml.indexOf("<update id=\"updateDrugs\"")));
        String normalized = updateDrugs.replaceAll("\\s+", " ").toLowerCase();

        assertTrue(normalized.contains("where id ="));
        assertTrue(normalized.contains("select id from seek where patientid=#{patientid} order by id desc limit 1"));
        assertFalse(normalized.contains("set drugs=#{drugs},price=price+#{price} where patientid=#{patientid}"));
    }
}

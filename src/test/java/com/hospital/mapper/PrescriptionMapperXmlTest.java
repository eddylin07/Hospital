package com.hospital.mapper;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PrescriptionMapperXmlTest {
    @Test
    public void updateDrugsTargetsOnlyLatestSeekRow() throws Exception {
        String updateDrugs = statement("src/main/resources/mapper/SeekMapper.xml", "updateDrugs");
        String normalized = normalize(updateDrugs);

        assertTrue(normalized.contains("update seek set drugs=#{drugs},price=ifnull(price,0)+#{price} where patientid=#{patientid} order by id desc limit 1"));
        assertFalse(normalized.contains("price=price+#{price} where patientid=#{patientid}"));
    }

    @Test
    public void updateNumberOnlyDeductsWhenEnoughStockRemains() throws Exception {
        String updateNumber = statement("src/main/resources/mapper/DrugsMapper.xml", "updateNumber");
        String normalized = normalize(updateNumber);

        assertTrue(normalized.contains("update drugs set number=number-#{number} where id=#{id} and number>=#{number}"));
    }

    private String statement(String path, String id) throws Exception {
        String xml = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        String startTag = "<update id=\"" + id + "\"";
        int start = xml.indexOf(startTag);
        int end = xml.indexOf("</update>", start);
        return xml.substring(start, end);
    }

    private String normalize(String sql) {
        return sql.replaceAll("\\s+", " ").trim();
    }
}

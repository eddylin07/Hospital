package com.hospital.mapper;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class SeekMapperSqlTest {
    @Test
    public void updateDrugsOnlyTargetsLatestSeekForPatient() throws Exception {
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(Paths.get("src/main/resources/mapper/SeekMapper.xml").toFile());

        String sql = findUpdateSql(document, "updateDrugs")
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase();

        assertTrue(sql.contains("where patientid=#{patientid}"));
        assertTrue(sql.contains("order by id desc limit 1"));
        assertTrue(sql.indexOf("where patientid=#{patientid}") < sql.indexOf("order by id desc limit 1"));
    }

    private String findUpdateSql(Document document, String id) {
        NodeList updates = document.getElementsByTagName("update");
        for (int i = 0; i < updates.getLength(); i++) {
            Element update = (Element) updates.item(i);
            if (id.equals(update.getAttribute("id"))) {
                return update.getTextContent();
            }
        }
        throw new AssertionError("Missing update statement: " + id);
    }
}

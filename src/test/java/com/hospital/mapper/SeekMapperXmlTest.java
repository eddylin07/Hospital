package com.hospital.mapper;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {
    @Test
    public void updateDrugsOnlyUpdatesLatestSeekForPatient() throws Exception {
        String sql = findUpdateSql("updateDrugs").replaceAll("\\s+", " ").trim().toLowerCase();

        assertTrue(sql.contains("where patientid=#{patientid}"));
        assertTrue(sql.contains("order by id desc"));
        assertTrue(sql.contains("limit 1"));
    }

    private String findUpdateSql(String updateId) throws Exception {
        Path mapper = Paths.get("src/main/resources/mapper/SeekMapper.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        Document document = factory.newDocumentBuilder().parse(mapper.toFile());
        NodeList updates = document.getElementsByTagName("update");
        for (int i = 0; i < updates.getLength(); i++) {
            Element update = (Element) updates.item(i);
            if (updateId.equals(update.getAttribute("id"))) {
                return update.getTextContent();
            }
        }
        throw new AssertionError("Missing update statement: " + updateId);
    }
}

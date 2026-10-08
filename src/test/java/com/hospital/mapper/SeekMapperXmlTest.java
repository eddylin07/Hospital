package com.hospital.mapper;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsStatementUpdatesDrugsPriceAndScopesByPatient() throws Exception {
        Element updateDrugs = findUpdateDrugsElement();

        assertNotNull("updateDrugs mapper statement should exist", updateDrugs);
        String sql = updateDrugs.getTextContent().replaceAll("\\s+", " ").trim();

        assertTrue(sql.contains("update seek"));
        assertTrue(sql.contains("set drugs=#{drugs},price=price+#{price}"));
        assertTrue(sql.contains("where patientid=#{patientid}"));
    }

    private Element findUpdateDrugsElement() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        Document document = factory.newDocumentBuilder()
                .parse(new File("src/main/resources/mapper/SeekMapper.xml"));

        NodeList updates = document.getElementsByTagName("update");
        for (int index = 0; index < updates.getLength(); index++) {
            Element element = (Element) updates.item(index);
            if ("updateDrugs".equals(element.getAttribute("id"))) {
                return element;
            }
        }
        return null;
    }
}

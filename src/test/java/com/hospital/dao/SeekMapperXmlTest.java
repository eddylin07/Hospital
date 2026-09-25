package com.hospital.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyTargetsLatestSeekForPatient() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
        Document document;
        try (InputStream inputStream = getClass().getResourceAsStream("/mapper/SeekMapper.xml")) {
            assertNotNull(inputStream);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
            document = builder.parse(inputStream);
        }

        XPath xpath = XPathFactory.newInstance().newXPath();
        Node updateDrugs = (Node) xpath.evaluate(
                "/mapper/update[@id='updateDrugs']",
                document,
                XPathConstants.NODE);
        assertNotNull(updateDrugs);
        String sql = updateDrugs.getTextContent().replaceAll("\\s+", " ").trim().toLowerCase();

        assertTrue(sql.contains("where id = ( select id from ( select id from seek where patientid=#{patientid}"));
        assertTrue(sql.contains("order by id desc"));
        assertTrue(sql.contains("limit 0,1"));
        assertFalse(sql.endsWith("where patientid=#{patientid}"));
    }
}

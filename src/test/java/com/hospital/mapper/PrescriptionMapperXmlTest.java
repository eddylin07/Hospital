package com.hospital.mapper;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class PrescriptionMapperXmlTest {

    @Test
    public void seekMapperKeepsPrescriptionUpdateEnabledForPatientVisits() throws Exception {
        String sql = mapperStatementSql("src/main/resources/mapper/SeekMapper.xml", "update", "updateDrugs");

        assertTrue(sql.contains("update seek"));
        assertTrue(sql.contains("set drugs=#{drugs},price=price+#{price}"));
        assertTrue(sql.contains("where patientid=#{patientid}"));
    }

    @Test
    public void seekMapperSelectsNewestVisitForPatientLookup() throws Exception {
        String sql = mapperStatementSql("src/main/resources/mapper/SeekMapper.xml", "select", "getSeekByPatientId");

        assertTrue(sql.contains("where patientid=#{patientid}"));
        assertTrue(sql.contains("order by id desc"));
        assertTrue(sql.contains("limit 0,1"));
    }

    @Test
    public void drugsMapperKeepsInventoryDecrementStatementBoundToDrugId() throws Exception {
        String sql = mapperStatementSql("src/main/resources/mapper/DrugsMapper.xml", "update", "updateNumber");

        assertTrue(sql.contains("update drugs set number=number-#{number}"));
        assertTrue(sql.contains("where id=#{id}"));
    }

    private String mapperStatementSql(String mapperPath, String tagName, String statementId) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
        Document document;
        try (InputStream inputStream = new FileInputStream(mapperPath)) {
            document = builder.parse(inputStream);
        }

        NodeList statements = document.getDocumentElement().getElementsByTagName(tagName);
        for (int index = 0; index < statements.getLength(); index++) {
            Node statement = statements.item(index);
            if (statement instanceof Element && statementId.equals(((Element) statement).getAttribute("id"))) {
                return statement.getTextContent().replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
            }
        }
        throw new AssertionError("Missing " + tagName + " statement: " + statementId);
    }
}

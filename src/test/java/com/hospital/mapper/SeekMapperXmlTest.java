package com.hospital.mapper;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsTargetsOnlyTheLatestSeekRecord() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver(localMyBatisDtdResolver());

        Document document = builder.parse(new File("src/main/resources/mapper/SeekMapper.xml"));
        Element updateDrugs = findUpdateStatement(document, "updateDrugs");
        assertNotNull(updateDrugs);
        String sql = updateDrugs.getTextContent().replaceAll("\\s+", " ").trim().toLowerCase();

        assertTrue(sql.contains("where id=("));
        assertTrue(sql.contains("select id from seek where patientid=#{patientid} order by id desc limit 1"));
        assertFalse(sql.contains("where patientid=#{patientid}"));
    }

    private EntityResolver localMyBatisDtdResolver() {
        return (publicId, systemId) -> new InputSource(new StringReader(""));
    }

    private Element findUpdateStatement(Document document, String id) {
        NodeList updates = document.getElementsByTagName("update");
        for (int i = 0; i < updates.getLength(); i++) {
            Element update = (Element) updates.item(i);
            if (id.equals(update.getAttribute("id"))) {
                return update;
            }
        }
        return null;
    }
}

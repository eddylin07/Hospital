package com.hospital.dao;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Locale;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsTargetsOnlyLatestSeekRecordForPatient() throws Exception {
        String sql = updateDrugsSql();

        assertTrue(sql.contains("where patientid=#{patientid} and id ="));
        assertTrue(sql.contains("from ( select id from seek where patientid=#{patientid} order by id desc limit 0,1 ) latest_seek"));
        assertFalse(sql.endsWith("where patientid=#{patientid}"));
    }

    private String updateDrugsSql() throws Exception {
        InputStream mapper = getClass().getResourceAsStream("/mapper/SeekMapper.xml");
        assertNotNull(mapper);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
        Document document = builder.parse(mapper);
        NodeList updates = document.getElementsByTagName("update");

        for (int i = 0; i < updates.getLength(); i++) {
            Element update = (Element) updates.item(i);
            if ("updateDrugs".equals(update.getAttribute("id"))) {
                return update.getTextContent().replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
            }
        }

        throw new AssertionError("updateDrugs mapper statement was not found");
    }
}

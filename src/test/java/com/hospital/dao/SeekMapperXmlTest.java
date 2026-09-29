package com.hospital.dao;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;
import java.util.Locale;

import static org.junit.Assert.assertTrue;
import org.xml.sax.InputSource;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsTargetsOnlyLatestSeekForPatient() throws Exception {
        File mapperFile = new File("src/main/resources/mapper/SeekMapper.xml");
        javax.xml.parsers.DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
        builder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
        Document document = builder.parse(mapperFile);

        Element updateDrugs = findUpdateById(document, "updateDrugs");
        String sql = updateDrugs.getTextContent()
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase(Locale.ROOT);

        assertTrue(sql.contains("where patientid=#{patientid} order by id desc limit 1"));
    }

    private Element findUpdateById(Document document, String id) {
        NodeList updates = document.getElementsByTagName("update");
        for (int i = 0; i < updates.getLength(); i++) {
            Element update = (Element) updates.item(i);
            if (id.equals(update.getAttribute("id"))) {
                return update;
            }
        }
        throw new AssertionError("Missing update statement: " + id);
    }
}

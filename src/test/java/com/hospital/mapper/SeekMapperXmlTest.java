package com.hospital.mapper;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {
    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndAddsPriceNullSafely() throws Exception {
        String sql = mapperSql("update", "updateDrugs");

        assertTrue(sql.contains("set drugs=#{drugs},price=ifnull(price,0)+#{price}"));
        assertTrue(sql.contains("where patientid=#{patientid} order by id desc limit 1"));
    }

    private static String mapperSql(String tagName, String id) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
        Document document = builder.parse(new File("src/main/resources/mapper/SeekMapper.xml"));
        NodeList nodes = document.getElementsByTagName(tagName);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            if (id.equals(element.getAttribute("id"))) {
                return element.getTextContent().replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
            }
        }
        throw new AssertionError("Mapper statement not found: " + id);
    }
}

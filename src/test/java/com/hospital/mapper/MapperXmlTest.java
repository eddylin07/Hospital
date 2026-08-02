package com.hospital.mapper;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class MapperXmlTest {
    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndTreatsNullPriceAsZero() throws Exception {
        String sql = normalizedStatement("src/main/resources/mapper/SeekMapper.xml", "update", "updateDrugs");

        assertTrue(sql, sql.contains("price=ifnull(price,0)+#{price}"));
        assertTrue(sql, sql.contains("where id = (select id from (select id from seek where patientid=#{patientid} order by id desc limit 1) latest_seek)"));
    }

    @Test
    public void drugsPrimaryKeyMappingIncludesInventoryAndPriceFields() throws Exception {
        Document document = parse("src/main/resources/mapper/DrugsMapper.xml");
        NodeList nodes = (NodeList) XPathFactory.newInstance().newXPath()
                .evaluate("//resultMap[@id='BaseResultMap']/*/@property", document, XPathConstants.NODESET);
        Set<String> properties = new HashSet<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            properties.add(node.getNodeValue());
        }

        assertTrue(properties.toString(), properties.contains("price"));
        assertTrue(properties.toString(), properties.contains("number"));
        assertTrue(properties.toString(), properties.contains("text"));
    }

    private static String normalizedStatement(String path, String tagName, String id) throws Exception {
        Document document = parse(path);
        Node node = (Node) XPathFactory.newInstance().newXPath()
                .evaluate("//" + tagName + "[@id='" + id + "']", document, XPathConstants.NODE);
        return node.getTextContent().replaceAll("\\s+", " ").trim();
    }

    private static Document parse(String path) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        return factory.newDocumentBuilder().parse(new File(path));
    }
}

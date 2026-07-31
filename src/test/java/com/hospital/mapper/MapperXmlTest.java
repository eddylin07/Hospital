package com.hospital.mapper;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MapperXmlTest {
    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndAccumulatesNullSafePrice() throws Exception {
        Element update = elementByTagAndId("SeekMapper.xml", "update", "updateDrugs");
        String sql = normalizeSql(update.getTextContent());

        assertTrue(sql, sql.contains("set drugs=#{drugs},price=ifnull(price,0)+#{price}"));
        assertTrue(sql, sql.contains("where id=( select id from ( select id from seek where patientid=#{patientid} order by id desc limit 1 ) latest_seek )"));
    }

    @Test
    public void selectDrugByPrimaryKeyMapsFieldsNeededForDispensing() throws Exception {
        Element select = elementByTagAndId("DrugsMapper.xml", "select", "selectByPrimaryKey");
        assertEquals("BaseResultMap", select.getAttribute("resultMap"));

        Element resultMap = elementByTagAndId("DrugsMapper.xml", "resultMap", "BaseResultMap");
        Set<String> properties = mappedProperties(resultMap);

        assertTrue(properties.contains("id"));
        assertTrue(properties.contains("name"));
        assertTrue(properties.contains("type"));
        assertTrue(properties.contains("price"));
        assertTrue(properties.contains("number"));
        assertTrue(properties.contains("text"));
    }

    private Element elementByTagAndId(String mapperFile, String tagName, String id) throws Exception {
        NodeList elements = mapperDocument(mapperFile).getElementsByTagName(tagName);
        for (int i = 0; i < elements.getLength(); i++) {
            Element element = (Element) elements.item(i);
            if (id.equals(element.getAttribute("id"))) {
                return element;
            }
        }
        throw new AssertionError("Missing " + tagName + " with id " + id + " in " + mapperFile);
    }

    private Document mapperDocument(String mapperFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
        return builder.parse(new File("src/main/resources/mapper/" + mapperFile));
    }

    private Set<String> mappedProperties(Element resultMap) {
        Set<String> properties = new HashSet<>();
        NodeList children = resultMap.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if ("id".equals(element.getTagName()) || "result".equals(element.getTagName())) {
                    properties.add(element.getAttribute("property"));
                }
            }
        }
        return properties;
    }

    private String normalizeSql(String sql) {
        return sql.replaceAll("\\s+", " ").trim();
    }
}


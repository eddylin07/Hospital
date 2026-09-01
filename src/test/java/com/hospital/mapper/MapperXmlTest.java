package com.hospital.mapper;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapperXmlTest {

    @Test
    public void updateDrugsOnlyTouchesLatestSeekAndKeepsNullPriceBillable() throws Exception {
        String sql = normalize(textOfElementWithId("mapper/SeekMapper.xml", "updateDrugs"));

        assertTrue(sql.contains("set drugs=#{drugs},price=ifnull(price,0)+#{price}"));
        assertTrue(sql.contains("where patientid=#{patientid}"));
        assertTrue(sql.contains("order by id desc"));
        assertTrue(sql.contains("limit 1"));
    }

    @Test
    public void drugsPrimaryKeyResultMapIncludesDispensingFields() throws Exception {
        Set<String> mappedProperties = resultMapProperties("mapper/DrugsMapper.xml", "BaseResultMap");

        assertTrue(mappedProperties.containsAll(Arrays.asList("id", "name", "type", "price", "number", "text")));
    }

    private static String textOfElementWithId(String resource, String id) throws Exception {
        return elementWithId(resource, id).getTextContent();
    }

    private static Set<String> resultMapProperties(String resource, String id) throws Exception {
        Element resultMap = elementWithId(resource, id);
        NodeList children = resultMap.getChildNodes();
        Set<String> properties = new HashSet<>();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                Element element = (Element) child;
                String property = element.getAttribute("property");
                if (!property.isEmpty()) {
                    properties.add(property);
                }
            }
        }
        return properties;
    }

    private static Element elementWithId(String resource, String id) throws Exception {
        Document document = parseXmlResource(resource);
        NodeList children = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                Element element = (Element) child;
                if (id.equals(element.getAttribute("id"))) {
                    return element;
                }
            }
        }
        throw new AssertionError("Missing element id " + id + " in " + resource);
    }

    private static Document parseXmlResource(String resource) throws Exception {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        assertNotNull("Missing resource " + resource, inputStream);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        return factory.newDocumentBuilder().parse(inputStream);
    }

    private static String normalize(String sql) {
        return sql.replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
    }
}


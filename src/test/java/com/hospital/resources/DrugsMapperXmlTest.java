package com.hospital.resources;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

public class DrugsMapperXmlTest {

    @Test
    public void selectByPrimaryKeyMapsFieldsUsedByDispensing() throws Exception {
        Document document = mapperDocument();
        XPath xpath = XPathFactory.newInstance().newXPath();

        assertThat(hasResultProperty(xpath, document, "price")).isTrue();
        assertThat(hasResultProperty(xpath, document, "number")).isTrue();
        assertThat(hasResultProperty(xpath, document, "text")).isTrue();

        String baseColumns = (String) xpath.evaluate(
                "normalize-space(//sql[@id='Base_Column_List'])",
                document,
                XPathConstants.STRING);
        assertThat(baseColumns.replaceAll("\\s+", "")).isEqualTo("id,name,type,price,number,text");
    }

    private boolean hasResultProperty(XPath xpath, Document document, String property) throws Exception {
        Double count = (Double) xpath.evaluate(
                "count(//resultMap[@id='BaseResultMap']/*[@property='" + property + "'])",
                document,
                XPathConstants.NUMBER);
        return count.intValue() == 1;
    }

    private Document mapperDocument() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
        return builder.parse(new File("src/main/resources/mapper/DrugsMapper.xml"));
    }
}

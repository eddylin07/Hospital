package com.hospital.mapper;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SeekMapperXmlTest {
    @Test
    public void updateDrugsOnlyTargetsLatestSeekAndAddsPriceSafely() throws Exception {
        String sql = updateDrugsSql();
        String compactSql = sql.toLowerCase().replaceAll("\\s+", "");

        assertTrue(compactSql.contains("whereid=("));
        assertTrue(compactSql.contains("wherepatientid=#{patientid}orderbyiddesclimit1"));
        assertTrue(compactSql.contains("price=ifnull(price,0)+#{price}"));
        assertFalse(compactSql.contains("price=price+#{price}"));
    }

    private String updateDrugsSql() throws Exception {
        InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("mapper/SeekMapper.xml");
        assertNotNull(stream);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setFeature("http://xml.org/sax/features/validation", false);
        factory.setExpandEntityReferences(false);
        Document document = factory.newDocumentBuilder().parse(new InputSource(stream));
        return XPathFactory.newInstance()
                .newXPath()
                .evaluate("normalize-space(/mapper/update[@id='updateDrugs'])", document);
    }
}

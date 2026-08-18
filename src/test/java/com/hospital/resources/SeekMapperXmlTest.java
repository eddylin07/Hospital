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
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class SeekMapperXmlTest {

    @Test
    public void updateDrugsOnlyUpdatesLatestSeekAndAccumulatesNullPrice() throws Exception {
        String sql = mapperStatementSql("updateDrugs");

        assertThat(sql).contains("update seek");
        assertThat(sql).contains("set drugs=#{drugs},price=ifnull(price,0)+#{price}");
        assertThat(sql).contains("where patientid=#{patientid}");
        assertThat(sql).contains("order by id desc");
        assertThat(sql).contains("limit 1");
        assertThat(sql).doesNotContain("price=price+#{price}");
        assertThat(sql.indexOf("where patientid=#{patientid}")).isLessThan(sql.indexOf("order by id desc"));
        assertThat(sql.indexOf("order by id desc")).isLessThan(sql.indexOf("limit 1"));
    }

    private String mapperStatementSql(String statementId) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
        Document document = builder.parse(new File("src/main/resources/mapper/SeekMapper.xml"));

        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "normalize-space(//update[@id='" + statementId + "'])";
        String sql = (String) xpath.evaluate(expression, document, XPathConstants.STRING);
        return sql.replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }
}

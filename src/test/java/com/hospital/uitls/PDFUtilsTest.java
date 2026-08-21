package com.hospital.uitls;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PDFUtilsTest {
    @Test
    public void outputFileSanitizesPatientNamePathSegments() throws Exception {
        File baseDir = new File("target/pdf-output");

        File output = PDFUtils.buildOutputFile(baseDir.getPath(), "../evil\\patient", "挂号单.pdf");

        String basePath = baseDir.getCanonicalPath();
        String outputPath = output.getCanonicalPath();
        assertTrue(outputPath.startsWith(basePath + File.separator));
        assertFalse(output.getName().contains(".."));
        assertFalse(output.getName().contains("/"));
        assertFalse(output.getName().contains("\\"));
    }
}

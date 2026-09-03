package com.hospital.config;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WebConfigTest {

    @Test
    public void publicHospitalPagesRemainExcludedFromLoginInterceptor() throws Exception {
        String source = new String(
                Files.readAllBytes(Paths.get("src/main/java/com/hospital/config/WebConfig.java")),
                StandardCharsets.UTF_8
        );

        Assert.assertTrue(source.contains("\"/patient/searchinfo\""));
        Assert.assertTrue(source.contains("\"/hospital/expert\""));
        Assert.assertTrue(source.contains("\"/hospital/seekguide\""));
        Assert.assertTrue(source.contains("\"/hospital/patientservice\""));
        Assert.assertTrue(source.contains("\"/hospital/environment\""));
        Assert.assertTrue(source.contains("\"/hospital/emergency\""));
    }
}

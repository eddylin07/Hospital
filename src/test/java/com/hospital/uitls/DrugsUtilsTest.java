package com.hospital.uitls;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class DrugsUtilsTest {

    @Test
    public void emptyDrugSelectionReturnsEmptyString() {
        assertEquals("", DrugsUtils.vaild(Collections.emptyMap()));
    }

    @Test
    public void emptyOptionSelectionReturnsEmptyString() {
        assertEquals("", DrugsUtils.vaild2(Collections.emptyMap()));
    }
}

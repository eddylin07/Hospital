package com.hospital.uitls;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DrugsUtilsTest {

    @Test
    public void vaildBuildsDrugQuantityPairsAndSkipsEmptyQuantities() {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("3_number", "2");
        form.put("5_number", "");
        form.put("8_number", "1");
        form.put("ignored_name", "unused");

        String ids = DrugsUtils.vaild(form);

        assertThat(ids).isEqualTo("3@2,8@1");
    }

    @Test
    public void vaildReturnsEmptyStringWhenNoDrugQuantitiesAreSelected() {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("3_number", "");
        form.put("ignored_name", "unused");

        String ids = DrugsUtils.vaild(form);

        assertThat(ids).isEqualTo("");
    }

    @Test
    public void vaild2BuildsOptionIdListAndHandlesEmptyInput() {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("option_1", "7");
        form.put("other_1", "ignored");
        form.put("option_2", "9");

        assertThat(DrugsUtils.vaild2(form)).isEqualTo("7,9");
        assertThat(DrugsUtils.vaild2(new LinkedHashMap<>())).isEqualTo("");
    }
}

package com.hospital.mapper;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertTrue;

public class MapperSqlSafetyTest {
    @Test
    public void updateDrugsOnlyTouchesLatestSeekForPatient() throws Exception {
        String sql = resource("mapper/SeekMapper.xml").replaceAll("\\s+", " ").toLowerCase();

        assertTrue(sql.contains("update seek set drugs=#{drugs},price=price+#{price} where patientid=#{patientid} order by id desc limit 1"));
    }

    @Test
    public void stockDecrementUsesAtomicAvailabilityGuard() throws Exception {
        String sql = resource("mapper/DrugsMapper.xml").replaceAll("\\s+", " ").toLowerCase();

        assertTrue(sql.contains("update drugs set number=number-#{number} where id=#{id} and number &gt;= #{number}"));
    }

    @Test
    public void appointmentInsertHydratesGeneratedIdForPatientBinding() throws Exception {
        String sql = resource("mapper/AppointmentMapper.xml");

        assertTrue(sql.contains("useGeneratedKeys=\"true\""));
        assertTrue(sql.contains("keyProperty=\"id\""));
        assertTrue(sql.contains("countByDoctorAndPatient"));
    }

    private String resource(String path) throws Exception {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
    }
}

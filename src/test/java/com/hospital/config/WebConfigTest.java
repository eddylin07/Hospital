package com.hospital.config;

import com.hospital.config.intercepors.LoginInterceptor;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.handler.MappedInterceptor;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WebConfigTest {

    @Test
    public void addInterceptorsProtectsAdminRoutesAndExcludesPublicLoginRoutes() {
        WebConfig config = new WebConfig();
        LoginInterceptor loginInterceptor = new LoginInterceptor();
        ReflectionTestUtils.setField(config, "loginInterceptor", loginInterceptor);
        TestInterceptorRegistry registry = new TestInterceptorRegistry();

        config.addInterceptors(registry);

        MappedInterceptor mappedInterceptor = (MappedInterceptor) registry.interceptors().get(0);
        AntPathMatcher pathMatcher = new AntPathMatcher();
        assertSame(loginInterceptor, mappedInterceptor.getInterceptor());
        assertFalse(mappedInterceptor.matches("/login", pathMatcher));
        assertFalse(mappedInterceptor.matches("/hospital/login", pathMatcher));
        assertTrue(mappedInterceptor.matches("/admin/patientManage", pathMatcher));
    }

    private static class TestInterceptorRegistry extends InterceptorRegistry {
        List<Object> interceptors() {
            return super.getInterceptors();
        }
    }
}

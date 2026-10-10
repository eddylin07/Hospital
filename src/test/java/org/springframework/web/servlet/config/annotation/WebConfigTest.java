package org.springframework.web.servlet.config.annotation;

import com.hospital.config.WebConfig;
import com.hospital.config.intercepors.LoginInterceptor;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.handler.MappedInterceptor;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WebConfigTest {

    @Test
    public void loginInterceptorExcludesPublicRoutesAndProtectsAdminRoutes() {
        WebConfig webConfig = new WebConfig();
        ReflectionTestUtils.setField(webConfig, "loginInterceptor", new LoginInterceptor());

        InterceptorRegistry registry = new InterceptorRegistry();
        webConfig.addInterceptors(registry);

        List<Object> interceptors = registry.getInterceptors();
        MappedInterceptor mappedInterceptor = (MappedInterceptor) interceptors.get(0);
        AntPathMatcher pathMatcher = new AntPathMatcher();

        assertFalse(mappedInterceptor.matches("/login", pathMatcher));
        assertFalse(mappedInterceptor.matches("/hospital/introduction", pathMatcher));
        assertFalse(mappedInterceptor.matches("/patient/search", pathMatcher));
        assertTrue(mappedInterceptor.matches("/admin/doctorManage", pathMatcher));
    }
}

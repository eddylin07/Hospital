package com.hospital.config;

import com.hospital.config.intercepors.LoginInterceptor;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.sameInstance;

public class WebConfigTest {

    @Test
    @SuppressWarnings("unchecked")
    public void addInterceptorsExcludesPublicPagesFromLoginRequirement() {
        WebConfig config = new WebConfig();
        LoginInterceptor loginInterceptor = new LoginInterceptor();
        ReflectionTestUtils.setField(config, "loginInterceptor", loginInterceptor);

        InterceptorRegistry registry = new InterceptorRegistry();
        config.addInterceptors(registry);

        List<Object> registrations = (List<Object>) ReflectionTestUtils.getField(registry, "registrations");
        assertThat(registrations, hasSize(1));
        Object registration = registrations.get(0);

        assertThat(ReflectionTestUtils.getField(registration, "interceptor"), sameInstance(loginInterceptor));
        assertThat((List<String>) ReflectionTestUtils.getField(registration, "includePatterns"), contains("/**"));
        assertThat((List<String>) ReflectionTestUtils.getField(registration, "excludePatterns"), containsInAnyOrder(
                "/hospital/login",
                "/hospital",
                "/regest",
                "/patient/search",
                "/hospital/introduction",
                "/hospital/service",
                "/hospital/guide",
                "/hospital/news",
                "/login"
        ));
    }
}

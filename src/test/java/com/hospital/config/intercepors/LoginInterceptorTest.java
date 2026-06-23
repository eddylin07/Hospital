package com.hospital.config.intercepors;

import com.hospital.config.WebConfig;
import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {

    @Test
    public void preHandleRedirectsAnonymousUsersToLoginPage() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void preHandleAllowsRequestsWithLoginSession() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", new Login());

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertTrue(allowed);
        assertNull(response.getRedirectedUrl());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void addInterceptorsProtectsAllRoutesButKeepsPublicRoutesOpen() {
        LoginInterceptor interceptor = new LoginInterceptor();
        WebConfig config = new WebConfig();
        ReflectionTestUtils.setField(config, "loginInterceptor", interceptor);
        InterceptorRegistry registry = new InterceptorRegistry();

        config.addInterceptors(registry);

        List<Object> registrations = (List<Object>) ReflectionTestUtils.getField(registry, "registrations");
        assertEquals(1, registrations.size());

        Object registration = registrations.get(0);
        assertSame(interceptor, ReflectionTestUtils.getField(registration, "interceptor"));

        List<String> includePatterns = (List<String>) ReflectionTestUtils.getField(registration, "includePatterns");
        assertEquals(1, includePatterns.size());
        assertEquals("/**", includePatterns.get(0));

        List<String> excludePatterns = (List<String>) ReflectionTestUtils.getField(registration, "excludePatterns");
        assertTrue(excludePatterns.contains("/hospital/login"));
        assertTrue(excludePatterns.contains("/hospital"));
        assertTrue(excludePatterns.contains("/regest"));
        assertTrue(excludePatterns.contains("/patient/search"));
        assertTrue(excludePatterns.contains("/hospital/introduction"));
        assertTrue(excludePatterns.contains("/hospital/service"));
        assertTrue(excludePatterns.contains("/hospital/guide"));
        assertTrue(excludePatterns.contains("/hospital/news"));
        assertTrue(excludePatterns.contains("/login"));
    }
}

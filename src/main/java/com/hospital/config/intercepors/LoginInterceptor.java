package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final int ROLE_ADMIN = 1;
    private static final int ROLE_DOCTOR = 2;
    private static final int ROLE_PATIENT = 3;
 
    //这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        //这里的User是登陆时放入session的
        Login login = session == null ? null : (Login) session.getAttribute("login");
        //如果session中没有user，表示没登陆
        if (!isAuthenticated(login)){
            //这个方法返回false表示忽略当前请求，如果一个用户调用了需要登陆才能使用的接口，如果他没有登陆这里会直接忽略掉
            //当然你可以利用response给用户返回一些提示信息，告诉他没登陆
            response.sendRedirect("/hospital/login");
            return false;
        }

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && !contextPath.isEmpty() && uri.startsWith(contextPath)) {
            uri = uri.substring(contextPath.length());
        }

        if (!isAuthorized(uri, login.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;    //如果session里有login，表示该用户已经登陆，放行，用户即可继续调用自己需要的接口
    }

    private boolean isAuthenticated(Login login) {
        return login != null && login.getId() != null && login.getRole() != null;
    }

    private boolean isAuthorized(String uri, Integer role) {
        if (uri.startsWith("/admin/") || uri.startsWith("/hospital/admin/")) {
            return ROLE_ADMIN == role;
        }
        if (isDoctorWorkflow(uri) || uri.startsWith("/hospital/doctor/")) {
            return ROLE_DOCTOR == role;
        }
        if (uri.startsWith("/patient/") || uri.startsWith("/hospital/patient/")) {
            return ROLE_PATIENT == role;
        }
        return true;
    }

    private boolean isDoctorWorkflow(String uri) {
        return uri.startsWith("/doctor/seekMedicalAdvice")
                || uri.startsWith("/doctor/seek/")
                || uri.startsWith("/doctor/drug")
                || uri.startsWith("/doctor/zation")
                || uri.startsWith("/doctor/medicalhistory/")
                || uri.startsWith("/doctor/seekinfo")
                || uri.startsWith("/doctor/printseek/");
    }
 
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }
 
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
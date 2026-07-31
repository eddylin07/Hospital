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
    private static final int ADMIN_ROLE = 1;
    private static final int DOCTOR_ROLE = 2;
    private static final int PATIENT_ROLE = 3;
 
    //这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Login login = (Login) session.getAttribute("login");
        if (login == null||login.getId()==null||login.getRole()==null){
            response.sendRedirect("/hospital/login");
            return false;
        }
        if (!hasRoleForPath(request, login.getRole())){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }

    private boolean hasRoleForPath(HttpServletRequest request, Integer role) {
        String uri = request.getRequestURI();
        if (uri == null) {
            return false;
        }
        if (uri.startsWith("/admin/")||uri.equals("/hospital/admin/index")) {
            return ADMIN_ROLE == role;
        }
        if (uri.startsWith("/patient/")||uri.equals("/hospital/patient/index")) {
            return PATIENT_ROLE == role;
        }
        if (uri.startsWith("/doctor/")||uri.equals("/hospital/doctor/index")) {
            return isDoctorLookup(request, uri)||DOCTOR_ROLE == role;
        }
        return true;
    }

    private boolean isDoctorLookup(HttpServletRequest request, String uri) {
        if (!"GET".equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        String prefix = "/doctor/";
        if (!uri.startsWith(prefix)) {
            return false;
        }
        String suffix = uri.substring(prefix.length());
        if (suffix.indexOf('/') >= 0) {
            return false;
        }
        return !"seekMedicalAdvice".equals(suffix)
                && !"drug".equals(suffix)
                && !"zation".equals(suffix)
                && !"seekinfo".equals(suffix)
                && !"printseek".equals(suffix)
                && !"seek".equals(suffix)
                && !"medicalhistory".equals(suffix);
    }
 
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }
 
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
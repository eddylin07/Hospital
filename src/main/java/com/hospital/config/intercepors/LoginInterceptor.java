package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
 
@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final Integer ADMIN_ROLE = 1;
    private static final Integer DOCTOR_ROLE = 2;
    private static final Integer PATIENT_ROLE = 3;
    private static final Set<String> DOCTOR_WORKFLOW_SEGMENTS = new HashSet<>(Arrays.asList(
            "seekMedicalAdvice", "seek", "drug", "zation", "medicalhistory", "seekinfo", "printseek"
    ));
 
    //这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //每一个项目对于登陆的实现逻辑都有所区别，我这里使用最简单的Session提取User来验证登陆。
        HttpSession session = request.getSession();
        //这里的User是登陆时放入session的
        Login login = (Login) session.getAttribute("login");
        //如果session中没有user，表示没登陆
        if (login == null){
            //这个方法返回false表示忽略当前请求，如果一个用户调用了需要登陆才能使用的接口，如果他没有登陆这里会直接忽略掉
            //当然你可以利用response给用户返回一些提示信息，告诉他没登陆
            response.sendRedirect("/hospital/login");
            return false;
        }
        if (login.getId()==null||login.getRole()==null){
            session.removeAttribute("login");
            response.sendRedirect("/hospital/login");
            return false;
        }
        if (!isAuthorized(request, login.getRole())){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;    //如果session里有有效login，且角色匹配，放行
    }

    private boolean isAuthorized(HttpServletRequest request, Integer role) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && !contextPath.equals("") && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }
        if (path.equals("/admin") || path.startsWith("/admin/") || path.startsWith("/hospital/admin/")) {
            return ADMIN_ROLE.equals(role);
        }
        if (path.equals("/patient") || path.startsWith("/patient/") || path.startsWith("/hospital/patient/")) {
            return PATIENT_ROLE.equals(role);
        }
        if (path.equals("/doctor") || path.startsWith("/doctor/") || path.startsWith("/hospital/doctor/")) {
            if (isDepartmentLookup(request.getMethod(), path)) {
                return true;
            }
            return DOCTOR_ROLE.equals(role);
        }
        return true;
    }

    private boolean isDepartmentLookup(String method, String path) {
        if (!"GET".equalsIgnoreCase(method) || !path.startsWith("/doctor/")) {
            return false;
        }
        String segment = path.substring("/doctor/".length());
        return !segment.equals("") && !segment.contains("/") && !DOCTOR_WORKFLOW_SEGMENTS.contains(segment);
    }
 
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }
 
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
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
 
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Login login = (Login) session.getAttribute("login");
        if (login == null||login.getId()==null||login.getRole()==null){
            response.sendRedirect("/hospital/login");
            return false;
        }
        if(!isAllowed(request,login.getRole())){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }

    private boolean isAllowed(HttpServletRequest request,Integer role){
        String path=request.getRequestURI();
        String contextPath=request.getContextPath();
        if(contextPath!=null&&!contextPath.equals("")&&path.startsWith(contextPath)){
            path=path.substring(contextPath.length());
        }
        if(path.startsWith("/admin/")||path.equals("/admin")){
            return role==ROLE_ADMIN;
        }
        if(path.startsWith("/patient/")||path.equals("/patient")){
            return role==ROLE_PATIENT;
        }
        if(path.startsWith("/hospital/admin/")){
            return role==ROLE_ADMIN;
        }
        if(path.startsWith("/hospital/patient/")){
            return role==ROLE_PATIENT;
        }
        if(path.startsWith("/hospital/doctor/")){
            return role==ROLE_DOCTOR;
        }
        if(isDoctorWorkflowPath(path)){
            return role==ROLE_DOCTOR;
        }
        return true;
    }

    private boolean isDoctorWorkflowPath(String path){
        return path.equals("/doctor/seekMedicalAdvice")
                ||path.startsWith("/doctor/seek/")
                ||path.equals("/doctor/drug")
                ||path.equals("/doctor/zation")
                ||path.startsWith("/doctor/medicalhistory/")
                ||path.equals("/doctor/seekinfo")
                ||path.startsWith("/doctor/printseek/");
    }
 
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }
 
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
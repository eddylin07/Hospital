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
    private static final int ROLE_ADMIN = 1;
    private static final int ROLE_DOCTOR = 2;
    private static final int ROLE_PATIENT = 3;
    private static final Set<String> DOCTOR_WORKFLOW_PATHS = new HashSet<>(Arrays.asList(
            "seekMedicalAdvice",
            "seek",
            "drug",
            "zation",
            "medicalhistory",
            "seekinfo",
            "printseek"
    ));
 
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Login login = (Login) session.getAttribute("login");
        if (login == null||login.getId()==null||login.getRole()==null){
            response.sendRedirect("/hospital/login");
            return false;
        }
        if(!isAuthorized(request,login.getRole())){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }

    private boolean isAuthorized(HttpServletRequest request,Integer role){
        String path=request.getRequestURI();
        if(path.startsWith("/admin/")){
            return role==ROLE_ADMIN;
        }
        if(path.startsWith("/patient/")){
            return role==ROLE_PATIENT;
        }
        if(isDoctorWorkflowPath(path)){
            return role==ROLE_DOCTOR;
        }
        return true;
    }

    private boolean isDoctorWorkflowPath(String path){
        if(!path.startsWith("/doctor/")){
            return false;
        }
        String remaining=path.substring("/doctor/".length());
        int slash=remaining.indexOf('/');
        String firstSegment=slash>=0?remaining.substring(0,slash):remaining;
        return DOCTOR_WORKFLOW_PATHS.contains(firstSegment);
    }
 
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }
 
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
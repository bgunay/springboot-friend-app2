package org.pinsoft.interview.web.interceptors;

import org.pinsoft.interview.service.LoggerService;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LogsInterceptor implements HandlerInterceptor {
   private final LoggerService loggerService;

    public LogsInterceptor(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader != null){
            String method = request.getMethod();

            if((method.equals("POST")|| method.equals("PUT") || method.equals("DELETE")) && !request.getRequestURI().endsWith("error")){
                String tableName = request.getRequestURI().split("/")[1];
                String action = request.getRequestURI().split("/")[2];
                String principal = request.getUserPrincipal().getName();

                loggerService.createLog(method, principal, tableName, action);
            }
        }

        return true;
    }
}

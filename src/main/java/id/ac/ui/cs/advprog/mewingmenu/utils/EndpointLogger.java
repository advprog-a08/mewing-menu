package id.ac.ui.cs.advprog.mewingmenu.utils;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class EndpointLogger implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        String logId = Long.toString(System.currentTimeMillis());
        request.setAttribute("logId", logId); // optional, for later use

        System.out.printf("[%s] %s %s%n", LocalDateTime.now(), request.getMethod(), request.getRequestURI());

        return true;
    }
}

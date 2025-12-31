package com.baluga.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> timeWindows = new ConcurrentHashMap<>();
    
    private static final int MAX_REQUESTS_PER_SECOND = 10;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();
        
        timeWindows.putIfAbsent(ip, currentTime);
        requestCounts.putIfAbsent(ip, new AtomicInteger(0));
        
        if (currentTime - timeWindows.get(ip) > 1000) {
            timeWindows.put(ip, currentTime);
            requestCounts.get(ip).set(0);
        }
        
        if (requestCounts.get(ip).incrementAndGet() > MAX_REQUESTS_PER_SECOND) {
            response.setStatus(429); // Too Many Requests
            response.getWriter().write("Rate limit exceeded. Please try again later.");
            return false;
        }
        
        return true;
    }
}

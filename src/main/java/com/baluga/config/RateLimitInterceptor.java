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
    private static final long WINDOW_NANOS = 1_000_000_000L;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        long now = System.nanoTime();

        long windowStart = timeWindows.computeIfAbsent(ip, k -> now);
        AtomicInteger counter = requestCounts.computeIfAbsent(ip, k -> new AtomicInteger(0));

        if (now < windowStart || now - windowStart >= WINDOW_NANOS) {
            timeWindows.put(ip, now);
            counter.set(0);
        }

        if (counter.incrementAndGet() > MAX_REQUESTS_PER_SECOND) {
            response.setStatus(429);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("Rate limit exceeded. Please try again later.");
            return false;
        }
        
        return true;
    }
}

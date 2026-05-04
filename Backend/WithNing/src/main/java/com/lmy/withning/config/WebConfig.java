package com.lmy.withning.config;

import com.lmy.withning.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                    "/api/auth/login",
                    "/api/auth/register",
                    "/api/auth/userinfo",
                    "/api/tasks/daily",
                    "/api/tasks/stage",
                    "/api/tasks/{taskId}",
                    "/api/points",
                    "/api/points/records",
                    "/api/mall/products",
                    "/api/mall/products/{productId}",
                    "/api/admin/check"
                );
    }
}

package com.xxw.shop.module.security.config;

import cn.hutool.core.util.ArrayUtil;
import com.xxw.shop.module.security.adapter.AuthConfigAdapter;
import com.xxw.shop.module.security.adapter.DefaultAuthConfigAdapter;
import com.xxw.shop.module.security.filter.AuthFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 授权配置
 */
@Configuration
public class AuthConfig {

    @Bean
    @ConditionalOnMissingBean
    public AuthConfigAdapter authConfigAdapter() {
        return new DefaultAuthConfigAdapter();
    }

    @Bean
    @Lazy
    public FilterRegistrationBean<AuthFilter> filterRegistration(AuthConfigAdapter authConfigAdapter, AuthFilter authFilter) {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
        // 添加过滤器
        registration.setFilter(authFilter);
        // 设置过滤路径，/*所有路径
        registration.addUrlPatterns(ArrayUtil.toArray(authConfigAdapter.pathPatterns(), String.class));
        registration.setName("authFilter");
        // 设置优先级
        registration.setOrder(0);
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        return registration;
    }

}

package com.ntschy.underground.config;

import com.ntschy.underground.handler.TokenInterceptor;
import com.ntschy.underground.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private WhiteList whiteList;

    private final AuthorityService authorityService;

    public WebConfig(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(authorityService))
                .addPathPatterns("/**")
                .excludePathPatterns(whiteList.getUrlList());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}

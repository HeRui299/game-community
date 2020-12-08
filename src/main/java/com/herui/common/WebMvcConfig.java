package com.herui.common;

import com.herui.controller.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/toLogin").setViewName("login");
        registry.addViewController("/deck.html").setViewName("deck");
        registry.addViewController("/strategy.html").setViewName("strategy");
        registry.addViewController("/official.html").setViewName("official");
        registry.addViewController("/photo.html").setViewName("photo");
        registry.addViewController("/article.html").setViewName("article");
        registry.addViewController("/register.html").setViewName("register");
        registry.addViewController("/header.html").setViewName("header");
        registry.addViewController("/footer.html").setViewName("footer");
    }
}
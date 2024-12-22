package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.annotation.LoginInterceptor;


/**
 * 末吉
 * 自作アノテーションのためのconfigクラス
 */
@Configuration
public class AnnoConfig implements WebMvcConfigurer {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/taskdon/user/login"); // 除外するパスを指定
    }
}

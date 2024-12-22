package com.example.demo.annotation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * 末吉
 * 自作アノテーション
 * ログイン未済のユーザにURLでのアクセスをさせない
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	HttpSession session;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			LoginRequired annotation = handlerMethod.getMethodAnnotation(LoginRequired.class);
			if (annotation != null) {
				HttpSession session = request.getSession();
				
				//セッション情報がnullだったらログイン画面に遷移
				if (session.getAttribute("user") == null) {
					response.sendRedirect("/taskdon/user/login");
					return false;
				}
			}
		}
		
		return true;
	}
}

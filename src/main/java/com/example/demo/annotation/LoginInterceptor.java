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
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			LoginRequired annotation = handlerMethod.getMethodAnnotation(LoginRequired.class);
			//アノテーションを置いていないときは実行されないように
			if (annotation != null) {
				HttpSession session = request.getSession();

				//セッション情報がnullだったらログイン画面に遷移
				if (session.getAttribute("user") == null) {
					response.sendRedirect("/taskdon/user/login");
					return false;
				}

//				// ユーザーの権限をチェックする
//				User user = (User) session.getAttribute("user");
//				if (user.getUser_id().startsWith("ad") || user.getUser_id().startsWith("te")) {
//					// 権限が管理者の場合、管理者のメニュー画面に遷移する
//					response.sendRedirect("/taskdon/admin/menu");
//				} else {
//					// 権限がユーザの場合、ユーザのメニュー画面に遷移する
//					response.sendRedirect("/taskdon/user/menu");
//					return false;
//				}
			}
		}

		return true;
	}
}

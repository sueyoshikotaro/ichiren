package com.example.demo.controll;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlerController {

	// 例外処理
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView handleException(Exception e) {
    	return new ModelAndView("redirect:/taskdon/admin/menu");
    }
}

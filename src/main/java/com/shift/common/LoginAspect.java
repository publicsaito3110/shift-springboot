package com.shift.common;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.model.bean.AccountBean;

/**
 * @author saito
 *
 */
@Aspect
@Component
public class LoginAspect {

	@Autowired
	private HttpSession httpSession;


	@Around("execution(* *..*Controller.*(..))")
	public Object executeSession(ProceedingJoinPoint joinPoint) throws Throwable {

		//Sessionを取得
		AccountBean accountBean = (AccountBean)httpSession.getAttribute(Const.SESSION_KEYWORD_ACCOUNT_BEAN);

		//現在のURIを取得
		HttpServletRequest requset = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String nowUri = requset.getRequestURI();

		//Sessionが存在しないとき
		if (accountBean == null) {

			//Sessionの未保持を許容するURI
			String[] sessionIgnoreUriArray = {"/login", "/login/auth", "/login/error", "/logout"};

			//sessionIgnoreUriArrayに含まれていないとき
			if (!Arrays.asList(sessionIgnoreUriArray).contains(nowUri)) {

				//ログイン画面へ強制的に遷移
				ModelAndView modelAndView = new ModelAndView();
				modelAndView.setViewName("redirect:/login/error");
				return modelAndView;
			}

			Object returnObject = joinPoint.proceed();
			return returnObject;
		}

		//ログインに関するURI
		String[] loginUriArray = {"/login", "/login/auth", "/login/error"};

		//Sessionが存在しているかつログインに関するURIにアクセスしたとき
		if (accountBean != null && Arrays.asList(loginUriArray).contains(nowUri)) {

			//ホーム画面へ強制的に遷移
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("redirect:/home");
			return modelAndView;
		}

		Object returnObject = joinPoint.proceed();
		return returnObject;
	}
}

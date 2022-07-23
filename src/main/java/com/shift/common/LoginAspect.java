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
	HttpSession httpSession;


	@Around("execution(* *..*Controller.*(..))")
	public Object executeSession(ProceedingJoinPoint joinPoint) throws Throwable {

		//Sessionを取得
		AccountBean accountBean = (AccountBean)httpSession.getAttribute(Const.SESSION_KEYWORD_ACCOUNT_BEAN);

		//Sessionが存在しないとき
		if (accountBean == null) {

			String[] ignoreUriArray = {"/login", "/login/auth", "/login/error", "/logout"};

			//現在のURIを取得
			HttpServletRequest requset = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			String uri = requset.getRequestURI();

			//Sessionの未保持を許容しないURIでないとき
			if (!Arrays.asList(ignoreUriArray).contains(uri)) {
				ModelAndView mv = new ModelAndView();
				mv.setViewName("redirect:/login/error");
				return mv;
			}
		}

		Object returnObject = joinPoint.proceed();
		return returnObject;
	}
}

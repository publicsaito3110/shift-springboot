package com.shift.common;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
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


	/**
     * セッション判定処理 (AOP)
     *
     * <p>Controller実行前にセッション及びURI制限を行う<br>
     * セッションが未保持のとき、<br>
     * ・セッションの未保持が許容されていないURI: ログイン画面へ強制的に遷移<br>
     * ・セッションの未保持が許容されているURI: そのまま実行<br>
     * ・存在しないURI: 404ページへ遷移
     * セッションが保持されているとき、<br>
     * ・ログイン関連のURI: 強制的にホーム画面へ遷移<br>
     * ・ログイン関連以外のURI: そのまま実行
     * </p>
     *
     * @param joinPoint ProceedingJoinPoint
     * @return Object
     */
	@Around("execution(* *..*Controller.*(..))")
	public Object executeSession(ProceedingJoinPoint joinPoint) throws Throwable {

		//リクエストを受け取ったControllerを取得
		Object controllerObj = joinPoint.getTarget();

		//エラーハンドリングするControllerのとき
		if (controllerObj instanceof BasicErrorController) {

			//Controllerの処理を実行し、オブジェクトを返す
			Object returnObject = joinPoint.proceed();
			return returnObject;
		}

		//Sessionを取得
		AccountBean accountBean = (AccountBean)httpSession.getAttribute(Const.SESSION_KEYWORD_ACCOUNT_BEAN);

		//現在のURIを取得
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String nowUri = request.getRequestURI();

		//Sessionが存在しないとき
		if (accountBean == null) {

			//Sessionの未保持を許容するURI
			String[] sessionIgnoreUriArray = {"/", "/login", "/login/auth", "/login/forgot-password", "/login/forgot-password/send", "/login/forgot-password/reset", "/login/forgot-password/reset/auth", "/login/forgot-password/reset/modify", "/login/forgot-id", "/login/forgot-id/send", "/login/error", "/logout"};

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
		String[] loginUriArray = {"/login", "/login/auth", "/login/forgot-password", "/login/forgot-password/send", "/login/forgot-password/reset", "/login/forgot-password/reset/auth", "/login/forgot-password/reset/modify", "/login/forgot-id", "/login/forgot-id/send", "/login/error"};

		//ログインに関するURIにアクセスかつSessionが存在しているとき
		if (Arrays.asList(loginUriArray).contains(nowUri) && accountBean != null) {

			//ホーム画面へ強制的に遷移
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("redirect:/home");
			return modelAndView;
		}

		//Controllerの処理を実行し、オブジェクトを返す
		Object returnObject = joinPoint.proceed();
		return returnObject;
	}
}

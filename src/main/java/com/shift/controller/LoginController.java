package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.model.bean.LoginAuthBean;
import com.shift.domain.model.bean.LoginForgotIdSendBean;
import com.shift.domain.service.LoginService;

/**
 * @author saito
 *
 */
@Controller
public class LoginController extends BaseController {

	@Autowired
	private LoginService loginService;


	/**
	 * ログイン画面<br>
	 * [Controller] (/login)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/login")
	public ModelAndView login(Authentication authentication, ModelAndView modelAndView) {

		modelAndView.addObject("isAlertLoginFailed", false);
		modelAndView.setViewName("login");
		return modelAndView;
	}


	/**
	 * ログイン事後処理機能<br>
	 * [Controller] (/login/auth)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/login/auth")
	public ModelAndView loginAuth(Authentication authentication, ModelAndView modelAndView) {

		//--------------------------
		// ログイン認証成功時に実行
		//--------------------------
		//Service
		LoginAuthBean loginAuthBean = loginService.loginAuth(authentication.getName());

		//ログイン可能ユーザーでなかったとき
		if (!loginAuthBean.isLogin()) {

			//ログイン画面へ戻す
			modelAndView.addObject("isAlertLoginFailed", true);
			modelAndView.addObject("errorMassage", loginAuthBean.getErrorMassage());

			modelAndView.setViewName("/login");
			return modelAndView;
		}

		modelAndView.setViewName("redirect:/home");
		return modelAndView;
	}


	/**
	 * ログインID取得画面<br>
	 * [Controller] (/login/forgot-id)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/login/forgot-id")
	public ModelAndView loginForgotId(Authentication authentication, ModelAndView modelAndView) {

		modelAndView.addObject("isAlertSuccess", false);
		modelAndView.addObject("isAlertFailed", false);
		modelAndView.setViewName("login-forgot-id");
		return modelAndView;
	}


	/**
	 * メール判定及びログインID送信機能<br>
	 * [Controller] (/login/forgot-id/send)
	 *
	 * @param email RequestParameter
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/login/forgot-id/send", method = RequestMethod.POST)
	public ModelAndView loginForgotIdSend(@RequestParam(value="email") String email, Authentication authentication, ModelAndView modelAndView) {

		//service
		LoginForgotIdSendBean loginForgotIdSendBean = loginService.loginForgotIdSend(email);
		if (loginForgotIdSendBean.isSuccessSendEmail()) {
			modelAndView.addObject("isAlertSuccess", true);
			modelAndView.addObject("isAlertFailed", false);
		} else {
			modelAndView.addObject("isAlertSuccess", false);
			modelAndView.addObject("isAlertFailed", true);
		}
		modelAndView.setViewName("login-forgot-id");
		return modelAndView;
	}


	/**
	 * セッションエラー画面<br>
	 * [Controller] (/login/error)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/login/error")
	public ModelAndView loginError(Authentication authentication, ModelAndView modelAndView) {

		modelAndView.addObject("isAlertLoginFailed", true);
		modelAndView.addObject("errorMassage", "セッションの有効期限が切れました。もう一度ログインしてください。");
		modelAndView.setViewName("login");
		return modelAndView;
	}
}

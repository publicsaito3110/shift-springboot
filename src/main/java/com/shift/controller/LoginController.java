package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.model.bean.LoginAuthBean;
import com.shift.domain.service.LoginService;

/**
 * @author saito
 *
 */
@Controller
public class LoginController extends BaseController {

	@Autowired
	private LoginService loginService;


	@RequestMapping("/login")
	public ModelAndView login(ModelAndView modelAndView) {

		modelAndView.addObject("isAlertLoginFailed", false);
		modelAndView.setViewName("login");
		return modelAndView;
	}


	@RequestMapping("/login/auth")
	public ModelAndView loginAuth(Authentication authentication, ModelAndView modelAndView) {

		//--------------------------
		// ログイン認証成功時に実行
		//--------------------------
		LoginAuthBean loginAuthBean = this.loginService.loginAuth(authentication.getName());

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


	@RequestMapping("/login/error")
	public ModelAndView loginError(ModelAndView modelAndView) {

		modelAndView.addObject("isAlertLoginFailed", true);
		modelAndView.addObject("errorMassage", "セッションの有効期限が切れました。もう一度ログインしてください。");
		modelAndView.setViewName("login");
		return modelAndView;
	}
}

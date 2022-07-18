package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.model.bean.LoginBean;
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


	@RequestMapping(value = "/login/auth", method = RequestMethod.POST)
	public ModelAndView loginAuth(@RequestParam(value="userId") String userId, @RequestParam(value="password") String password, ModelAndView modelAndView) {

		LoginBean loginBean = this.loginService.loginAuth(userId, password);

		//ログインができなかったときログイン画面へ戻す
		if (!loginBean.isLogin()) {

			modelAndView.addObject("isAlertLoginFailed", true);
			modelAndView.addObject("errorMassage", loginBean.getErrorMassage());

			modelAndView.setViewName("login");
			return modelAndView;
		}

		modelAndView.setViewName("redirect:/home");
		return modelAndView;
	}
}

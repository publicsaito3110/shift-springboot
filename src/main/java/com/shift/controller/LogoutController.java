package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.service.LogoutService;

/**
 * @author saito
 *
 */
@Controller
public class LogoutController extends BaseController {

	@Autowired
	private LogoutService logoutService;


	/**
	 * ログアウト機能<br>
	 * [Controller] (/logout)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/logout")
	public ModelAndView login(Authentication authentication, ModelAndView modelAndView) {

		//Service
		logoutService.logout();

		modelAndView.setViewName("redirect:/login");
		return modelAndView;
	}
}

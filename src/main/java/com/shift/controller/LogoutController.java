package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
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


	@RequestMapping("/logout")
	public ModelAndView login(ModelAndView modelAndView) {

		this.logoutService.logout();
		modelAndView.addObject("isAlertLoginFailed", false);

		modelAndView.setViewName("login");
		return modelAndView;
	}
}

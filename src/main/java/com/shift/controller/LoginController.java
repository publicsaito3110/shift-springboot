package com.shift.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author saito
 *
 */
@Controller
public class LoginController extends BaseController {

	@Autowired
	private HttpSession httpSession;


	@RequestMapping("/login")
	public ModelAndView login(ModelAndView modelAndView) {

		modelAndView.setViewName("login");
		return modelAndView;
	}
}

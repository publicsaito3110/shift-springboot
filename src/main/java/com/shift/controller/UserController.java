package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.service.UserService;

/**
 * @author saito
 *
 */
@Controller
public class UserController extends BaseController {

	@Autowired
	private UserService userService;


	@RequestMapping("/user")
	public ModelAndView login(ModelAndView modelAndView) {

		this.userService.logout();
		modelAndView.setViewName("redirect:/login");
		return modelAndView;
	}
}

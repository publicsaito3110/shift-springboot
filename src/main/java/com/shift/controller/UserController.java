package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.model.bean.UserBean;
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
	public ModelAndView login(@RequestParam(value="p",required=false) String page, @RequestParam(value="keyword",required=false) String keyword, ModelAndView modelAndView) {

		UserBean userBean =this.userService.user(page, keyword);
		modelAndView.addObject("userList", userBean.getUserList());

		modelAndView.setViewName("user");
		return modelAndView;
	}
}

package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.model.bean.HomeBean;
import com.shift.domain.service.HomeService;

/**
 * @author saito
 *
 */
@Controller
public class HomeController extends BaseController {

	@Autowired
	private HomeService homeService;


	/**
	 * ホーム表示機能<br>
	 * [Controller] (/dm/home)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/home")
	public ModelAndView home(Authentication authentication, ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();

		//Service
		HomeBean homeBean = homeService.home(loginUser);
		modelAndView.addObject("userEntity", homeBean.getUserEntity());
		modelAndView.addObject("newsList", homeBean.getNewsList());

		modelAndView.setViewName("home");
		return modelAndView;
	}
}

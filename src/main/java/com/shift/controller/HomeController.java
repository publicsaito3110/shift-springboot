package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.common.Const;
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
	 * @param ym RequestParameter 年月ym
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/home")
	public ModelAndView home(@RequestParam(value="ymd",required=false) String ymd, Authentication authentication, ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();

		//Service
		HomeBean homeBean = homeService.home(ymd, loginUser);
		modelAndView.addObject("beforeWeekYmd", homeBean.getBeforeWeekYmd());
		modelAndView.addObject("afterWeekYmd", homeBean.getAfterWeekYmd());
		modelAndView.addObject("userEntity", homeBean.getUserEntity());
		modelAndView.addObject("newsList", homeBean.getNewsList());
		modelAndView.addObject("dayScheduleList", homeBean.getDayScheduleList());
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
		//View
		modelAndView.setViewName("home");
		return modelAndView;
	}
}

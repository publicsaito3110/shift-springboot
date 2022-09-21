package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.common.Const;
import com.shift.domain.model.bean.ScheduleDecisionBean;
import com.shift.domain.service.ScheduleDecisionService;

/**
 * @author saito
 *
 */
@Controller
public class ScheduleDecisionController extends BaseController {

	@Autowired
	private ScheduleDecisionService scheduleDecisionService;


	/**
	 * スケジュール確定画面<br>
	 * [Controller] (/schedule)
	 *
	 * @param ym RequestParameter(required=false)
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/schedule-decision")
	public ModelAndView schedule(@RequestParam(value="ym",required=false) String ym, Authentication authentication, ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();

		//Service
		ScheduleDecisionBean scheduleDecisionBean = scheduleDecisionService.scheduleDecision(ym, loginUser);
		modelAndView.addObject("year", scheduleDecisionBean.getYear());
		modelAndView.addObject("month", scheduleDecisionBean.getMonth());
		modelAndView.addObject("calendarList", scheduleDecisionBean.getCalendarList());
		modelAndView.addObject("afterYm", scheduleDecisionBean.getAfterYm());
		modelAndView.addObject("beforeYm", scheduleDecisionBean.getBeforeYm());
		modelAndView.addObject("scheduleTimeList", scheduleDecisionBean.getScheduleTimeList());
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);

		modelAndView.setViewName("schedule-decision");
		return modelAndView;
	}
}

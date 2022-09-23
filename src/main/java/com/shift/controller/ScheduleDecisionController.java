package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.common.Const;
import com.shift.domain.model.bean.ScheduleDecisionBean;
import com.shift.domain.model.bean.ScheduleDecisionModifyBean;
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
	 * 確定スケジュール画面<br>
	 * [Controller] (/schedule-decision)
	 *
	 * @param ym RequestParameter(required=false)
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/schedule-decision")
	public ModelAndView scheduleDecision(@RequestParam(value="ym",required=false) String ym, Authentication authentication, ModelAndView modelAndView) {

		//Service
		ScheduleDecisionBean scheduleDecisionBean = scheduleDecisionService.scheduleDecision(ym);
		modelAndView.addObject("year", scheduleDecisionBean.getYear());
		modelAndView.addObject("month", scheduleDecisionBean.getMonth());
		modelAndView.addObject("calendarList", scheduleDecisionBean.getCalendarList());
		modelAndView.addObject("nowYm", scheduleDecisionBean.getNowYm());
		modelAndView.addObject("afterYm", scheduleDecisionBean.getAfterYm());
		modelAndView.addObject("beforeYm", scheduleDecisionBean.getBeforeYm());
		modelAndView.addObject("scheduleTimeList", scheduleDecisionBean.getScheduleTimeList());
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);

		modelAndView.setViewName("schedule-decision");
		return modelAndView;
	}


	/**
	 * 確定スケジュール修正画面<br>
	 * [Controller] (/schedule-decision/modify)
	 *
	 * @param ym RequestParameter
	 * @param day RequestParameter
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/schedule-decision/modify", method = RequestMethod.POST)
	public ModelAndView scheduleDecisionModify(@RequestParam(value="ym") String ym, @RequestParam(value="day") String day, Authentication authentication, ModelAndView modelAndView) {

		//Service
		ScheduleDecisionModifyBean scheduleDecisionModifyBean = scheduleDecisionService.scheduleDecisionModify(ym, day);
		modelAndView.addObject("year", scheduleDecisionModifyBean.getYear());
		modelAndView.addObject("month", scheduleDecisionModifyBean.getMonth());
		modelAndView.addObject("day", scheduleDecisionModifyBean.getDay());
		modelAndView.addObject("schedulePreUserList", scheduleDecisionModifyBean.getSchedulePreUserList());
		modelAndView.addObject("scheduleUserList", scheduleDecisionModifyBean.getScheduleUserList());
		modelAndView.addObject("scheduleTimeList", scheduleDecisionModifyBean.getScheduleTimeList());
		modelAndView.addObject("scheduleTimeHtmlClassColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY);
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);

		modelAndView.setViewName("schedule-decision-modify");
		return modelAndView;
	}
}

package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.common.Const;
import com.shift.domain.model.bean.CalendarAllBean;
import com.shift.domain.model.bean.CalendarBean;
import com.shift.domain.service.CalendarService;

/**
 * @author saito
 *
 */
@Controller
public class CalendarController extends BaseController {

	@Autowired
	private CalendarService calendarService;


	/**
	 * ログインユーザの確定スケジュール表示機能<br>
	 * [Controller] (/calendar)
	 *
	 * @param ym RequestParameter(required=false)
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/calendar")
	public ModelAndView calendar(@RequestParam(value="ym",required=false) String ym, Authentication authentication, ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();

		//Service
		CalendarBean calendarBean = calendarService.calendar(ym, loginUser);
		modelAndView.addObject("year", calendarBean.getYear());
		modelAndView.addObject("month", calendarBean.getMonth());
		modelAndView.addObject("nowYm", calendarBean.getNowYm());
		modelAndView.addObject("calendarList", calendarBean.getCalendarList());
		modelAndView.addObject("isScheduleDisplayArray", calendarBean.getIsScheduleDisplayArray());
		modelAndView.addObject("afterYm", calendarBean.getAfterYm());
		modelAndView.addObject("beforeYm", calendarBean.getBeforeYm());
		modelAndView.addObject("scheduleTimeEntity", calendarBean.getScheduleTimeEntity());
		modelAndView.addObject("scheduleTimeHtmlClassColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY);
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);

		modelAndView.setViewName("calendar");
		return modelAndView;
	}


	/**
	 * 全てのユーザの確定スケジュール表示機能<br>
	 * [Controller] (/calendar/all)
	 *
	 * @param ym RequestParameter(required=false)
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/calendar/all")
	public ModelAndView calendarAll(@RequestParam(value="ym",required=false) String ym, Authentication authentication, ModelAndView modelAndView) {

		//Service
		CalendarAllBean calendarAllBean = calendarService.calendarAll(ym);
		modelAndView.addObject("year", calendarAllBean.getYear());
		modelAndView.addObject("month", calendarAllBean.getMonth());
		modelAndView.addObject("calendarList", calendarAllBean.getCalendarList());
		modelAndView.addObject("userScheduleAllArray", calendarAllBean.getUserScheduleAllArray());
		modelAndView.addObject("afterYm", calendarAllBean.getAfterYm());
		modelAndView.addObject("beforeYm", calendarAllBean.getBeforeYm());
		modelAndView.addObject("scheduleTimeEntity", calendarAllBean.getScheduleTimeEntity());
		modelAndView.addObject("scheduleTimeHtmlClassColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY);
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);

		modelAndView.setViewName("calendar-all");
		return modelAndView;
	}
}

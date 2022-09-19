package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.common.Const;
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
		modelAndView.addObject("calendarList", calendarBean.getCalendarList());
		modelAndView.addObject("isScheduleDisplayArray", calendarBean.getIsScheduleDisplayArray());
		modelAndView.addObject("afterYm", calendarBean.getAfterYm());
		modelAndView.addObject("beforeYm", calendarBean.getBeforeYm());
		modelAndView.addObject("scheduleTimeList", calendarBean.getScheduleTimeList());
		modelAndView.addObject("scheduleTimeHtmlClassColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY);
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);

		modelAndView.setViewName("calendar");
		return modelAndView;
	}
}

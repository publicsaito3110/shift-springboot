package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.common.Const;
import com.shift.domain.model.bean.ScheduleBean;
import com.shift.domain.service.ScheduleService;

/**
 * @author saito
 *
 */
@Controller
public class ScheduleController extends BaseController {

	@Autowired
	private ScheduleService scheduleService;


	/**
	 * スケジュール登録画面<br>
	 * [Controller] (/schedule)
	 *
	 * @param ym RequestParameter(required=false)
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/schedule")
	public ModelAndView schedule(@RequestParam(value="ym",required=false) String ym, Authentication authentication, ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();

		//Service
		ScheduleBean scheduleBean = scheduleService.schedule(ym, loginUser);
		modelAndView.addObject("year", scheduleBean.getYear());
		modelAndView.addObject("month", scheduleBean.getMonth());
		modelAndView.addObject("calendarList", scheduleBean.getCalendarList());
		modelAndView.addObject("nowYm", scheduleBean.getNowYm());
		modelAndView.addObject("afterYm", scheduleBean.getAfterYm());
		modelAndView.addObject("beforeYm", scheduleBean.getBeforeYm());
		modelAndView.addObject("scheduleTimeList", scheduleBean.getScheduleTimeList());
		modelAndView.addObject("scheduleTimeHtmlClassColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY);
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);

		modelAndView.setViewName("schedule");
		return modelAndView;
	}


	/**
	 * スケジュール修正機能面<br>
	 * [Controller] (/schedule/modify)
	 *
	 * @param ym RequestParameter
	 * @param schedule1 RequestParameter
	 * @param schedule2 RequestParameter
	 * @param schedule3 RequestParameter
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/schedule/modify", method = RequestMethod.POST)
	public ModelAndView scheduleModify(@RequestParam(value="ym") String ym, @RequestParam(value="day") String day, @RequestParam(value="schedule1",required=false) String schedule1, @RequestParam(value="schedule2",required=false) String schedule2, @RequestParam(value="schedule3",required=false) String schedule3, Authentication authentication, ModelAndView modelAndView) {

		//TODO スケジュール修正処理の追加

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();

		//Service
		ScheduleBean scheduleBean = scheduleService.schedule(ym, loginUser);
		modelAndView.addObject("year", scheduleBean.getYear());
		modelAndView.addObject("month", scheduleBean.getMonth());
		modelAndView.addObject("calendarList", scheduleBean.getCalendarList());
		modelAndView.addObject("nowYm", scheduleBean.getNowYm());
		modelAndView.addObject("afterYm", scheduleBean.getAfterYm());
		modelAndView.addObject("beforeYm", scheduleBean.getBeforeYm());
		modelAndView.addObject("scheduleTimeList", scheduleBean.getScheduleTimeList());
		modelAndView.addObject("scheduleTimeHtmlClassColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY);
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);

		modelAndView.setViewName("schedule");
		return modelAndView;
	}
}

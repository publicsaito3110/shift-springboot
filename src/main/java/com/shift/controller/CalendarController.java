package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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

	@RequestMapping("/calendar")
	public ModelAndView calendar(@RequestParam(value="ym",required=false) String ym, ModelAndView modelAndView) {

		CalendarBean calendarBean = calendarService.calendar(ym);
		modelAndView.addObject("year", calendarBean.getYear());
		modelAndView.addObject("month", calendarBean.getMonth());
		modelAndView.addObject("calendarList", calendarBean.getCalendarList());
		modelAndView.addObject("afterYm", calendarBean.getAfterYm());
		modelAndView.addObject("beforeYm", calendarBean.getBeforeYm());

		modelAndView.setViewName("calendar");
		return modelAndView;
	}

}

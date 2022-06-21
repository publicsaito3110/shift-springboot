package com.shift.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CalendarController {

	@Autowired
	CalendarService calendarService;

	@RequestMapping(value="/calendar", method={RequestMethod.GET, RequestMethod.POST})
	public String sum(Model model) {

		calendarService.calendar(model);
		return "calendar";
	}

}

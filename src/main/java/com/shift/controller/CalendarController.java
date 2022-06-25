package com.shift.controller;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.service.CalendarService;

@Controller
public class CalendarController {

	@Autowired
	CalendarService calendarService;

	@RequestMapping(value="/calendar")
	public ModelAndView calendar(@RequestParam(value="ym",required=false) String ym, ModelAndView modelAndView) throws ServletException, IOException {

		calendarService.calendar(modelAndView, ym);
		modelAndView.setViewName("calendar");
		return modelAndView;
	}

}

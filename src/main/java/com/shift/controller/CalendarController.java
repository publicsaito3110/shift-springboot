package com.shift.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shift.domain.service.CalendarService;

@Controller
public class CalendarController {

	@Autowired
	CalendarService calendarService;

	@RequestMapping(value="/calendar", method={RequestMethod.GET, RequestMethod.POST})
	public String sum(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		calendarService.calendar(request, response);
		return "calendar";
	}

}

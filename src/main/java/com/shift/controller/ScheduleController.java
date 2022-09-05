package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
	 * @param ym (required=false)
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/schedule")
	public ModelAndView schedule(@RequestParam(value="ym",required=false) String ym, Authentication authentication, ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();

		//Service
		ScheduleBean scheduleBean = scheduleService.schedule(ym, loginUser);

		modelAndView.setViewName("schedule");
		return modelAndView;
	}
}
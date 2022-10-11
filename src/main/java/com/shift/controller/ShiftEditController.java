package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shift.common.Const;
import com.shift.domain.model.bean.ShiftEditBean;
import com.shift.domain.service.ShiftEditService;

/**
 * @author saito
 *
 */
@Controller
public class ShiftEditController extends BaseController {

	@Autowired
	private ShiftEditService shiftEdiService;


	/**
	 * スケジュール時間確認画面<br>
	 * [Controller] (/shift-edit)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/shift-edit")
	public ModelAndView shiftEdit(Authentication authentication, ModelAndView modelAndView) {

		//Service
		ShiftEditBean shiftEditBean = shiftEdiService.shiftEdit();
		modelAndView.addObject("scheduleTimeEntity", shiftEditBean.getScheduleTimeEntity());
		modelAndView.addObject("scheduleTimeDivision", Const.SCHEDULE_RECORDABLE_MAX_DIVISION);
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);

		modelAndView.setViewName("shift-edit");
		return modelAndView;
	}
}

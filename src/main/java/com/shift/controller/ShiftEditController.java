package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.shift.common.Const;
import com.shift.domain.model.bean.ShiftEditBean;
import com.shift.domain.service.ShiftEditService;
import com.shift.form.ShiftEditModifyForm;

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
		modelAndView.addObject("shiftEditModifyForm", new ShiftEditModifyForm());
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
		//View
		modelAndView.setViewName("shift-edit");
		return modelAndView;
	}


	/**
	 * スケジュール時間修正機能<br>
	 * [Controller] (/shift-edit/modify)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/shift-edit/modify", method = RequestMethod.POST)
	public ModelAndView shiftEditModify(@Validated @ModelAttribute ShiftEditModifyForm shiftEditModifyForm, BindingResult bindingResult, Authentication authentication, ModelAndView modelAndView) {

		//バリデーションエラーのとき
		if (bindingResult.hasErrors()) {

			//Service
			ShiftEditBean shiftEditBean = shiftEdiService.shiftEdit();
			modelAndView.addObject("scheduleTimeEntity", shiftEditBean.getScheduleTimeEntity());
			modelAndView.addObject("shiftEditModifyForm", new ShiftEditModifyForm());
			modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
			//View
			modelAndView.setViewName("shift-edit");
			return modelAndView;
		}

		//Service
		ShiftEditBean shiftEditBean = shiftEdiService.shiftEdit();
		modelAndView.addObject("scheduleTimeEntity", shiftEditBean.getScheduleTimeEntity());
		modelAndView.addObject("shiftEditModifyForm", new ShiftEditModifyForm());
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
		//View
		modelAndView.setViewName("shift-edit");
		return modelAndView;
	}
}

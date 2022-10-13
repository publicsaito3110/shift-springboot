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
import com.shift.domain.model.bean.ShiftEditAddBean;
import com.shift.domain.model.bean.ShiftEditBean;
import com.shift.domain.service.ShiftEditService;
import com.shift.form.ShiftEditAddForm;

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
		modelAndView.addObject("shiftEditAddForm", new ShiftEditAddForm());
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
		modelAndView.addObject("isModalResult", false);
		//View
		modelAndView.setViewName("shift-edit");
		return modelAndView;
	}


	/**
	 * スケジュール時間修正機能<br>
	 * [Controller] (/shift-edit/add)
	 *
	 * @param shiftEditAddForm RequestParam Form
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/shift-edit/add", method = RequestMethod.POST)
	public ModelAndView shiftEditAdd(@Validated @ModelAttribute ShiftEditAddForm shiftEditAddForm, BindingResult bindingResult, Authentication authentication, ModelAndView modelAndView) {

		//バリデーションエラーのとき
		if (bindingResult.hasErrors()) {

			//Service
			ShiftEditBean shiftEditBean = shiftEdiService.shiftEdit();
			modelAndView.addObject("scheduleTimeEntity", shiftEditBean.getScheduleTimeEntity());
			modelAndView.addObject("shiftAddModifyForm", new ShiftEditAddForm());
			modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultTitle", "勤務時間の新規追加結果");
			modelAndView.addObject("modalResultContentFail", "入力値が不正です。勤務時間の新規追加に失敗しました。");
			//View
			modelAndView.setViewName("shift-edit");
			return modelAndView;
		}

		//Service
		ShiftEditAddBean shiftEditAddBean = shiftEdiService.shiftEditAdd(shiftEditAddForm);

		//新規のスケジュール時間区分の追加に失敗したとき
		if (!shiftEditAddBean.isInsertScheduleTime()) {

			modelAndView.addObject("scheduleTimeEntity", shiftEditAddBean.getScheduleTimeEntity());
			modelAndView.addObject("shiftEditAddForm", new ShiftEditAddForm());
			modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultTitle", "勤務時間の新規追加結果");
			modelAndView.addObject("modalResultContentFail", "勤務時間の新規追加に失敗しました。");
			//View
			modelAndView.setViewName("shift-edit");
			return modelAndView;
		}

		//新規のスケジュール時間区分の追加が成功したとき
		modelAndView.addObject("scheduleTimeEntity", shiftEditAddBean.getScheduleTimeEntity());
		modelAndView.addObject("shiftEditAddForm", new ShiftEditAddForm());
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
		modelAndView.addObject("isModalResult", true);
		modelAndView.addObject("modalResultTitle", "勤務時間の新規追加結果");
		modelAndView.addObject("modalResultContentSuccess", "勤務時間の新規追加に成功しました。");
		//View
		modelAndView.setViewName("shift-edit");
		return modelAndView;
	}
}

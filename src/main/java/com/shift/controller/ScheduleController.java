package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.common.Const;
import com.shift.domain.model.bean.ScheduleBean;
import com.shift.domain.model.bean.ScheduleModifyBean;
import com.shift.domain.service.ScheduleService;
import com.shift.form.ScheduleModifyForm;

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
	 * @param authentication Authentication
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
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
		modelAndView.addObject("scheduleModifyForm", new ScheduleModifyForm(scheduleBean.getIsScheduleRecordedArrayList(), scheduleBean.getNowYm()));
		modelAndView.addObject("isModalResult", false);

		modelAndView.setViewName("schedule");
		return modelAndView;
	}


	/**
	 * スケジュール修正機能<br>
	 * [Controller] (/schedule/modify)
	 *
	 * @param scheduleModifyForm RequestParameter
	 * @param bindingResult BindingResult
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/schedule/modify", method = RequestMethod.POST)
	public ModelAndView scheduleModify(@Validated @ModelAttribute ScheduleModifyForm scheduleModifyForm, BindingResult bindingResult, Authentication authentication, ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();

		//バリデーションエラーのとき
		if (bindingResult.hasErrors()) {

			//Service
			ScheduleBean scheduleBean = scheduleService.schedule(null, loginUser);
			modelAndView.addObject("year", scheduleBean.getYear());
			modelAndView.addObject("month", scheduleBean.getMonth());
			modelAndView.addObject("calendarList", scheduleBean.getCalendarList());
			modelAndView.addObject("nowYm", scheduleBean.getNowYm());
			modelAndView.addObject("afterYm", scheduleBean.getAfterYm());
			modelAndView.addObject("beforeYm", scheduleBean.getBeforeYm());
			modelAndView.addObject("scheduleTimeList", scheduleBean.getScheduleTimeList());
			modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultTitle", "シフト提出結果");
			modelAndView.addObject("modalResultContentFail", "入力値が不正です。");

			modelAndView.setViewName("schedule");
			return modelAndView;
		}

		//Service
		ScheduleModifyBean scheduleModifyBean = scheduleService.scheduleModify(scheduleModifyForm, loginUser);
		modelAndView.addObject("year", scheduleModifyBean.getYear());
		modelAndView.addObject("month", scheduleModifyBean.getMonth());
		modelAndView.addObject("calendarList", scheduleModifyBean.getCalendarList());
		modelAndView.addObject("nowYm", scheduleModifyBean.getNowYm());
		modelAndView.addObject("afterYm", scheduleModifyBean.getAfterYm());
		modelAndView.addObject("beforeYm", scheduleModifyBean.getBeforeYm());
		modelAndView.addObject("scheduleTimeList", scheduleModifyBean.getScheduleTimeList());
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
		modelAndView.addObject("scheduleModifyFormArray", scheduleModifyForm);
		modelAndView.addObject("isModalResult", true);
		modelAndView.addObject("modalResultTitle", "シフト提出結果");
		modelAndView.addObject("modalResultContentSuccess", "シフトを提出しました。");

		modelAndView.setViewName("schedule");
		return modelAndView;
	}
}

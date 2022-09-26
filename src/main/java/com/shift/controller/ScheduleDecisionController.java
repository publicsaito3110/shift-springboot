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
import com.shift.domain.model.bean.ScheduleDecisionBean;
import com.shift.domain.model.bean.ScheduleDecisionModifyBean;
import com.shift.domain.model.bean.ScheduleDecisionModifyModifyBean;
import com.shift.domain.service.ScheduleDecisionService;
import com.shift.form.ScheduleDecisionModifyForm;

/**
 * @author saito
 *
 */
@Controller
public class ScheduleDecisionController extends BaseController {

	@Autowired
	private ScheduleDecisionService scheduleDecisionService;


	/**
	 * 確定スケジュール画面<br>
	 * [Controller] (/schedule-decision)
	 *
	 * @param ym RequestParameter(required=false)
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/schedule-decision")
	public ModelAndView scheduleDecision(@RequestParam(value="ym",required=false) String ym, Authentication authentication, ModelAndView modelAndView) {

		//Service
		ScheduleDecisionBean scheduleDecisionBean = scheduleDecisionService.scheduleDecision(ym);
		modelAndView.addObject("year", scheduleDecisionBean.getYear());
		modelAndView.addObject("month", scheduleDecisionBean.getMonth());
		modelAndView.addObject("calendarList", scheduleDecisionBean.getCalendarList());
		modelAndView.addObject("nowYm", scheduleDecisionBean.getNowYm());
		modelAndView.addObject("afterYm", scheduleDecisionBean.getAfterYm());
		modelAndView.addObject("beforeYm", scheduleDecisionBean.getBeforeYm());
		modelAndView.addObject("scheduleTimeList", scheduleDecisionBean.getScheduleTimeList());
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);

		modelAndView.setViewName("schedule-decision");
		return modelAndView;
	}


	/**
	 * 確定スケジュール修正画面<br>
	 * [Controller] (/schedule-decision/modify)
	 *
	 * @param ym RequestParameter
	 * @param day RequestParameter
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/schedule-decision/modify", method = RequestMethod.POST)
	public ModelAndView scheduleDecisionModify(@RequestParam(value="ym") String ym, @RequestParam(value="day") String day, Authentication authentication, ModelAndView modelAndView) {

		//Service
		ScheduleDecisionModifyBean scheduleDecisionModifyBean = scheduleDecisionService.scheduleDecisionModify(ym, day);
		modelAndView.addObject("year", scheduleDecisionModifyBean.getYear());
		modelAndView.addObject("month", scheduleDecisionModifyBean.getMonth());
		modelAndView.addObject("day", scheduleDecisionModifyBean.getDay());
		modelAndView.addObject("schedulePreUserList", scheduleDecisionModifyBean.getSchedulePreUserList());
		modelAndView.addObject("scheduleUserList", scheduleDecisionModifyBean.getScheduleUserList());
		modelAndView.addObject("scheduleTimeList", scheduleDecisionModifyBean.getScheduleTimeList());
		modelAndView.addObject("userList", scheduleDecisionModifyBean.getUserList());
		modelAndView.addObject("scheduleTimeHtmlClassColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY);
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
		modelAndView.addObject("scheduleDecisionModifyForm", new ScheduleDecisionModifyForm(scheduleDecisionModifyBean.getScheduleUserList(), scheduleDecisionModifyBean.getYear(), scheduleDecisionModifyBean.getMonth(), scheduleDecisionModifyBean.getDay()));
		modelAndView.addObject("isModalResult", false);

		modelAndView.setViewName("schedule-decision-modify");
		return modelAndView;
	}


	/**
	 * 確定スケジュール修正機能<br>
	 * [Controller] (/schedule-decision/modif/modifyy)
	 *
	 * @param userAddForm RequestParameter
	 * @param bindingResult BindingResult
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/schedule-decision/modify/modify", method = RequestMethod.POST)
	public ModelAndView scheduleDecisionModifyModify(@Validated @ModelAttribute ScheduleDecisionModifyForm scheduleDecisionModifyForm, BindingResult bindingResult, Authentication authentication, ModelAndView modelAndView) {

		//バリデーションエラーのとき
		if (bindingResult.hasErrors()) {

			//Service
			ScheduleDecisionModifyBean scheduleDecisionModifyBean = scheduleDecisionService.scheduleDecisionModify("202209", "1");
			modelAndView.addObject("year", scheduleDecisionModifyBean.getYear());
			modelAndView.addObject("month", scheduleDecisionModifyBean.getMonth());
			modelAndView.addObject("day", scheduleDecisionModifyBean.getDay());
			modelAndView.addObject("schedulePreUserList", scheduleDecisionModifyBean.getSchedulePreUserList());
			modelAndView.addObject("scheduleUserList", scheduleDecisionModifyBean.getScheduleUserList());
			modelAndView.addObject("scheduleTimeList", scheduleDecisionModifyBean.getScheduleTimeList());
			modelAndView.addObject("userList", scheduleDecisionModifyBean.getUserList());
			modelAndView.addObject("scheduleTimeHtmlClassColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY);
			modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultTitle", "シフト登録結果");
			modelAndView.addObject("modalResultContentFail", "入力値が不正です。");

			modelAndView.setViewName("schedule-decision-modify");
			return modelAndView;
		}

		//Service
		ScheduleDecisionModifyModifyBean scheduleDecisionModifyModifyBean = scheduleDecisionService.scheduleDecisionModifyModify(scheduleDecisionModifyForm);
		modelAndView.addObject("year", scheduleDecisionModifyModifyBean.getYear());
		modelAndView.addObject("month", scheduleDecisionModifyModifyBean.getMonth());
		modelAndView.addObject("day", scheduleDecisionModifyModifyBean.getDay());
		modelAndView.addObject("schedulePreUserList", scheduleDecisionModifyModifyBean.getSchedulePreUserList());
		modelAndView.addObject("scheduleUserList", scheduleDecisionModifyModifyBean.getScheduleUserList());
		modelAndView.addObject("scheduleTimeList", scheduleDecisionModifyModifyBean.getScheduleTimeList());
		modelAndView.addObject("userList", scheduleDecisionModifyModifyBean.getUserList());
		modelAndView.addObject("scheduleTimeHtmlClassColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY);
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
		modelAndView.addObject("scheduleDecisionModifyForm", new ScheduleDecisionModifyForm(scheduleDecisionModifyModifyBean.getScheduleUserList(), scheduleDecisionModifyModifyBean.getYear(), scheduleDecisionModifyModifyBean.getMonth(), scheduleDecisionModifyModifyBean.getDay()));
		modelAndView.addObject("isModalResult", true);
		modelAndView.addObject("modalResultTitle", "シフト登録結果");
		modelAndView.addObject("modalResultContentSuccess", "シフトを登録しました。");

		modelAndView.setViewName("schedule-decision-modify");
		return modelAndView;
	}
}

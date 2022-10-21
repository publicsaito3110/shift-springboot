package com.shift.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.common.CommonLogic;
import com.shift.common.Const;
import com.shift.common.ExcelLogic;
import com.shift.domain.model.bean.ScheduleDecisionBean;
import com.shift.domain.model.bean.ScheduleDecisionDownloadShitXlsxBean;
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
		modelAndView.addObject("nowYm", scheduleDecisionBean.getNowYm());
		modelAndView.addObject("calendarList", scheduleDecisionBean.getCalendarList());
		modelAndView.addObject("userScheduleAllArray", scheduleDecisionBean.getUserScheduleAllArray());
		modelAndView.addObject("afterYm", scheduleDecisionBean.getAfterYm());
		modelAndView.addObject("beforeYm", scheduleDecisionBean.getBeforeYm());
		modelAndView.addObject("scheduleTimeEntity", scheduleDecisionBean.getScheduleTimeEntity());
		modelAndView.addObject("scheduleTimeHtmlClassColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY);
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
		//View
		modelAndView.setViewName("schedule-decision");
		return modelAndView;
	}


	/**
	 * 確定スケジュールダウンロード画面<br>
	 * [Controller] (/schedule-decision/download)
	 *
	 * @param ym RequestParameter ダウンロード対象の年月
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/schedule-decision/download")
	public ModelAndView scheduleDecisionDownload(@RequestParam(value="ym",required=false) String ym, Authentication authentication, ModelAndView modelAndView) {

		LocalDate ymLd = new CommonLogic().getLocalDateByYmd(ym + "01");
		modelAndView.addObject("year", ymLd.getYear());
		modelAndView.addObject("month", ymLd.getMonthValue());
		modelAndView.addObject("ym", ym);
		//View
		modelAndView.setViewName("schedule-decision-download");
		return modelAndView;
	}


	/**
	 * 確定スケジュールExcel出力機能<br>
	 * [Controller] (/schedule-decision)
	 *
	 * @param ym RequestParameter ダウンロードする確定スケジュールの年月
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return void
	 */
	@RequestMapping("/schedule-decision/download/DL_shift_{ym}.xlsx")
	public void scheduleDecisionDownloadShitXlsx(@PathVariable String ym, HttpServletResponse response, Authentication authentication, ModelAndView modelAndView) {

		//Service
		ScheduleDecisionDownloadShitXlsxBean scheduleDecisionDownloadShitXlsxBean = scheduleDecisionService.scheduleDecisionDownloadShitXlsx(ym);
		//ダウンロード処理
		new ExcelLogic().outputExcelFile(response, scheduleDecisionDownloadShitXlsxBean.getOutFilePath(), scheduleDecisionDownloadShitXlsxBean.getDownloadFileName());
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
		modelAndView.addObject("schedulePreDayList", scheduleDecisionModifyBean.getSchedulePreDayList());
		modelAndView.addObject("scheduleDayList", scheduleDecisionModifyBean.getScheduleDayList());
		modelAndView.addObject("scheduleTimeEntity", scheduleDecisionModifyBean.getScheduleTimeEntity());
		modelAndView.addObject("userList", scheduleDecisionModifyBean.getUserList());
		modelAndView.addObject("scheduleTimeHtmlClassColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY);
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
		modelAndView.addObject("scheduleDecisionModifyForm", new ScheduleDecisionModifyForm(scheduleDecisionModifyBean.getScheduleDayList(), scheduleDecisionModifyBean.getYear(), scheduleDecisionModifyBean.getMonth(), scheduleDecisionModifyBean.getDay()));
		modelAndView.addObject("isModalResult", false);
		//View
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
			ScheduleDecisionModifyBean scheduleDecisionModifyBean = scheduleDecisionService.scheduleDecisionModify(scheduleDecisionModifyForm.getYm(), scheduleDecisionModifyForm.getDay());
			modelAndView.addObject("year", scheduleDecisionModifyBean.getYear());
			modelAndView.addObject("month", scheduleDecisionModifyBean.getMonth());
			modelAndView.addObject("day", scheduleDecisionModifyBean.getDay());
			modelAndView.addObject("schedulePreDayList", scheduleDecisionModifyBean.getSchedulePreDayList());
			modelAndView.addObject("scheduleDayList", scheduleDecisionModifyBean.getScheduleDayList());
			modelAndView.addObject("scheduleTimeEntity", scheduleDecisionModifyBean.getScheduleTimeEntity());
			modelAndView.addObject("userList", scheduleDecisionModifyBean.getUserList());
			modelAndView.addObject("scheduleTimeHtmlClassColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY);
			modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
			modelAndView.addObject("scheduleDecisionModifyForm", new ScheduleDecisionModifyForm(scheduleDecisionModifyBean.getScheduleDayList(), scheduleDecisionModifyBean.getYear(), scheduleDecisionModifyBean.getMonth(), scheduleDecisionModifyBean.getDay()));
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultTitle", "シフト登録結果");
			modelAndView.addObject("modalResultContentFail", "入力値が不正です。");
			//View
			modelAndView.setViewName("schedule-decision-modify");
			return modelAndView;
		}

		//Service
		ScheduleDecisionModifyModifyBean scheduleDecisionModifyModifyBean = scheduleDecisionService.scheduleDecisionModifyModify(scheduleDecisionModifyForm);
		modelAndView.addObject("year", scheduleDecisionModifyModifyBean.getYear());
		modelAndView.addObject("month", scheduleDecisionModifyModifyBean.getMonth());
		modelAndView.addObject("day", scheduleDecisionModifyModifyBean.getDay());
		modelAndView.addObject("schedulePreDayList", scheduleDecisionModifyModifyBean.getSchedulePreDayList());
		modelAndView.addObject("scheduleDayList", scheduleDecisionModifyModifyBean.getScheduleDayList());
		modelAndView.addObject("scheduleTimeEntity", scheduleDecisionModifyModifyBean.getScheduleTimeEntity());
		modelAndView.addObject("userList", scheduleDecisionModifyModifyBean.getUserList());
		modelAndView.addObject("scheduleTimeHtmlClassColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY);
		modelAndView.addObject("scheduleTimeHtmlClassBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY);
		modelAndView.addObject("scheduleDecisionModifyForm", new ScheduleDecisionModifyForm(scheduleDecisionModifyModifyBean.getScheduleDayList(), scheduleDecisionModifyModifyBean.getYear(), scheduleDecisionModifyModifyBean.getMonth(), scheduleDecisionModifyModifyBean.getDay()));
		modelAndView.addObject("isModalResult", true);
		modelAndView.addObject("modalResultTitle", "シフト登録結果");
		modelAndView.addObject("modalResultContentSuccess", "シフトを登録しました。");
		//View
		modelAndView.setViewName("schedule-decision-modify");
		return modelAndView;
	}
}

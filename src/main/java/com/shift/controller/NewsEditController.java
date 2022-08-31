package com.shift.controller;

import java.util.List;

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
import com.shift.common.ValidationSingleLogic;
import com.shift.domain.model.bean.NewsEditAddBean;
import com.shift.domain.model.bean.NewsEditBean;
import com.shift.domain.model.bean.NewsEditModifyBean;
import com.shift.domain.model.bean.ValidationBean;
import com.shift.domain.service.NewsEditService;
import com.shift.form.NewsEditModifyForm;

/**
 * @author saito
 *
 */
@Controller
public class NewsEditController extends BaseController {

	@Autowired
	private NewsEditService newsEditService;


	/**
	 * お知らせ編集画面<br>
	 * [Controller] (/news-edit)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/news-edit")
	public ModelAndView newsEdit(Authentication authentication, ModelAndView modelAndView) {

		//Service
		NewsEditBean newsEditBean = newsEditService.newsEdit();
		modelAndView.addObject("newsList", newsEditBean.getNewsList());
		modelAndView.addObject("newsRecordList", newsEditBean.getNewsRecordList());
		modelAndView.addObject("newsRecordableMinDate", newsEditBean.getNewsRecordableMinDate());
		modelAndView.addObject("newsRecordableMaxDate", newsEditBean.getNewsRecordableMaxDate());
		modelAndView.addObject("newsCategoryArray", Const.NEWS_CATEGORY_ALL_ARRAY);
		modelAndView.addObject("newsEditModifyForm", new NewsEditModifyForm());
		modelAndView.addObject("isModalResult", false);
		modelAndView.addObject("titleValidationBean", new ValidationBean());
		modelAndView.addObject("dateValidationBean", new ValidationBean());
		modelAndView.addObject("categoryValidationBean", new ValidationBean());
		modelAndView.addObject("contentValidationBean", new ValidationBean());

		modelAndView.setViewName("news-edit");
		return modelAndView;
	}


	/**
	 * お知らせ修正機能<br>
	 * [Controller] (/news-edit/modify)
	 *
	 * @param newsEditModifyForm RequestParameter
	 * @param bindingResult BindingResult
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/news-edit/modify", method = RequestMethod.POST)
	public ModelAndView newsEditModify(@Validated @ModelAttribute NewsEditModifyForm newsEditModifyForm, BindingResult bindingResult, Authentication authentication, ModelAndView modelAndView) {

		//バリデーションエラーのとき
		if (bindingResult.hasErrors()) {

			//Service
			NewsEditBean newsEditBean = newsEditService.newsEdit();
			modelAndView.addObject("newsList", newsEditBean.getNewsList());
			modelAndView.addObject("newsRecordList", newsEditBean.getNewsRecordList());
			modelAndView.addObject("newsRecordableMinDate", newsEditBean.getNewsRecordableMinDate());
			modelAndView.addObject("newsRecordableMaxDate", newsEditBean.getNewsRecordableMaxDate());
			modelAndView.addObject("newsCategoryArray", Const.NEWS_CATEGORY_ALL_ARRAY);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultTitle", "お知らせ修正結果");
			modelAndView.addObject("modalResultContentFail", "お知らせの修正に失敗しました。");
			modelAndView.addObject("titleValidationBean", new ValidationBean());
			modelAndView.addObject("dateValidationBean", new ValidationBean());
			modelAndView.addObject("categoryValidationBean", new ValidationBean());
			modelAndView.addObject("contentValidationBean", new ValidationBean());

			modelAndView.setViewName("news-edit");
			return modelAndView;
		}

		//Service
		NewsEditModifyBean newsEditModifyBean = newsEditService.newsEditModify(newsEditModifyForm);
		modelAndView.addObject("newsList", newsEditModifyBean.getNewsList());
		modelAndView.addObject("newsRecordList", newsEditModifyBean.getNewsRecordList());
		modelAndView.addObject("newsRecordableMinDate", newsEditModifyBean.getNewsRecordableMinDate());
		modelAndView.addObject("newsRecordableMaxDate", newsEditModifyBean.getNewsRecordableMaxDate());
		modelAndView.addObject("newsEditModifyForm", new NewsEditModifyForm());
		modelAndView.addObject("newsCategoryArray", Const.NEWS_CATEGORY_ALL_ARRAY);
		modelAndView.addObject("isModalResult", true);
		modelAndView.addObject("modalResultTitle", "お知らせ修正結果");
		modelAndView.addObject("modalResultContentSuccess", "お知らせを修正しました。");
		modelAndView.addObject("titleValidationBean", new ValidationBean());
		modelAndView.addObject("dateValidationBean", new ValidationBean());
		modelAndView.addObject("categoryValidationBean", new ValidationBean());
		modelAndView.addObject("contentValidationBean", new ValidationBean());

		modelAndView.setViewName("news-edit");
		return modelAndView;
	}


	/**
	 * お知らせ追加機能<br>
	 * [Controller] (/news-edit/add)
	 *
	 * @param title RequestParameter
	 * @param date RequestParameter
	 * @param category RequestParameter
	 * @param content RequestParameter
	 * @param bindingResult BindingResult
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/news-edit/add", method = RequestMethod.POST)
	public ModelAndView newsEditAdd(@RequestParam(value="title") String title, @RequestParam(value="date") String date, @RequestParam(value="category") String category, @RequestParam(value="content") String content, Authentication authentication, ModelAndView modelAndView) {

		//バリデーションチェック
		ValidationSingleLogic validationSingleLogic = new ValidationSingleLogic(title, Const.PATTERN_NEWS_TITLE_INPUT, "20文字以内で入力してください");
		validationSingleLogic.checkValidation(date, Const.PATTERN_NEWS_UNIQUE_DATE_INPUT, "入力値が不正です");
		validationSingleLogic.checkValidation(category, Const.PATTERN_NEWS_CATEGORY_INPUT, "入力値が不正です");
		validationSingleLogic.checkValidation(content, Const.PATTERN_NEWS_CONTENT_INPUT, "200文字以内で入力してください");

		//バリデーションエラーのとき
		if (validationSingleLogic.isValidationEroor()) {

			//Service
			NewsEditBean newsEditBean = newsEditService.newsEdit();
			modelAndView.addObject("newsList", newsEditBean.getNewsList());
			modelAndView.addObject("newsRecordList", newsEditBean.getNewsRecordList());
			modelAndView.addObject("newsRecordableMinDate", newsEditBean.getNewsRecordableMinDate());
			modelAndView.addObject("newsRecordableMaxDate", newsEditBean.getNewsRecordableMaxDate());
			modelAndView.addObject("newsEditModifyForm", new NewsEditModifyForm());
			modelAndView.addObject("newsCategoryArray", Const.NEWS_CATEGORY_ALL_ARRAY);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultTitle", "お知らせ新規追加結果");
			modelAndView.addObject("modalResultContentFail", "お知らせの新規追加に失敗しました。");
			List<ValidationBean> validationBeanList = validationSingleLogic.getValidationResult();
			modelAndView.addObject("titleValidationBean", validationBeanList.get(0));
			modelAndView.addObject("dateValidationBean", validationBeanList.get(1));
			modelAndView.addObject("categoryValidationBean", validationBeanList.get(2));
			modelAndView.addObject("contentValidationBean", validationBeanList.get(3));

			modelAndView.setViewName("news-edit");
			return modelAndView;
		}

		//Service
		NewsEditAddBean newsEditAddBean = newsEditService.newsEditAdd(title, date, category, content);
		modelAndView.addObject("newsList", newsEditAddBean.getNewsList());
		modelAndView.addObject("newsRecordList", newsEditAddBean.getNewsRecordList());
		modelAndView.addObject("newsRecordableMinDate", newsEditAddBean.getNewsRecordableMinDate());
		modelAndView.addObject("newsRecordableMaxDate", newsEditAddBean.getNewsRecordableMaxDate());
		modelAndView.addObject("newsEditModifyForm", new NewsEditModifyForm());
		modelAndView.addObject("newsCategoryArray", Const.NEWS_CATEGORY_ALL_ARRAY);
		modelAndView.addObject("isModalResult", true);
		modelAndView.addObject("modalResultTitle", "お知らせ新規追加結果");
		modelAndView.addObject("modalResultContentSuccess", "お知らせを新規追加しました。");
		modelAndView.addObject("titleValidationBean", new ValidationBean());
		modelAndView.addObject("dateValidationBean", new ValidationBean());
		modelAndView.addObject("categoryValidationBean", new ValidationBean());
		modelAndView.addObject("contentValidationBean", new ValidationBean());

		modelAndView.setViewName("news-edit");
		return modelAndView;
	}
}

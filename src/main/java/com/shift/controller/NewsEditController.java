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
import org.springframework.web.servlet.ModelAndView;

import com.shift.common.CommonUtil;
import com.shift.common.Const;
import com.shift.domain.model.bean.NewsEditAddBean;
import com.shift.domain.model.bean.NewsEditBean;
import com.shift.domain.model.bean.NewsEditModifyBean;
import com.shift.domain.service.NewsEditService;
import com.shift.form.NewsEditAddForm;
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
		modelAndView.addObject("newsEditAddForm", new NewsEditAddForm());
		modelAndView.addObject("isModalResult", false);
		//View
		modelAndView.setViewName("news-edit");
		return modelAndView;
	}


	/**
	 * お知らせ修正機能<br>
	 * [Controller] (/news-edit/modify)
	 *
	 * @param newsEditModifyForm RequestParameter Form
	 * @param bindingResult BindingResult
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/news-edit/modify", method = RequestMethod.POST)
	public ModelAndView newsEditModify(@Validated @ModelAttribute NewsEditModifyForm newsEditModifyForm, BindingResult bindingResult, Authentication authentication, ModelAndView modelAndView) {

		//バリデーションエラーのとき
		if (bindingResult.hasErrors()) {

			//最初のエラーメッセージを取得
			List<String> errorMessageList = CommonUtil.getErrorMessage(bindingResult);
			String firstErrorMessage = errorMessageList.get(0);

			//Service
			NewsEditBean newsEditBean = newsEditService.newsEdit();
			modelAndView.addObject("newsList", newsEditBean.getNewsList());
			modelAndView.addObject("newsRecordList", newsEditBean.getNewsRecordList());
			modelAndView.addObject("newsRecordableMinDate", newsEditBean.getNewsRecordableMinDate());
			modelAndView.addObject("newsRecordableMaxDate", newsEditBean.getNewsRecordableMaxDate());
			modelAndView.addObject("newsCategoryArray", Const.NEWS_CATEGORY_ALL_ARRAY);
			modelAndView.addObject("newsEditAddForm", new NewsEditAddForm());
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultTitle", "お知らせ修正エラー");
			modelAndView.addObject("modalResultContentFail", firstErrorMessage);
			//View
			modelAndView.setViewName("news-edit");
			return modelAndView;
		}

		//Service
		NewsEditModifyBean newsEditModifyBean = newsEditService.newsEditModify(newsEditModifyForm);

		//お知らせの更新に失敗したとき
		if (!newsEditModifyBean.isUpdate()) {
			modelAndView.addObject("newsList", newsEditModifyBean.getNewsList());
			modelAndView.addObject("newsRecordList", newsEditModifyBean.getNewsRecordList());
			modelAndView.addObject("newsRecordableMinDate", newsEditModifyBean.getNewsRecordableMinDate());
			modelAndView.addObject("newsRecordableMaxDate", newsEditModifyBean.getNewsRecordableMaxDate());
			modelAndView.addObject("newsEditModifyForm", new NewsEditModifyForm());
			modelAndView.addObject("newsEditAddForm", new NewsEditAddForm());
			modelAndView.addObject("newsCategoryArray", Const.NEWS_CATEGORY_ALL_ARRAY);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultTitle", "お知らせ修正エラー");
			modelAndView.addObject("modalResultContentFail", "お知らせの修正に失敗しました");
			//View
			modelAndView.setViewName("news-edit");
			return modelAndView;
		}

		//お知らせの更新に成功したとき
		modelAndView.addObject("newsList", newsEditModifyBean.getNewsList());
		modelAndView.addObject("newsRecordList", newsEditModifyBean.getNewsRecordList());
		modelAndView.addObject("newsRecordableMinDate", newsEditModifyBean.getNewsRecordableMinDate());
		modelAndView.addObject("newsRecordableMaxDate", newsEditModifyBean.getNewsRecordableMaxDate());
		modelAndView.addObject("newsEditModifyForm", new NewsEditModifyForm());
		modelAndView.addObject("newsEditAddForm", new NewsEditAddForm());
		modelAndView.addObject("newsCategoryArray", Const.NEWS_CATEGORY_ALL_ARRAY);
		modelAndView.addObject("isModalResult", true);
		modelAndView.addObject("modalResultTitle", "お知らせ修正結果");
		modelAndView.addObject("modalResultContentSuccess", "お知らせを修正しました。");
		//View
		modelAndView.setViewName("news-edit");
		return modelAndView;
	}


	/**
	 * お知らせ追加機能<br>
	 * [Controller] (/news-edit/add)
	 *
	 * @param newsEditAddForm RequestParameter Form
	 * @param bindingResult BindingResult
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/news-edit/add", method = RequestMethod.POST)
	public ModelAndView newsEditAdd(@Validated @ModelAttribute NewsEditAddForm newsEditAddForm, BindingResult bindingResult, Authentication authentication, ModelAndView modelAndView) {

		//バリデーションエラーのとき
		if (bindingResult.hasErrors() || newsEditAddForm.isErrorValidRelated()) {

			//Service
			NewsEditBean newsEditBean = newsEditService.newsEdit();
			modelAndView.addObject("newsList", newsEditBean.getNewsList());
			modelAndView.addObject("newsRecordList", newsEditBean.getNewsRecordList());
			modelAndView.addObject("newsRecordableMinDate", newsEditBean.getNewsRecordableMinDate());
			modelAndView.addObject("newsRecordableMaxDate", newsEditBean.getNewsRecordableMaxDate());
			modelAndView.addObject("newsEditModifyForm", new NewsEditModifyForm());
			modelAndView.addObject("newsEditAddForm", newsEditAddForm);
			modelAndView.addObject("newsCategoryArray", Const.NEWS_CATEGORY_ALL_ARRAY);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultTitle", "お知らせ新規追加エラー");
			if (bindingResult.hasErrors()) {
				//単項目エラーのとき、最初のエラーメッセージを取得し値をセットする
				List<String> errorMessageList = CommonUtil.getErrorMessage(bindingResult);
				String firstErrorMessage = errorMessageList.get(0);
				modelAndView.addObject("modalResultContentFail", firstErrorMessage);
			} else {
				//相関エラーのとき、値をセットする
				modelAndView.addObject("modalResultContentFail", "入力値が不正です");
			}
			//View
			modelAndView.setViewName("news-edit");
			return modelAndView;
		}

		//Service
		NewsEditAddBean newsEditAddBean = newsEditService.newsEditAdd(newsEditAddForm);
		modelAndView.addObject("newsList", newsEditAddBean.getNewsList());
		modelAndView.addObject("newsRecordList", newsEditAddBean.getNewsRecordList());
		modelAndView.addObject("newsRecordableMinDate", newsEditAddBean.getNewsRecordableMinDate());
		modelAndView.addObject("newsRecordableMaxDate", newsEditAddBean.getNewsRecordableMaxDate());
		modelAndView.addObject("newsEditModifyForm", new NewsEditModifyForm());
		modelAndView.addObject("newsEditAddForm", new NewsEditAddForm());
		modelAndView.addObject("newsCategoryArray", Const.NEWS_CATEGORY_ALL_ARRAY);
		modelAndView.addObject("isModalResult", true);
		modelAndView.addObject("modalResultTitle", "お知らせ新規追加結果");
		modelAndView.addObject("modalResultContentSuccess", "お知らせを新規追加しました。");
		//View
		modelAndView.setViewName("news-edit");
		return modelAndView;
	}
}

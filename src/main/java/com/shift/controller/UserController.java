package com.shift.controller;

import javax.servlet.http.HttpServletResponse;

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

import com.shift.common.CommonUtil;
import com.shift.common.Const;
import com.shift.domain.model.bean.UserBean;
import com.shift.domain.model.bean.UserModifyBean;
import com.shift.domain.service.UserService;
import com.shift.form.UserAddForm;
import com.shift.form.UserModifyForm;

/**
 * @author saito
 *
 */
@Controller
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@RequestMapping("/user")
	public ModelAndView user(@RequestParam(value="p",required=false) String page, @RequestParam(value="keyword",required=false) String keyword, Authentication authentication,  ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();
		//ログインユーザのROLEを取得
		String[] userRoleArray = CommonUtil.getUserRoleArrayByAuthentication(authentication);

		//Service
		UserBean userBean = userService.user(page, keyword, loginUser, userRoleArray);
		modelAndView.addObject("userList", userBean.getUserList());
		modelAndView.addObject("searchHitCount", userBean.getSearchHitCount());
		modelAndView.addObject("paginationList", userBean.getPaginationList());
		modelAndView.addObject("keyword", userBean.getKeywordFormatNotNull());
		modelAndView.addObject("beforePage", userBean.getBeforePage());
		modelAndView.addObject("afterPage", userBean.getAfterPage());
		modelAndView.addObject("isPaginationIndex", userBean.isPaginationIndex());

		modelAndView.setViewName("user");
		return modelAndView;
	}


	@RequestMapping(value = "/user/add")
	public ModelAndView userAdd(Authentication authentication, ModelAndView modelAndView) {

		modelAndView.addObject("genderAllArray", Const.USER_GENDER_ALL_ARRAY);
		modelAndView.addObject("adminFlg", Const.USER_ADMIN_FLG);
		modelAndView.addObject("userAddForm", new UserAddForm());
		modelAndView.addObject("isModalResult", false);

		modelAndView.setViewName("user-add");
		return modelAndView;
	}


	@RequestMapping(value = "/user/add/add", method = RequestMethod.POST)
	public ModelAndView userAdd(@Validated @ModelAttribute UserAddForm userAddForm, BindingResult bindingResult, Authentication authentication, ModelAndView modelAndView) {

		//バリデーションエラーのとき
		if (bindingResult.hasErrors()) {

			modelAndView.addObject("genderAllArray", Const.USER_GENDER_ALL_ARRAY);
			modelAndView.addObject("adminFlg", Const.USER_ADMIN_FLG);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultContentFail", "ユーザーの新規追加に失敗しました。");

			modelAndView.setViewName("user-add");
			return modelAndView;
		}

		//Service
		userService.userAddAdd(userAddForm);
		modelAndView.addObject("genderAllArray", Const.USER_GENDER_ALL_ARRAY);
		modelAndView.addObject("adminFlg", Const.USER_ADMIN_FLG);
		modelAndView.addObject("userAddForm", new UserAddForm());
		modelAndView.addObject("isModalResult", true);
		modelAndView.addObject("modalResultTitle", "ユーザー新規追加結果");
		modelAndView.addObject("modalResultContentSuccess", "ユーザーを新規追加しました。");

		modelAndView.setViewName("user-add");
		return modelAndView;
	}


	@RequestMapping(value = "/user/download")
	public ModelAndView userDownload(ModelAndView modelAndView) {

		modelAndView.setViewName("user-download");
		return modelAndView;
	}


	@RequestMapping(value = "/user/download/user.xlsx")
	public void userDownloadUserXlsx(HttpServletResponse response, ModelAndView modelAndView) {

		//Service
		userService.userDownloadUserXlsx(response);
	}


	@RequestMapping(value = "/user/modify", method = RequestMethod.POST)
	public ModelAndView userModify(@RequestParam(value="userId") String userId, Authentication authentication, ModelAndView modelAndView) {

		//Service
		UserModifyBean userModifyBean = userService.userModify(userId);
		UserModifyForm userModifyForm = new UserModifyForm();
		userModifyForm.setUserId(userModifyBean.getUserEntity().getId());
		userModifyForm.setName(userModifyBean.getUserEntity().getName());
		userModifyForm.setNameKana(userModifyBean.getUserEntity().getNameKana());
		userModifyForm.setGender(userModifyBean.getUserEntity().getGender());
		userModifyForm.setNote(userModifyBean.getUserEntity().getNote());
		modelAndView.addObject("genderAllArray", Const.USER_GENDER_ALL_ARRAY);
		modelAndView.addObject("userModifyForm", userModifyForm);
		modelAndView.addObject("isModalResult", false);

		modelAndView.setViewName("user-modify");
		return modelAndView;
	}


	@RequestMapping(value = "/user/modify/modify", method = RequestMethod.POST)
	public ModelAndView userModifyModify(@Validated @ModelAttribute UserModifyForm userModifyForm, BindingResult bindingResult, Authentication authentication, ModelAndView modelAndView) {

		//バリデーションエラーのとき
		if (bindingResult.hasErrors()) {

			modelAndView.addObject("genderAllArray", Const.USER_GENDER_ALL_ARRAY);
			modelAndView.addObject("adminFlg", Const.USER_ADMIN_FLG);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultContentFail", "ユーザーの修正に失敗しました。");

			modelAndView.setViewName("user-modify");
			return modelAndView;
		}

		//Service
		userService.userModifyModify(userModifyForm);
		modelAndView.addObject("genderAllArray", Const.USER_GENDER_ALL_ARRAY);
		modelAndView.addObject("userModifyForm", userModifyForm);
		modelAndView.addObject("isModalResult", true);
		modelAndView.addObject("modalResultTitle", "ユーザー情報修正結果");
		modelAndView.addObject("modalResultContentSuccess", "ユーザー情報を修正しました。");

		modelAndView.setViewName("user-modify");
		return modelAndView;
	}
}

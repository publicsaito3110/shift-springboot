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
import com.shift.common.ExcelLogic;
import com.shift.domain.model.bean.UserBean;
import com.shift.domain.model.bean.UserDownloadUserXlsxBean;
import com.shift.domain.model.bean.UserListBean;
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


	/**
	 * ユーザ詳細画面<br>
	 * [Controller] (/user)
	 *
	 * @param userId RequestParameter
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/user")
	public ModelAndView user(@RequestParam(value="userId",required=false) String userId, Authentication authentication, ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();

		//Service
		UserBean userBean = userService.user(userId, loginUser);
		modelAndView.addObject("userEntity", userBean.getUserEntity());
		if (userBean.isUserModifyForm()) {
			modelAndView.addObject("isUserModifyForm", true);
		}

		modelAndView.setViewName("user");
		return modelAndView;
	}


	/**
	 * ユーザ一覧画面<br>
	 * [Controller] (/user/list)
	 *
	 * @param page RequestParameter (value="p",required=false)
	 * @param keyword RequestParameter (required=false)
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/user/list")
	public ModelAndView userList(@RequestParam(value="p",required=false) String page, @RequestParam(value="keyword",required=false) String keyword, Authentication authentication,  ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();
		//ログインユーザのROLEを取得
		String[] userRoleArray = CommonUtil.getUserRoleArrayByAuthentication(authentication);

		//Service
		UserListBean userListBean = userService.userList(page, keyword, loginUser, userRoleArray);
		modelAndView.addObject("userList", userListBean.getUserList());
		modelAndView.addObject("searchHitCount", userListBean.getSearchHitCount());
		modelAndView.addObject("paginationList", userListBean.getPaginationList());
		modelAndView.addObject("keyword", userListBean.getKeywordFormatNotNull());
		modelAndView.addObject("beforePage", userListBean.getBeforePage());
		modelAndView.addObject("afterPage", userListBean.getAfterPage());
		modelAndView.addObject("isPaginationIndex", userListBean.isPaginationIndex());

		modelAndView.setViewName("user-list");
		return modelAndView;
	}


	/**
	 * ユーザ追加画面<br>
	 * [Controller] (/user/add)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/user/add")
	public ModelAndView userAdd(Authentication authentication, ModelAndView modelAndView) {

		modelAndView.addObject("genderAllArray", Const.USER_GENDER_ALL_ARRAY);
		modelAndView.addObject("adminFlg", Const.USER_ADMIN_FLG);
		modelAndView.addObject("userAddForm", new UserAddForm());
		modelAndView.addObject("isModalResult", false);

		modelAndView.setViewName("user-add");
		return modelAndView;
	}


	/**
	 * ユーザ追加機能<br>
	 * [Controller] (/user/add/add)
	 *
	 * @param userAddForm RequestParameter
	 * @param bindingResult BindingResult
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/user/add/add", method = RequestMethod.POST)
	public ModelAndView userAdd(@Validated @ModelAttribute UserAddForm userAddForm, BindingResult bindingResult, Authentication authentication, ModelAndView modelAndView) {

		//バリデーションエラーのとき
		if (bindingResult.hasErrors()) {

			modelAndView.addObject("genderAllArray", Const.USER_GENDER_ALL_ARRAY);
			modelAndView.addObject("adminFlg", Const.USER_ADMIN_FLG);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultTitle", "ユーザー新規追加結果");
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


	/**
	 * ユーザ一覧ダウンロード画面<br>
	 * [Controller] (/user/download)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/user/download")
	public ModelAndView userDownload(ModelAndView modelAndView) {

		modelAndView.setViewName("user-download");
		return modelAndView;
	}


	/**
	 * ユーザ一覧Excelダウンロード機能<br>
	 * [Controller] (/user/download/user.xlsx)
	 *
	 * @param response HttpServletResponse
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return void
	 */
	@RequestMapping(value = "/user/download/user.xlsx")
	public void userDownloadUserXlsx(HttpServletResponse response, Authentication authentication, ModelAndView modelAndView) {

		//Service
		UserDownloadUserXlsxBean userDownloadUserXlsxBean = userService.userDownloadUserXlsx();
		//ダウンロード処理
		new ExcelLogic().outputExcelFile(response, userDownloadUserXlsxBean.getOutFilePath(), userDownloadUserXlsxBean.getDownloadFileName());
	}


	/**
	 * ユーザ修正画面<br>
	 * [Controller] (/user/modify)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/user/modify")
	public ModelAndView userModify(Authentication authentication, ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();

		//Service
		UserModifyBean userModifyBean = userService.userModify(loginUser);
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


	/**
	 * ユーザ修正機能<br>
	 * [Controller] (/user/modify/modify)
	 *
	 * @param userModifyForm RequestParameter
	 * @param bindingResult BindingResult
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/user/modify/modify", method = RequestMethod.POST)
	public ModelAndView userModifyModify(@Validated @ModelAttribute UserModifyForm userModifyForm, BindingResult bindingResult, Authentication authentication, ModelAndView modelAndView) {

		//バリデーションエラーのとき
		if (bindingResult.hasErrors()) {

			modelAndView.addObject("genderAllArray", Const.USER_GENDER_ALL_ARRAY);
			modelAndView.addObject("adminFlg", Const.USER_ADMIN_FLG);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultTitle", "ユーザー情報修正結果");
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

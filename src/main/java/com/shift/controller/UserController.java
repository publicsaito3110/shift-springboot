package com.shift.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.common.Const;
import com.shift.domain.model.bean.UserBean;
import com.shift.domain.model.bean.UserDownloadUserTemplateBean;
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
	public ModelAndView user(@RequestParam(value="p",required=false) String page, @RequestParam(value="keyword",required=false) String keyword, ModelAndView modelAndView) {

		UserBean userBean =this.userService.user(page, keyword);
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
	public ModelAndView userAdd(ModelAndView modelAndView) {

		modelAndView.addObject("genderAllArray", Const.USER_GENDER_ALL_ARRAY);
		modelAndView.addObject("adminFlg", Const.USER_ADMIN_FLG);
		modelAndView.addObject("userAddForm", new UserAddForm());
		modelAndView.addObject("isModalResult", false);

		modelAndView.setViewName("user-add");
		return modelAndView;
	}


	@RequestMapping(value = "/user/add/add", method = RequestMethod.POST)
	public ModelAndView userAdd(@Validated @ModelAttribute UserAddForm userAddForm, BindingResult bindingResult, ModelAndView modelAndView) {

		//バリデーションエラーのとき
		if (bindingResult.hasErrors()) {

			modelAndView.addObject("genderAllArray", Const.USER_GENDER_ALL_ARRAY);
			modelAndView.addObject("adminFlg", Const.USER_ADMIN_FLG);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultContentFail", "ユーザーの新規追加に失敗しました。");

			modelAndView.setViewName("user-add");
			return modelAndView;
		}

		this.userService.userAddAdd(userAddForm);
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


	@RequestMapping(value = "/user/download/user-template.xlsx")
	public ModelAndView userDownloadUserTemplate(HttpServletResponse response, ModelAndView modelAndView) {

		UserDownloadUserTemplateBean userDownloadUserTemplateBean = this.userService.userDownloadUserTemplateXlsx();
		//-----------------------------------------------------
		//ダウンロード処理(EXCEL書き込み処理が成功時のみ実行)
		//-----------------------------------------------------
		try (OutputStream outputStream = response.getOutputStream();) {

			Path filePath = Paths.get(userDownloadUserTemplateBean.getOutFilePass());
			byte[] fileByte = Files.readAllBytes(filePath);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(userDownloadUserTemplateBean.getDownloadFileName(), "UTF-8") + "\"");
			response.setContentLength(fileByte.length);
			outputStream.write(fileByte);
			outputStream.flush();
		} catch (IOException e) {

			//例外発生時、エラーページへ遷移
			modelAndView.setViewName("error");
			return modelAndView;
		}

		modelAndView.setViewName("user-download");
		return modelAndView;
	}


	@RequestMapping(value = "/user/modify", method = RequestMethod.POST)
	public ModelAndView userModify(@RequestParam(value="userId") String userId, ModelAndView modelAndView) {

		UserModifyBean userModifyBean = this.userService.userModify(userId);
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
	public ModelAndView userModifyModify(@Validated @ModelAttribute UserModifyForm userModifyForm, BindingResult bindingResult, ModelAndView modelAndView) {

		//バリデーションエラーのとき
		if (bindingResult.hasErrors()) {

			modelAndView.addObject("genderAllArray", Const.USER_GENDER_ALL_ARRAY);
			modelAndView.addObject("adminFlg", Const.USER_ADMIN_FLG);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultContentFail", "ユーザーの修正に失敗しました。");

			modelAndView.setViewName("user-modify");
			return modelAndView;
		}

		this.userService.userModifyModify(userModifyForm);
		modelAndView.addObject("genderAllArray", Const.USER_GENDER_ALL_ARRAY);
		modelAndView.addObject("userModifyForm", userModifyForm);
		modelAndView.addObject("isModalResult", true);
		modelAndView.addObject("modalResultTitle", "ユーザー情報修正結果");
		modelAndView.addObject("modalResultContentSuccess", "ユーザー情報を修正しました。");

		modelAndView.setViewName("user-modify");
		return modelAndView;
	}
}

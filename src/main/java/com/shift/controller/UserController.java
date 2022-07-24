package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.common.Const;
import com.shift.domain.model.bean.UserBean;
import com.shift.domain.model.bean.UserModifyBean;
import com.shift.domain.service.UserService;
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
		modelAndView.addObject("indexCount", userBean.getIndexCount());
		modelAndView.addObject("paginationList", userBean.getPaginationList());
		modelAndView.addObject("keyword", userBean.getKeywordFormatNotNull());
		modelAndView.addObject("beforePage", userBean.getBeforePage());
		modelAndView.addObject("afterPage", userBean.getAfterPage());
		modelAndView.addObject("isPaginationIndex", userBean.isPaginationIndex());

		modelAndView.setViewName("user");
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
	public ModelAndView userModifyModify(@ModelAttribute UserModifyForm userModifyForm, ModelAndView modelAndView) {

		this.userService.userModifyModify(userModifyForm);
		modelAndView.addObject("genderAllArray", Const.USER_GENDER_ALL_ARRAY);
		modelAndView.addObject("userModifyForm", userModifyForm);
		modelAndView.addObject("isModalResult", true);
		modelAndView.addObject("modalResultTitle", "ユーザー情報修正結果");
		modelAndView.addObject("modalResultContent", "ユーザー情報を修正しました。");

		modelAndView.setViewName("user-modify");
		return modelAndView;
	}
}

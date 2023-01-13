package com.shift.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

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
import com.shift.domain.model.bean.DmAddressBean;
import com.shift.domain.model.bean.DmBean;
import com.shift.domain.model.bean.DmTalkBean;
import com.shift.domain.model.bean.DmTalkRoadBean;
import com.shift.domain.model.bean.DmTalkSendBean;
import com.shift.domain.service.DmService;
import com.shift.form.DmTalkSendForm;

/**
 * @author saito
 *
 */
@Controller
public class DmController extends BaseController {

	@Autowired
	private DmService dmService;

	@Autowired
	private HttpSession httpSession;


	/**
	 * メッセージ一覧表示機能<br>
	 * [Controller] (/dm)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/dm")
	public ModelAndView dm(Authentication authentication, ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();

		//Service
		DmBean dmBean = dmService.dm(loginUser, httpSession);
		modelAndView.addObject("dmFinalHistoryList", dmBean.getDmFinalHistoryList());
		//View
		modelAndView.setViewName("dm");
		return modelAndView;
	}


	/**
	 * 連絡先一覧表示機能<br>
	 * [Controller] (/dm/address)
	 *
	 * @param keyword RequestParameter(required=false)
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/dm/address")
	public ModelAndView dmAddress(@RequestParam(value="keyword", required=false) String keyword, Authentication authentication, ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();

		//Service
		DmAddressBean dmAddressBean = dmService.dmAddress(keyword, loginUser);
		modelAndView.addObject("userList", dmAddressBean.getUserList());
		//View
		modelAndView.setViewName("dm-address");
		return modelAndView;
	}


	/**
	 * メッセージ履歴表示機能<br>
	 * [Controller] (/dm/talk)
	 *
	 * @param receiveUser RequestParameter
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/dm/talk", method = RequestMethod.POST)
	public ModelAndView dmTalk(@RequestParam(value="receiveUser") String receiveUser, Authentication authentication, ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();

		//Service
		DmTalkBean dmTalkBean = dmService.dmTalk(receiveUser, loginUser, httpSession);
		modelAndView.addObject("receiveUserName", dmTalkBean.getReceiveUserName());
		modelAndView.addObject("talkHistoryList", dmTalkBean.getTalkHistoryList());
		modelAndView.addObject("nextLastOffset", dmTalkBean.getNextLastOffset());
		modelAndView.addObject("chatLimit", Const.DM_CHAT_LIMIT);
		DmTalkSendForm dmTalkSendForm = new DmTalkSendForm();
		dmTalkSendForm.setReceiveUser(dmTalkBean.getReceiveUser());
		modelAndView.addObject("dmTalkSendForm", dmTalkSendForm);
		modelAndView.addObject("isModalResult", false);
		//View
		modelAndView.setViewName("dm-talk");
		return modelAndView;
	}


	/**
	 * メッセージ履歴更新表示機能(非同期)<br>
	 * [Controller] (/dm/talk/road)
	 *
	 * @param receiveUser RequestParameter
	 * @param nextLastOffset RequestParameter
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/dm/talk/road")
	public ModelAndView dmTalkRoad(@RequestParam(value="receiveUser") String receiveUser, @RequestParam(value="nextLastOffset") String nextLastOffset, Authentication authentication, ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();

		//Service
		DmTalkRoadBean dmTalkRoadBean = dmService.dmTalkRoad(receiveUser, loginUser, nextLastOffset);
		modelAndView.addObject("talkHistoryList", dmTalkRoadBean.getTalkHistoryList());
		//View
		modelAndView.setViewName("dm-talk-road");
		return modelAndView;
	}


	/**
	 * メッセージ送信機能<br>
	 * [Controller] (/dm/talk/send)
	 *
	 * @param userModifyForm RequestParameter Form
	 * @param bindingResult BindingResult
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/dm/talk/send", method = RequestMethod.POST)
	public ModelAndView dmTalkSend(@Validated @ModelAttribute DmTalkSendForm dmTalkSendForm, BindingResult bindingResult, Authentication authentication, ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();

		//バリデーションエラーのとき
		if (bindingResult.hasErrors()) {

			//単項目エラーのとき、最初のエラーメッセージを取得し値をセットする
			List<String> errorMessageList = CommonUtil.getErrorMessage(bindingResult);
			String firstErrorMessage = errorMessageList.get(0);

			//Service
			DmTalkBean dmTalkBean = dmService.dmTalk(dmTalkSendForm.getReceiveUser(), loginUser, httpSession);
			modelAndView.addObject("receiveUserName", dmTalkBean.getReceiveUserName());
			modelAndView.addObject("talkHistoryList", dmTalkBean.getTalkHistoryList());
			modelAndView.addObject("nextLastOffset", dmTalkBean.getNextLastOffset());
			modelAndView.addObject("chatLimit", Const.DM_CHAT_LIMIT);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultTitle", "メッセージ送信エラー");
			modelAndView.addObject("modalResultContentFail", firstErrorMessage);
			//View
			modelAndView.setViewName("dm-talk");
			return modelAndView;
		}

		//Service
		DmTalkSendBean dmTalkSendBean = dmService.dmTalkSend(dmTalkSendForm, loginUser);
		modelAndView.addObject("receiveUserId", dmTalkSendBean.getReceiveUser());
		modelAndView.addObject("receiveUserName", dmTalkSendBean.getReceiveUserName());
		modelAndView.addObject("talkHistoryList", dmTalkSendBean.getTalkHistoryList());
		modelAndView.addObject("nextLastOffset", dmTalkSendBean.getNextLastOffset());
		modelAndView.addObject("chatLimit", Const.DM_CHAT_LIMIT);
		DmTalkSendForm newDmTalkSendForm = new DmTalkSendForm();
		newDmTalkSendForm.setReceiveUser(dmTalkSendBean.getReceiveUser());
		modelAndView.addObject("dmTalkSendForm", newDmTalkSendForm);
		modelAndView.addObject("isModalResult", false);
		//View
		modelAndView.setViewName("dm-talk");
		return modelAndView;
	}
}

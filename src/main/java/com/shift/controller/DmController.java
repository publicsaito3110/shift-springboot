package com.shift.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.common.Const;
import com.shift.common.ValidationSingleLogic;
import com.shift.domain.model.bean.DmAddressBean;
import com.shift.domain.model.bean.DmBean;
import com.shift.domain.model.bean.DmTalkBean;
import com.shift.domain.model.bean.DmTalkSendBean;
import com.shift.domain.model.bean.ValidationBean;
import com.shift.domain.service.DmService;

/**
 * @author saito
 *
 */
@Controller
public class DmController extends BaseController {

	@Autowired
	private DmService dmService;


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
		DmBean dmBean = dmService.dm(loginUser);
		modelAndView.addObject("dmFinalHistoryList", dmBean.getDmFinalHistoryList());

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
		DmTalkBean dmTalkBean = dmService.dmTalk(receiveUser, loginUser);
		modelAndView.addObject("receiveUser", dmTalkBean.getReceiveUser());
		modelAndView.addObject("receiveUserName", dmTalkBean.getReceiveUserName());
		modelAndView.addObject("talkHistoryList", dmTalkBean.getTalkHistoryList());
		modelAndView.addObject("msg", "");
		modelAndView.addObject("isModalResult", false);

		modelAndView.setViewName("dm-talk");
		return modelAndView;
	}


	/**
	 * メッセージ送信機能<br>
	 * [Controller] (/dm/talk/send)
	 *
	 * @param receiveUser RequestParameter
	 * @param msg RequestParameter
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/dm/talk/send", method = RequestMethod.POST)
	public ModelAndView dmTalkSend(@RequestParam(value="receiveUser") String receiveUser, @RequestParam(value="msg") String msg, Authentication authentication, ModelAndView modelAndView) {

		//authenticationからログインユーザのIDを取得
		String loginUser = authentication.getName();

		//バリデーションチェック
		ValidationSingleLogic validationSingleLogic = new ValidationSingleLogic(msg, Const.PATTERN_DM_MSG_INPUT, "200文字以内のみ有効です");

		//バリデーションエラーのとき
		if (validationSingleLogic.isValidationEroor()) {

			//Service
			DmTalkBean dmTalkBean = dmService.dmTalk(receiveUser, loginUser);
			modelAndView.addObject("receiveUser", dmTalkBean.getReceiveUser());
			modelAndView.addObject("receiveUserName", dmTalkBean.getReceiveUserName());
			modelAndView.addObject("talkHistoryList", dmTalkBean.getTalkHistoryList());
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultTitle", "メッセージ送信エラー");
			List<ValidationBean> validationBeanList = validationSingleLogic.getValidationResult();
			modelAndView.addObject("msg", validationBeanList.get(0).getInputQuery());
			modelAndView.addObject("modalResultContentFail", validationBeanList.get(0).getErrorMessage());

			modelAndView.setViewName("dm-talk");
			return modelAndView;
		}

		//Service
		DmTalkSendBean dmTalkSendBean = dmService.dmTalkSend(receiveUser, msg, loginUser);
		modelAndView.addObject("receiveUser", dmTalkSendBean.getReceiveUser());
		modelAndView.addObject("receiveUserName", dmTalkSendBean.getReceiveUserName());
		modelAndView.addObject("talkHistoryList", dmTalkSendBean.getTalkHistoryList());
		modelAndView.addObject("msg", "");
		modelAndView.addObject("isModalResult", false);

		modelAndView.setViewName("dm-talk");
		return modelAndView;
	}
}

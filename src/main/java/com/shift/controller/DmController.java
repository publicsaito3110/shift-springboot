package com.shift.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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


	@RequestMapping("/dm")
	public ModelAndView dm(ModelAndView modelAndView) {

		DmBean dmBean = this.dmService.dm();
		modelAndView.addObject("dmFinalHistoryList", dmBean.getDmFinalHistoryList());

		modelAndView.setViewName("dm");
		return modelAndView;
	}


	@RequestMapping(value = "/dm/address")
	public ModelAndView dmAddress(@RequestParam(value="keyword", required=false) String keyword, ModelAndView modelAndView) {

		DmAddressBean dmAddressBean = this.dmService.dmAddress(keyword);
		modelAndView.addObject("userList", dmAddressBean.getUserList());

		modelAndView.setViewName("dm-address");
		return modelAndView;
	}


	@RequestMapping(value = "/dm/talk", method = RequestMethod.POST)
	public ModelAndView dmTalk(@RequestParam(value="receiveUser") String receiveUser, ModelAndView modelAndView) {

		DmTalkBean dmTalkBean = this.dmService.dmTalk(receiveUser);
		modelAndView.addObject("receiveUser", dmTalkBean.getReceiveUser());
		modelAndView.addObject("receiveUserName", dmTalkBean.getReceiveUserName());
		modelAndView.addObject("talkHistoryList", dmTalkBean.getTalkHistoryList());
		modelAndView.addObject("msg", "");
		modelAndView.addObject("isModalResult", false);

		modelAndView.setViewName("dm-talk");
		return modelAndView;
	}


	@RequestMapping(value = "/dm/talk/send", method = RequestMethod.POST)
	public ModelAndView dmTalkSend(@RequestParam(value="receiveUser") String receiveUser, @RequestParam(value="msg") String msg, ModelAndView modelAndView) {

		//バリデーションエラーのとき
		ValidationSingleLogic validationSingleLogic = new ValidationSingleLogic(msg, Const.PATTERN_DM_MSG_ALL, "200文字以内のみ有効です");
		if (validationSingleLogic.isValidationEroor()) {

			DmTalkBean dmTalkBean = this.dmService.dmTalk(receiveUser);
			modelAndView.addObject("receiveUser", dmTalkBean.getReceiveUser());
			modelAndView.addObject("receiveUserName", dmTalkBean.getReceiveUserName());
			modelAndView.addObject("talkHistoryList", dmTalkBean.getTalkHistoryList());
			modelAndView.addObject("msg", msg);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultTitle", "メッセージ送信エラー");
			List<ValidationBean> validationBeanList = validationSingleLogic.getValidationResult();
			modelAndView.addObject("modalResultContentFail", validationBeanList.get(0).getErrorMessage());

			modelAndView.setViewName("dm-talk");
			return modelAndView;
		}

		DmTalkSendBean dmTalkSendBean = this.dmService.dmTalkSend(receiveUser, msg);
		modelAndView.addObject("receiveUser", dmTalkSendBean.getReceiveUser());
		modelAndView.addObject("receiveUserName", dmTalkSendBean.getReceiveUserName());
		modelAndView.addObject("talkHistoryList", dmTalkSendBean.getTalkHistoryList());
		modelAndView.addObject("msg", "");
		modelAndView.addObject("isModalResult", false);

		modelAndView.setViewName("dm-talk");
		return modelAndView;
	}
}

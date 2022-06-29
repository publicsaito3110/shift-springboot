package com.shift.domain.Interface;

import org.springframework.web.servlet.ModelAndView;

public interface DmTalkInterface extends BaseInterface {

	void getgetSessionByLoginUser(ModelAndView modelAndView);

	void getTalkHistoryByReceiveUser(ModelAndView modelAndView, String receiveUser);
}

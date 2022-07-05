package com.shift.domain.Interface;

import org.springframework.web.servlet.ModelAndView;

public interface DmInterface extends BaseInterface {

	void getUserIdBySession(ModelAndView modelAndView);

	void getDmHistory(ModelAndView modelAndView);
}

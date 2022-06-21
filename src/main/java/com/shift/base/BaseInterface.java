package com.shift.base;

import org.springframework.ui.Model;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

public interface BaseInterface {


	public default void executeEncoding(Model model) {

		ThymeleafViewResolver resolver = new ThymeleafViewResolver();

		//文字コードをUTF-8で設定
		resolver.setCharacterEncoding("UTF-8");
		resolver.setContentType("text/html;charset=UTF-8");
		model.addAttribute("base", "Base");
	}
}

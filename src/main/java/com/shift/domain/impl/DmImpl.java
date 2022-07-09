package com.shift.domain.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.Interface.DmInterface;
import com.shift.domain.model.dto.DmMenueDto;
import com.shift.domain.repository.DmMenueRepository;
import com.shift.domain.repository.DmRepository;

@Component
public class DmImpl extends BaseImple implements DmInterface {

	//フィールド
	private String id;

	@Autowired
	DmRepository dmRepository;

	@Autowired
	DmMenueRepository dmMenueRepository;


	@Override
	public void getUserIdBySession(ModelAndView modelAndView) {

		String id = "A001";
		this.id = id;
	}
	@Override
	public void getDmHistory(ModelAndView modelAndView) {

		List<DmMenueDto> dmHistoryList = new ArrayList<>();
		dmHistoryList = dmMenueRepository.selectDmTalkHistoryByLoginUser(this.id);

		//ログインしているユーザがメッセージを一度も送受信していないとき
		if (dmHistoryList.isEmpty()) {

			//dmHistoryListに結果を代入
			DmMenueDto bean = new DmMenueDto();
			bean.setMsg("メッセージはありません");
			dmHistoryList.add(bean);
		}

		// 引き渡す値を設定
		modelAndView.addObject("dmHistoryList", dmHistoryList);
	}
}

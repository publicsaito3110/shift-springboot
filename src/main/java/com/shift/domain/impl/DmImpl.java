package com.shift.domain.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.Interface.DmInterface;
import com.shift.domain.repositry.DmMenueRepositry;
import com.shift.domain.repositry.DmRepositry;
import com.shift.dto.DmMenueDto;

@Component
public class DmImpl implements DmInterface {

	//フィールド
	private String id;

	@Autowired
	DmRepositry dmRepositry;

	@Autowired
	DmMenueRepositry dmMenueRepositry;


	@Override
	public void getUserIdBySession(ModelAndView modelAndView) {

		String id = "A001";
		this.id = id;
	}
	@Override
	public void getDmHistory(ModelAndView modelAndView) {

		List<DmMenueDto> dmHistoryList = new ArrayList<>();
		dmHistoryList = dmMenueRepositry.selectDmTalkHistoryByLoginUser(this.id);

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

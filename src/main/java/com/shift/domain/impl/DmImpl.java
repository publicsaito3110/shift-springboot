package com.shift.domain.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.Interface.DmInterface;
import com.shift.domain.repositry.DmRepositry;
import com.shift.entity.DmEntity;

@Component
public class DmImpl implements DmInterface {

	//フィールド
	private String id;

	@Autowired
	DmRepositry dmRepositry;


	@Override
	public void getUserIdBySession(ModelAndView modelAndView) {

		String id = "A001";
		this.id = id;
	}
	@Override
	public void getDmHistory(ModelAndView modelAndView) {

		List<DmEntity> dmHistoryList = new ArrayList<>();
		dmHistoryList = dmRepositry.selectDmTalkHistoryByYm(this.id);

		//ログインしているユーザがメッセージを一度も送受信していないとき
		if (dmHistoryList.isEmpty()) {

			//chatListに結果を代入
			DmEntity bean = new DmEntity();
			bean.setMsg("メッセージはありません");
			dmHistoryList.add(bean);
		}

		// 引き渡す値を設定
		modelAndView.addObject("dmHistoryList", dmHistoryList);
	}
}

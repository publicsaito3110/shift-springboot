package com.shift.domain.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.shift.bean.NewsBean;
import com.shift.common.Const;
import com.shift.domain.Interface.HomeInterface;
import com.shift.domain.repositry.NewsRepositry;
import com.shift.entity.NewsEntity;

@Component
public class HomeImpl  extends BaseImple implements HomeInterface {

	//フィールド
	private String nowYmd;
	private LocalDate nowDateLd;
	private List<NewsEntity> newsDbList;

	@Autowired
	NewsRepositry newsRepositry;

	@Override
	public void getDate(ModelAndView modelAndView) {

		//現在の日付を取得
		LocalDate nowDateLd = LocalDate.now();
		int nowYear = nowDateLd.getYear();
		int nowMonth = nowDateLd.getMonthValue();
		int nowDay = nowDateLd.getDayOfMonth();
		String nowYmd = String.valueOf(nowYear) + String.format("%02d", nowMonth) + String.format("%02d", nowDay);
		this.nowYmd = nowYmd;
		this.nowDateLd = nowDateLd;
	}

	@Override
	public void getNews(ModelAndView modelAndView) {

		List<NewsEntity> newsDbList = new ArrayList<>();
		newsDbList = newsRepositry.selectNewsBeforeNowByNowYmdNewsLimit(this.nowYmd, Const.HOME_NEWS_SELECT_LIMIT);
		this.newsDbList = newsDbList;
	}

	@Override
	public void chengeDisplayNews(ModelAndView modelAndView) {

		//格納するための変数
		List<NewsBean> newsList = new ArrayList<>();

		//表示できるお知らせがないとき
		if (this.newsDbList.isEmpty()) {

			NewsBean newsbean = new NewsBean();
			newsbean.setYmd("お知らせはありません");
			newsList.add(newsbean);

			//引き渡す値を設定
			modelAndView.addObject("newsList", newsList);

			return;
		}


		//現在の日付からお知らせに表示する下限の日付を取得
		LocalDate limitDateLd = nowDateLd.minusDays(Const.HOME_NEWS_DISPLAY_LIMIT_DATE);

		//dbListの要素数の回数だけdbListから結果を抽出し、newsListにセットする
		for(NewsEntity newsEntity: this.newsDbList) {

			//ymdからLocalDate用(yyyy-mm-dd)に変換し、LocalDateで日付を取得
			String ymd = newsEntity.getYmd();
			String dateYmd = ymd.substring(0, 4) + "-" + ymd.substring(4, 6) + "-" + ymd.substring(6, 8);
			LocalDate newsDateLd = LocalDate.parse(dateYmd);

			//お知らせの日付(newsDateLd)が表示可能の下限の日付(limitDateLd)より後のとき
			if (newsDateLd.isAfter(limitDateLd)) {

				//newアイコンの表示
				NewsBean newsbean = new NewsBean(newsEntity);
				newsbean.setSrcPng(Const.HOME_NEWS_NEW_ICON_SRC);
				newsList.add(newsbean);

				continue;
			}

			newsList.add(new NewsBean(newsEntity));
		}

		//引き渡す値を設定
		modelAndView.addObject("newsList", newsList);
	}

}

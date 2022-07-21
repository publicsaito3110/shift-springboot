package com.shift.domain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CommonLogic;
import com.shift.common.Const;
import com.shift.domain.model.bean.HomeBean;
import com.shift.domain.model.bean.NewsBean;
import com.shift.domain.model.entity.NewsEntity;
import com.shift.domain.repository.NewsRepository;

/**
 * @author saito
 *
 */
@Service
public class HomeService extends BaseService {


	/**
	 * [Service] (/home)
	 *
	 * @param void
	 * @return DmTalkSendBean
	 */
	public HomeBean home() {

		this.getNowDate();
		this.selectNewsForNow();
		List<NewsBean> newsList = this.chengeDisplayNews();

		//Beanにセット
		HomeBean homeBean = new HomeBean(newsList);
		return homeBean;
	}

	@Autowired
	private NewsRepository newsRepository;


	//フィールド
	private String nowYmd;
	private LocalDate nowDateLd;
	private List<NewsEntity> newsDbList;


	/**
	 * 現在の日付取得処理
	 *
	 * <p>現在のを取得し、nowYmd(YYYMMDD), nowDateLd(LocalDate)で取得する</p>
	 *
	 * @param void
	 * @return void
	 */
	private void getNowDate() {

		//現在の日付をnowYmd(YYYYMMDD)とnowDateLd(LocalDate)を取得
		LocalDate nowDateLd = LocalDate.now();
		String nowYmd = new CommonLogic().getNowDateToYmd();
		this.nowYmd = nowYmd;
		this.nowDateLd = nowDateLd;
	}


	/**
	 * [DB]ニュース検索処理
	 *
	 * <p>ホーム画面に表示するニュースを取得する<br>
	 * 取得するお知らせは現在日を含む過去5件までとなる
	 * </p>
	 *
	 * @param void
	 * @return void
	 */
	private void selectNewsForNow() {

		List<NewsEntity> newsDbList = new ArrayList<>();
		newsDbList = newsRepository.selectNewsBeforeNowByNowYmdNewsLimit(this.nowYmd, Const.HOME_NEWS_SELECT_LIMIT);
		this.newsDbList = newsDbList;
	}


	/**
	 * 表示ニュース変換処理
	 *
	 * <p>ホーム画面に表示するニュースを変換する<br>
	 * 取得するお知らせは現在日を含む過去5件までとなる<br>
	 * ただし、表示できるニュースがないときはお知らせがないことを表示
	 * </p>
	 *
	 * @param void
	 * @return List<NewsBean>
	 */
	private List<NewsBean> chengeDisplayNews() {

		//表示できるお知らせがないとき
		if (this.newsDbList.isEmpty()) {
			return new ArrayList<>();
		}


		//現在の日付からお知らせにnew-iconを表示する下限の日付を取得
		LocalDate limitDateLd = nowDateLd.minusDays(Const.HOME_NEWS_DISPLAY_NEW_ICON_LIMIT_DATE);

		//お知らせを格納するための変数
		List<NewsBean> newsList = new ArrayList<>();

		//dbListの要素数の回数だけdbListから結果を抽出し、newsListにセットする
		for(NewsEntity newsEntity: this.newsDbList) {

			//ymdからLocalDate用(yyyy-mm-dd)に変換し、LocalDateで日付を取得
			String ymd = newsEntity.getYmd();
			String dateYmd = ymd.substring(0, 4) + "-" + ymd.substring(4, 6) + "-" + ymd.substring(6, 8);
			LocalDate newsDateLd = LocalDate.parse(dateYmd);

			//お知らせの日付(newsDateLd)が表示可能の下限の日付(limitDateLd)より後のとき
			if (newsDateLd.isAfter(limitDateLd)) {

				//newアイコンの表示する
				NewsBean newsbean = new NewsBean(newsEntity);
				newsbean.setSrcPngNewIcon(Const.HOME_NEWS_NEW_ICON_SRC);
				newsList.add(newsbean);

				continue;
			}

			newsList.add(new NewsBean(newsEntity));
		}

		return newsList;
	}
}

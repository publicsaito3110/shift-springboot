package com.shift.domain.service.common;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CommonLogic;
import com.shift.common.Const;
import com.shift.domain.model.bean.CmnNewsBean;
import com.shift.domain.model.bean.NewsBean;
import com.shift.domain.model.entity.NewsEntity;
import com.shift.domain.repository.NewsRepository;
import com.shift.domain.service.BaseService;

/**
 * @author saito
 *
 */
@Service
public class CmnNewsService extends BaseService {

	@Autowired
	private NewsRepository newsRepository;


	/**
	 * [共通Service]
	 *
	 * @param void
	 * @return CmnNewsBean
	 */
	public CmnNewsBean generateDisplayNowNews() {

		List<NewsEntity> newsDbList = selectNewsForNow();
		List<NewsBean> newsList = changeDisplayNews(newsDbList);

		//Beanにセット
		CmnNewsBean cmnNewsBean = new CmnNewsBean(newsList);
		return cmnNewsBean;
	}


	/**
	 * 表示ニュース変換処理
	 *
	 * <p>ホーム画面に表示するニュースを変換する<br>
	 * 取得するお知らせは現在日を含む過去5件までとなる<br>
	 * また、現在日から14日以内のお知らせはnewアイコンが表示される
	 * ただし、表示できるニュースがないときはEmptyになる
	 * </p>
	 *
	 * @param void
	 * @return List<NewsBean><br>
	 * フィールド(List&lt;NewsBean&gt;)<br>
	 * id, ymd, category, title, content, srcPngNewIcon
	 */
	private List<NewsBean> changeDisplayNews(List<NewsEntity> newsDbList) {

		//表示できるお知らせがないとき
		if (newsDbList.isEmpty()) {
			return new ArrayList<>();
		}

		//現在の日付のLocalDateを取得
		LocalDate nowLd = LocalDate.now();

		//現在の日付からお知らせにnew-iconを表示する下限の日付を取得
		LocalDate limitDateLd = nowLd.minusDays(Const.HOME_NEWS_DISPLAY_NEW_ICON_LIMIT_DATE);

		//お知らせを格納するための変数
		List<NewsBean> newsList = new ArrayList<>();
		CommonLogic commonLogic = new CommonLogic();

		//dbListの要素数の回数だけdbListから結果を抽出し、newsListにセットする
		for(NewsEntity newsEntity: newsDbList) {

			//ymdをLocalDateで取得
			String ymd = newsEntity.getYmd();
			LocalDate newsDateLd = commonLogic.getLocalDateByYmd(ymd);

			//お知らせの日付(newsDateLd)が表示可能の下限の日付(limitDateLd)より後のとき
			if (newsDateLd.isAfter(limitDateLd)) {

				//newアイコンの表示する
				NewsBean newsBean = new NewsBean(newsEntity);
				newsBean.setSrcPngNewIcon(Const.HOME_NEWS_NEW_ICON_SRC);
				newsList.add(newsBean);
				continue;
			}

			newsList.add(new NewsBean(newsEntity));
		}

		return newsList;
	}


	/**
	 * [DB]ニュース検索処理
	 *
	 * <p>ホーム画面に表示するニュースを取得する<br>
	 * 取得するお知らせは現在日を含む過去5件までとなる<br>
	 * ただし、表示可能なお知らせがないときはEmptyとなる
	 * </p>
	 *
	 * @param void
	 * @return List<NewsEntity><br>
	 * フィールド(List&lt;NewsEntity&gt;)<br>
	 * id, ymd, category, title, content
	 */
	private List<NewsEntity> selectNewsForNow() {

		//現在のymdを取得
		String nowYmd = new CommonLogic().getNowDateToYmd();

		List<NewsEntity> newsDbList = newsRepository.selectNewsBeforeNowByNowYmdNewsLimit(nowYmd, Const.HOME_NEWS_SELECT_LIMIT);
		return newsDbList;
	}
}

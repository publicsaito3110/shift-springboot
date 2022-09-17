package com.shift.domain.model.bean;

import com.shift.common.CommonLogic;
import com.shift.common.Const;
import com.shift.domain.model.entity.NewsEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saito
 *
 */
@Data
@NoArgsConstructor
public class NewsBean {

	//フィールド
	private String id;

	private String ymd;

	private String category;

	private String title;

	private String content;

	private String srcPngNewIcon;


	//コンストラクタ
	public NewsBean(NewsEntity newsEntity) {
		id = newsEntity.getId();
		ymd = newsEntity.getYmd();
		category = newsEntity.getCategory();
		title = newsEntity.getTitle();
		content = newsEntity.getContent();
	}


	//メソッド
	public String ymdFormatDate() {

		//日付形式(mm/dd)返す
		return ymd.substring(4, 6) + "/" + ymd.substring(6, 8);
	}


	public String categoryFormatPngSrc() {

		//category-icon.pngのsrcを返す
		return Const.HOME_NEWS_CATEGORY_ICON_SRC +  category + ".png";
	}


	public String contentAfterBreakLine() {

		//改行対応
		return new CommonLogic().changeAfterBreakLine(content);
	}
}

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
		this.id = newsEntity.getId();
		this.ymd = newsEntity.getYmd();
		this.category = newsEntity.getCategory();
		this.title = newsEntity.getTitle();
		this.content = newsEntity.getContent();
	}


	//メソッド
	public String ymdFormatDate() {

		//日付形式(mm/dd)返す
		return this.ymd.substring(4, 6) + "/" + this.ymd.substring(6, 8);
	}


	public String categoryFormatPngSrc() {

		//category-icon.pngのsrcを返す
		return Const.HOME_NEWS_CATEGORY_ICON_SRC +  this.category + ".png";
	}


	public String contentAfterBreakLine() {

		//改行対応
		CommonLogic commonLogic = new CommonLogic();
		return commonLogic.changeAfterBreakLine(this.content);
	}
}

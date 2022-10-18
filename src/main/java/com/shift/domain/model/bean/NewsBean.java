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
	private Integer id;

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



	/**
	 * 日付フォーマット変換処理
	 *
	 * <p>日付フォーマット(MM/DD)に変換</p>
	 *
	 * @param void
	 * @return String 日付フォーマット(MM/DD)
	 */
	public String ymdFormatDate() {

		//日付形式(mm/dd)に変換して返す
		return ymd.substring(4, 6) + "/" + ymd.substring(6, 8);
	}


	/**
	 * カテゴリーアイコンSRC変換処理
	 *
	 * <p>カテゴリーアイコンのパスを取得する</p>
	 *
	 * @param void
	 * @return String アイコンのパス(img)
	 */
	public String categoryFormatPngSrc() {

		//category-icon.pngのsrcを返す
		return Const.HOME_NEWS_CATEGORY_ICON_SRC +  category + ".png";
	}


	/**
	 * 改行処理
	 *
	 * <p>お知らせ内容に改行コードがあるとき改行タグに返還する</p>
	 *
	 * @param void
	 * @return String 改行済みのお知らせ内容
	 */
	public String contentAfterBreakLine() {

		//改行対応
		return new CommonLogic().changeAfterBreakLine(content);
	}
}

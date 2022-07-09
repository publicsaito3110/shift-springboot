package com.shift.domain.model.bean;

import com.shift.domain.model.entity.NewsEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saito
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsBean {

	//フィールド
	private String id;
	private String ymd;
	private String category;
	private String title;
	private String content;

	private String srcPng;


	//コンストラクタ―

	public NewsBean(NewsEntity newsEntity) {
		this.id = newsEntity.getId();
		this.ymd = newsEntity.getYmd();
		this.category = newsEntity.getCategory();
		this.title = newsEntity.getTitle();
		this.content = newsEntity.getContent();
	}
}

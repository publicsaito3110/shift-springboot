package com.shift.bean;

import com.shift.entity.NewsEntity;

import lombok.Data;

@Data
public class NewsBean {

	//フィールド
	private String id;
	private String ymd;
	private String category;
	private String title;
	private String content;

	private String srcPng;


	//コンストラクタ―
	public NewsBean() {}

	public NewsBean(NewsEntity newsEntity) {
		this.id = newsEntity.getId();
		this.ymd = newsEntity.getYmd();
		this.category = newsEntity.getCategory();
		this.title = newsEntity.getTitle();
		this.content = newsEntity.getContent();
	}
}

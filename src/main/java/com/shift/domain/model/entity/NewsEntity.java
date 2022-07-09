package com.shift.domain.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author saito
 *
 */
@Entity
@Table(name="news")
@Data
public class NewsEntity {

	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id;

	@Column(name = "ymd")
	private String ymd;

	@Column(name = "category")
	private String category;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;
}

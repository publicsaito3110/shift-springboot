package com.shift.domain.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author saito
 *
 */
@Entity
@Table(name="news")
@Data
@EqualsAndHashCode(callSuper = true)
public class NewsEntity extends BaseEntity {

	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "ymd")
	private String ymd;

	@Column(name = "category")
	private String category;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;
}

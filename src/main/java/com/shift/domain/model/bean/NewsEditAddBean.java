package com.shift.domain.model.bean;

import java.util.List;

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
public class NewsEditAddBean {

	private List<NewsBean> newsList;

	private List<NewsBean> newsRecordList;

	private String newsRecordableMaxDate;

	private String newsRecordableMinDate;
}

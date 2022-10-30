package com.shift.domain.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author saito
 *
 */
@Entity
@Data
public class ScheduleYearDto {


	//フィールド
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "user_name", unique = true)
	private String userName;

	@Column(name = "day1", unique = true)
	private String day1;

	@Column(name = "day2", unique = true)
	private String day2;

	@Column(name = "day3", unique = true)
	private String day3;

	@Column(name = "day4", unique = true)
	private String day4;

	@Column(name = "day5", unique = true)
	private String day5;

	@Column(name = "day6", unique = true)
	private String day6;

	@Column(name = "day7", unique = true)
	private String day7;

	@Column(name = "day8", unique = true)
	private String day8;

	@Column(name = "day9", unique = true)
	private String day9;

	@Column(name = "day10", unique = true)
	private String day10;

	@Column(name = "day11", unique = true)
	private String day11;

	@Column(name = "day12", unique = true)
	private String day12;

	@Column(name = "day13", unique = true)
	private String day13;

	@Column(name = "day14", unique = true)
	private String day14;

	@Column(name = "day15", unique = true)
	private String day15;

	@Column(name = "day16", unique = true)
	private String day16;

	@Column(name = "day17", unique = true)
	private String day17;

	@Column(name = "day18", unique = true)
	private String day18;

	@Column(name = "day19", unique = true)
	private String day19;

	@Column(name = "day20", unique = true)
	private String day20;

	@Column(name = "day21", unique = true)
	private String day21;

	@Column(name = "day22", unique = true)
	private String day22;

	@Column(name = "day23", unique = true)
	private String day23;

	@Column(name = "day24", unique = true)
	private String day24;

	@Column(name = "day25", unique = true)
	private String day25;

	@Column(name = "day26", unique = true)
	private String day26;

	@Column(name = "day27", unique = true)
	private String day27;

	@Column(name = "day28", unique = true)
	private String day28;

	@Column(name = "day29", unique = true)
	private String day29;

	@Column(name = "day30", unique = true)
	private String day30;

	@Column(name = "day31", unique = true)
	private String day31;


	//メソッド
	public List<String> getDayList() {

		//日付のフィールドをListで全て取得
		List<String> dayList = new ArrayList<>();
		dayList.add(day1);
		dayList.add(day2);
		dayList.add(day3);
		dayList.add(day4);
		dayList.add(day5);
		dayList.add(day6);
		dayList.add(day7);
		dayList.add(day8);
		dayList.add(day9);
		dayList.add(day10);
		dayList.add(day11);
		dayList.add(day12);
		dayList.add(day13);
		dayList.add(day14);
		dayList.add(day15);
		dayList.add(day16);
		dayList.add(day17);
		dayList.add(day18);
		dayList.add(day19);
		dayList.add(day20);
		dayList.add(day21);
		dayList.add(day22);
		dayList.add(day23);
		dayList.add(day24);
		dayList.add(day25);
		dayList.add(day26);
		dayList.add(day27);
		dayList.add(day28);
		dayList.add(day29);
		dayList.add(day30);
		dayList.add(day31);

		return dayList;
	}
}

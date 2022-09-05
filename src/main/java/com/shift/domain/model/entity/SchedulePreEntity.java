package com.shift.domain.model.entity;

import java.util.ArrayList;
import java.util.List;

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
@Table(name="schedule_pre")
@Data
@EqualsAndHashCode(callSuper = true)
public class SchedulePreEntity extends BaseEntity {

	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "ym")
	private String ym;

	@Column(name = "user")
	private String user;

	@Column(name = "1")
	private String day1;

	@Column(name = "2")
	private String day2;

	@Column(name = "3")
	private String day3;

	@Column(name = "4")
	private String day4;

	@Column(name = "5")
	private String day5;

	@Column(name = "6")
	private String day6;

	@Column(name = "7")
	private String day7;

	@Column(name = "8")
	private String day8;

	@Column(name = "9")
	private String day9;

	@Column(name = "10")
	private String day10;

	@Column(name = "11")
	private String day11;

	@Column(name = "12")
	private String day12;

	@Column(name = "13")
	private String day13;

	@Column(name = "14")
	private String day14;

	@Column(name = "15")
	private String day15;

	@Column(name = "16")
	private String day16;

	@Column(name = "17")
	private String day17;

	@Column(name = "18")
	private String day18;

	@Column(name = "19")
	private String day19;

	@Column(name = "20")
	private String day20;

	@Column(name = "21")
	private String day21;

	@Column(name = "22")
	private String day22;

	@Column(name = "23")
	private String day23;

	@Column(name = "24")
	private String day24;

	@Column(name = "25")
	private String day25;

	@Column(name = "26")
	private String day26;

	@Column(name = "27")
	private String day27;

	@Column(name = "28")
	private String day28;

	@Column(name = "29")
	private String day29;

	@Column(name = "30")
	private String day30;

	@Column(name = "31")
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

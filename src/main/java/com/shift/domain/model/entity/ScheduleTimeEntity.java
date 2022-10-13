package com.shift.domain.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.shift.common.CommonUtil;
import com.shift.domain.model.bean.ScheduleTimeBean;
import com.shift.form.ShiftEditAddForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author saito
 *
 */
@Entity
@Table(name="schedule_time")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ScheduleTimeEntity extends BaseEntity {

	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "end_ymd")
	private String endYmd;

	@Column(name = "name1")
	private String name1;

	@Column(name = "start_hm1")
	private String startHm1;

	@Column(name = "end_hm1")
	private String endHm1;

	@Column(name = "rest_hm1")
	private String restHm1;

	@Column(name = "name2")
	private String name2;

	@Column(name = "start_hm2")
	private String startHm2;

	@Column(name = "end_hm2")
	private String endHm2;

	@Column(name = "rest_hm2")
	private String restHm2;

	@Column(name = "name3")
	private String name3;

	@Column(name = "start_hm3")
	private String startHm3;

	@Column(name = "end_hm3")
	private String endHm3;

	@Column(name = "rest_hm3")
	private String restHm3;

	@Column(name = "name4")
	private String name4;

	@Column(name = "start_hm4")
	private String startHm4;

	@Column(name = "end_hm4")
	private String endHm4;

	@Column(name = "rest_hm4")
	private String restHm4;

	@Column(name = "name5")
	private String name5;

	@Column(name = "start_hm5")
	private String startHm5;

	@Column(name = "end_hm5")
	private String endHm5;

	@Column(name = "rest_hm5")
	private String restHm5;

	@Column(name = "name6")
	private String name6;

	@Column(name = "start_hm6")
	private String startHm6;

	@Column(name = "end_hm6")
	private String endHm6;

	@Column(name = "rest_hm6")
	private String restHm6;

	@Column(name = "name7")
	private String name7;

	@Column(name = "start_hm7")
	private String startHm7;

	@Column(name = "end_hm7")
	private String endHm7;

	@Column(name = "rest_hm7")
	private String restHm7;



	/**
	 * [コンストラクタ] ShiftEditAddForm
	 *
	 * <p>endYmd, shiftEditModifyFormから値をセットする</p>
	 *
	 * @param shiftEditAddForm ShiftEditAddForm
	 * @param endYmd 更新したい最終日付(YYYYMMDD)
	 */
	public ScheduleTimeEntity(ShiftEditAddForm shiftEditAddForm, String endYmd) {

		//endYmdをセット
		this.endYmd = endYmd;

		//name, startHm, endHm, restHmをそれぞれhm(HHMM)でセット
		name1 = shiftEditAddForm.getName1();
		startHm1 = shiftEditAddForm.getStartHm1().replaceAll(":", "");
		endHm1 = shiftEditAddForm.getEndHm1().replaceAll(":", "");
		restHm1 =  shiftEditAddForm.getRestHm1().replaceAll(":", "");
		name2 = shiftEditAddForm.getName2();
		startHm2 = shiftEditAddForm.getStartHm2().replaceAll(":", "");
		endHm2 = shiftEditAddForm.getEndHm2().replaceAll(":", "");
		restHm2 =  shiftEditAddForm.getRestHm2().replaceAll(":", "");
		name3 = shiftEditAddForm.getName3();
		startHm3 = shiftEditAddForm.getStartHm3().replaceAll(":", "");
		endHm3 = shiftEditAddForm.getEndHm3().replaceAll(":", "");
		restHm3 =  shiftEditAddForm.getRestHm3().replaceAll(":", "");
		name4 = shiftEditAddForm.getName4();
		startHm4 = shiftEditAddForm.getStartHm4().replaceAll(":", "");
		endHm4 = shiftEditAddForm.getEndHm4().replaceAll(":", "");
		restHm4 =  shiftEditAddForm.getRestHm4().replaceAll(":", "");
		name5 = shiftEditAddForm.getName5();
		startHm5 = shiftEditAddForm.getStartHm5().replaceAll(":", "");
		endHm5 = shiftEditAddForm.getEndHm5().replaceAll(":", "");
		restHm5 =  shiftEditAddForm.getRestHm5().replaceAll(":", "");
		name6 = shiftEditAddForm.getName6();
		startHm6 = shiftEditAddForm.getStartHm6().replaceAll(":", "");
		endHm6 = shiftEditAddForm.getEndHm6().replaceAll(":", "");
		restHm6 =  shiftEditAddForm.getRestHm6().replaceAll(":", "");
		name7 = shiftEditAddForm.getName7();
		startHm7 = shiftEditAddForm.getStartHm7().replaceAll(":", "");
		endHm7 = shiftEditAddForm.getEndHm7().replaceAll(":", "");
		restHm7 =  shiftEditAddForm.getRestHm7().replaceAll(":", "");
	}



	/**
	 * [メソッド]スケジュール時間区分List取得処理
	 *
	 * <p>スケジュール時間区分をList取得する<br>
	 * ただし、スケジュール時間区分が登録されていない場合はListに格納されない
	 * </p>
	 *
	 * @param void
	 * @return List<ScheduleTimeBean> <br>
	 * フィールド(List&lt;ScheduleTimeBean&gt;)<br>
	 * name, startHm, endHm, restHm
	 *
	 */
	public List<ScheduleTimeBean> scheduleTimeFormatList() {

		//スケジュール時間区分を格納するためのList
		List<ScheduleTimeBean> ScheduleTimeBeanList = new ArrayList<>();

		//スケジュール時間区分1が登録されているとき、ScheduleTimeBeanListに格納する
		if (!CommonUtil.changeEmptyByNull(name1).equals("") && !CommonUtil.changeEmptyByNull(startHm1).equals("") && !CommonUtil.changeEmptyByNull(endHm1).equals("") && !CommonUtil.changeEmptyByNull(restHm1).equals("")) {
			ScheduleTimeBean ScheduleTimeBean = new ScheduleTimeBean(name1, startHm1, endHm1, restHm1);
			ScheduleTimeBeanList.add(ScheduleTimeBean);
		}

		//スケジュール時間区分2が登録されているとき、ScheduleTimeBeanListに格納する
		if (!CommonUtil.changeEmptyByNull(name2).equals("") && !CommonUtil.changeEmptyByNull(startHm2).equals("") && !CommonUtil.changeEmptyByNull(endHm2).equals("") && !CommonUtil.changeEmptyByNull(restHm2).equals("")) {
			ScheduleTimeBean ScheduleTimeBean = new ScheduleTimeBean(name2, startHm2, endHm2, restHm2);
			ScheduleTimeBeanList.add(ScheduleTimeBean);
		}

		//スケジュール時間区分3が登録されているとき、ScheduleTimeBeanListに格納する
		if (!CommonUtil.changeEmptyByNull(name3).equals("") && !CommonUtil.changeEmptyByNull(startHm3).equals("") && !CommonUtil.changeEmptyByNull(endHm3).equals("") && !CommonUtil.changeEmptyByNull(restHm3).equals("")) {
			ScheduleTimeBean ScheduleTimeBean = new ScheduleTimeBean(name3, startHm3, endHm3, restHm3);
			ScheduleTimeBeanList.add(ScheduleTimeBean);
		}

		//スケジュール時間区分4が登録されているとき、ScheduleTimeBeanListに格納する
		if (!CommonUtil.changeEmptyByNull(name4).equals("") && !CommonUtil.changeEmptyByNull(startHm4).equals("") && !CommonUtil.changeEmptyByNull(endHm4).equals("") && !CommonUtil.changeEmptyByNull(restHm4).equals("")) {
			ScheduleTimeBean ScheduleTimeBean = new ScheduleTimeBean(name4, startHm4, endHm4, restHm4);
			ScheduleTimeBeanList.add(ScheduleTimeBean);
		}

		//スケジュール時間区分5が登録されているとき、ScheduleTimeBeanListに格納する
		if (!CommonUtil.changeEmptyByNull(name5).equals("") && !CommonUtil.changeEmptyByNull(startHm5).equals("") && !CommonUtil.changeEmptyByNull(endHm5).equals("") && !CommonUtil.changeEmptyByNull(restHm5).equals("")) {
			ScheduleTimeBean ScheduleTimeBean = new ScheduleTimeBean(name5, startHm5, endHm5, restHm5);
			ScheduleTimeBeanList.add(ScheduleTimeBean);
		}

		//スケジュール時間区分6が登録されているとき、ScheduleTimeBeanListに格納する
		if (!CommonUtil.changeEmptyByNull(name6).equals("") && !CommonUtil.changeEmptyByNull(startHm6).equals("") && !CommonUtil.changeEmptyByNull(endHm6).equals("") && !CommonUtil.changeEmptyByNull(restHm6).equals("")) {
			ScheduleTimeBean ScheduleTimeBean = new ScheduleTimeBean(name6, startHm6, endHm6, restHm6);
			ScheduleTimeBeanList.add(ScheduleTimeBean);
		}

		//スケジュール時間区分7が登録されているとき、ScheduleTimeBeanListに格納する
		if (!CommonUtil.changeEmptyByNull(name7).equals("") && !CommonUtil.changeEmptyByNull(startHm7).equals("") && !CommonUtil.changeEmptyByNull(endHm7).equals("") && !CommonUtil.changeEmptyByNull(restHm7).equals("")) {
			ScheduleTimeBean ScheduleTimeBean = new ScheduleTimeBean(name7, startHm7, endHm7, restHm7);
			ScheduleTimeBeanList.add(ScheduleTimeBean);
		}

		return ScheduleTimeBeanList;
	}
}

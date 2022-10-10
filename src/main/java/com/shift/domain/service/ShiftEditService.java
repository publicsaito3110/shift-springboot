package com.shift.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CommonLogic;
import com.shift.domain.model.bean.ShiftEditBean;
import com.shift.domain.model.entity.ScheduleTimeEntity;
import com.shift.domain.repository.ScheduleTimeRepository;

/**
 * @author saito
 *
 */
@Service
public class ShiftEditService extends BaseService {

	@Autowired
	private ScheduleTimeRepository scheduleTimeRepository;


	/**
	 * [Service] (/shift-edit)
	 *
	 * @return ScheduleBean
	 */
	public ShiftEditBean shiftEdit() {

		//現在の日付をymdで取得
		String nowYmd = new CommonLogic().getNowDateToYmd();
		//スケジュール時間区分を取得
		ScheduleTimeEntity scheduleTimeEntity = selectScheduleTime(nowYmd);

		//Beanにセット
		ShiftEditBean shiftEditBean = new ShiftEditBean(scheduleTimeEntity);
		return shiftEditBean;
	}


	/**
	 * [DB]スケジュール時間区分取得処理
	 *
	 * <p>取得したい日付(ymd)から該当するスケジュール時間区分を取得する<br>
	 * また、現在日(ymd)に該当するスケジュール時間区分が複数登録されているときは最新のスケジュール時間区分が取得される<br>
	 * ただし、スケジュール時間区分が何も登録されていないときはnullとなる
	 * </p>
	 *
	 * @param ymd 取得したいスケジュール時間区分の日付(YYYYMMDD)
	 * @return ScheduleTimeEntity<br>
	 * フィールド(ScheduleTimeEntity)<br>
	 * id, endYmd, name1, startHm1, endHM1, restHm1... startHm7, endHM7, restHm7
	 */
	private ScheduleTimeEntity selectScheduleTime(String ymd) {

		ScheduleTimeEntity scheduleTimeEntity = scheduleTimeRepository.selectScheduleTimeByYmd(ymd);
		return scheduleTimeEntity;
	}
}

package com.shift.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

		List<ScheduleTimeEntity> scheduleTimeEntityList = selectScheduleTime();

		//Beanにセット
		ShiftEditBean shiftEditBean = new ShiftEditBean(scheduleTimeEntityList);
		return shiftEditBean;
	}


	/**
	 * [DB]シフト時間取得処理
	 *
	 * <p>管理者が設定したシフト時間を取得する</p>
	 *
	 * @return List<ScheduleTimeEntity> <br>
	 * フィールド(List&lt;ScheduleTimeEntity&gt;)<br>
	 * id, name, startHms, endHms, restHms
	 *
	 */
	private List<ScheduleTimeEntity> selectScheduleTime() {

		List<ScheduleTimeEntity> scheduleTimeEntityList = scheduleTimeRepository.findAll();
		return scheduleTimeEntityList;
	}
}

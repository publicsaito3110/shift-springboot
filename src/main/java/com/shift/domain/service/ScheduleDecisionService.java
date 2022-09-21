package com.shift.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.domain.model.bean.CmnScheduleBean;
import com.shift.domain.model.bean.ScheduleDecisionBean;
import com.shift.domain.model.entity.ScheduleTimeEntity;
import com.shift.domain.repository.ScheduleTimeRepository;
import com.shift.domain.service.common.CmnScheduleService;

/**
 * @author saito
 *
 */
@Service
public class ScheduleDecisionService extends BaseService {

	@Autowired
	private ScheduleTimeRepository scheduleTimeRepository;

	@Autowired
	private CmnScheduleService cmnScheduleService;


	/**
	 * [Service] (/schedule-decision)
	 *
	 * @param ym RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return ScheduleDecisionBean
	 */
	public ScheduleDecisionBean scheduleDecision(String ym, String loginUser) {

		//CmnScheduleService(共通サービス)から処理結果を取得
		CmnScheduleBean cmnScheduleBean = cmnScheduleService.generateCalendarYmByYm(ym);
		//Service内の処理を実行
		List<ScheduleTimeEntity> scheduleTimeList = selectScheduleTime();

		//Beanにセット
		ScheduleDecisionBean scheduleDecisionBean = new ScheduleDecisionBean();
		scheduleDecisionBean.setYear(cmnScheduleBean.getYear());
		scheduleDecisionBean.setMonth(cmnScheduleBean.getMonth());
		scheduleDecisionBean.setCalendarList(cmnScheduleBean.getCalendarList());
		scheduleDecisionBean.setAfterYm(cmnScheduleBean.getNextYm());
		scheduleDecisionBean.setBeforeYm(cmnScheduleBean.getBeforeYm());
		scheduleDecisionBean.setScheduleTimeList(scheduleTimeList);
		return scheduleDecisionBean;
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

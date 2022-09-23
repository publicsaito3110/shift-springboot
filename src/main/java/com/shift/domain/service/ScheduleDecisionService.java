package com.shift.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.Const;
import com.shift.domain.model.bean.CmnScheduleBean;
import com.shift.domain.model.bean.ScheduleDecisionBean;
import com.shift.domain.model.bean.ScheduleDecisionModifyBean;
import com.shift.domain.model.dto.ScheduleUserDto;
import com.shift.domain.model.entity.ScheduleTimeEntity;
import com.shift.domain.repository.ScheduleTimeRepository;
import com.shift.domain.repository.ScheduleUserRepository;
import com.shift.domain.service.common.CmnScheduleService;

/**
 * @author saito
 *
 */
@Service
public class ScheduleDecisionService extends BaseService {

	@Autowired
	private ScheduleUserRepository scheduleUserRepository;

	@Autowired
	private ScheduleTimeRepository scheduleTimeRepository;

	@Autowired
	private CmnScheduleService cmnScheduleService;


	/**
	 * [Service] (/schedule-decision)
	 *
	 * @param ym RequestParameter
	 * @return ScheduleDecisionBean
	 */
	public ScheduleDecisionBean scheduleDecision(String ym) {

		//CmnScheduleService(共通サービス)から処理結果を取得
		CmnScheduleBean cmnScheduleBean = cmnScheduleService.generateCalendarYmByYm(ym);
		//Service内の処理を実行
		List<ScheduleTimeEntity> scheduleTimeList = selectScheduleTime();

		//Beanにセット
		ScheduleDecisionBean scheduleDecisionBean = new ScheduleDecisionBean();
		scheduleDecisionBean.setYear(cmnScheduleBean.getYear());
		scheduleDecisionBean.setMonth(cmnScheduleBean.getMonth());
		scheduleDecisionBean.setCalendarList(cmnScheduleBean.getCalendarList());
		scheduleDecisionBean.setNowYm(cmnScheduleBean.getNowYm());
		scheduleDecisionBean.setAfterYm(cmnScheduleBean.getNextYm());
		scheduleDecisionBean.setBeforeYm(cmnScheduleBean.getBeforeYm());
		scheduleDecisionBean.setScheduleTimeList(scheduleTimeList);
		return scheduleDecisionBean;
	}


	/**
	 * [Service] (/schedule-decision/modify)
	 *
	 * @param ym RequestParameter
	 * @param day RequestParameter
	 * @return ScheduleDecisionBean
	 */
	public ScheduleDecisionModifyBean scheduleDecisionModify(String ym, String day) {

		//Service
		List<ScheduleUserDto> scheduleUserList = selectScheduleDay(ym, day);
		List<ScheduleTimeEntity> scheduleTimeList = selectScheduleTime();

		//Beanにセット
		ScheduleDecisionModifyBean scheduleDecisionModifyBean = new ScheduleDecisionModifyBean(scheduleUserList, scheduleTimeList);
		return scheduleDecisionModifyBean;
	}


	/**
	 * [DB]シフト時間取得処理
	 *
	 * <p>管理者が設定したシフト時間を取得する</p>
	 *
	 * @param void
	 * @return List<ScheduleTimeEntity> <br>
	 * フィールド(List&lt;ScheduleTimeEntity&gt;)<br>
	 * id, name, startHms, endHms, restHms
	 *
	 */
	private List<ScheduleTimeEntity> selectScheduleTime() {

		List<ScheduleTimeEntity> scheduleTimeEntityList = scheduleTimeRepository.findAll();
		return scheduleTimeEntityList;
	}


	/**
	 * [DB]確定スケジュール取得処理
	 *
	 * <p>ym及びdayに該当する確定スケジュールをユーザごとに取得する<br>
	 * ym=200001,day=1のとき2000年1月1日, ym=200001,day=2のとき2000年1月2日...<br>
	 * ただし、登録済みのスケジュールが1つもないまたは存在しない日付のときはEmptyとなる
	 * </p>
	 *
	 * @param ym 検索したい年月(YYYYMM)
	 * @param day 検索したい日付(1日 -> 1, 2日 -> 2...)
	 * @return List<ScheduleUserDto><br>
	 * フィールド(&lt;ScheduleUserDto&gt;)<br>
	 * id, user, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7
	 *
	 */
	private List<ScheduleUserDto> selectScheduleDay(String ym, String day) {

		//検索したい文字と文字列目を指定するためにフォーマットを"%1______%"に整える
		String schedule1 = Const.CHARACTER_PERCENT + Const.SCHEDULE_PRE_DAY_RECORDED + "______" + Const.CHARACTER_PERCENT;
		String schedule2 = Const.CHARACTER_PERCENT + "_" + Const.SCHEDULE_PRE_DAY_RECORDED + "_____" + Const.CHARACTER_PERCENT;
		String schedule3 = Const.CHARACTER_PERCENT + "__" + Const.SCHEDULE_PRE_DAY_RECORDED + "____" + Const.CHARACTER_PERCENT;
		String schedule4 = Const.CHARACTER_PERCENT + "___" + Const.SCHEDULE_PRE_DAY_RECORDED + "___" + Const.CHARACTER_PERCENT;
		String schedule5 = Const.CHARACTER_PERCENT + "____" + Const.SCHEDULE_PRE_DAY_RECORDED + "__" + Const.CHARACTER_PERCENT;
		String schedule6 = Const.CHARACTER_PERCENT + "_____" + Const.SCHEDULE_PRE_DAY_RECORDED + "_" + Const.CHARACTER_PERCENT;
		String schedule7 = Const.CHARACTER_PERCENT + "______" + Const.SCHEDULE_PRE_DAY_RECORDED + Const.CHARACTER_PERCENT;

		List<ScheduleUserDto> scheduleDayDtoList = new ArrayList<>();

		//dayの値に応じて取得するスケジュールの日付を変える
		if ("1".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay1BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("2".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay2BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("3".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay3BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("4".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay4BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("5".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay5BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("6".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay6BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("7".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay7BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("8".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay8BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("9".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay9BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("10".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay10BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("11".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay11BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("12".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay12BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("13".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay13BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("14".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay14BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("15".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay15BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("16".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay16BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("17".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay17BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("18".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay18BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("19".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay19BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("20".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay20BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("21".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay21BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("22".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay22BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("23".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay23BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("24".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay24BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("25".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay25BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("26".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay26BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("27".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay27BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("28".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay28BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("29".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay29BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("30".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay30BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("31".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay31BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		}

		return scheduleDayDtoList;
	}
}

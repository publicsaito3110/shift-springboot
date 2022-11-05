package com.shift.domain.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shift.common.CommonLogic;
import com.shift.common.Const;
import com.shift.domain.model.bean.CmnScheduleCalendarBean;
import com.shift.domain.model.bean.ShiftEditAddBean;
import com.shift.domain.model.bean.ShiftEditBean;
import com.shift.domain.model.entity.ScheduleTimeEntity;
import com.shift.domain.repository.ScheduleTimeRepository;
import com.shift.domain.service.common.CmnScheduleCalendarService;
import com.shift.form.ShiftEditAddForm;

/**
 * @author saito
 *
 */
@Service
@Transactional
public class ShiftEditService extends BaseService {

	@Autowired
	private ScheduleTimeRepository scheduleTimeRepository;

	@Autowired
	private CmnScheduleCalendarService cmnScheduleCalendarService;



	/**
	 * スケジュール時間確認画面取得機能<br>
	 * [Service] (/shift-edit)
	 *
	 * @param ym RequestParameter 取得したいスケジュールの年月(YYYYMM)
	 * @return ShiftEditBean
	 */
	public ShiftEditBean shiftEdit(String ym) {

		//CmnScheduleCalendarServiceからカレンダー, 年月, 最終日を取得
		CmnScheduleCalendarBean cmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendarYmByYm(ym);
		//スケジュール時間区分を取得
		ScheduleTimeEntity scheduleTimeEntity = selectScheduleTime(cmnScheduleCalendarBean.getLastDateYmd());

		//Beanにセット
		ShiftEditBean shiftEditBean = new ShiftEditBean();
		shiftEditBean.setYear(cmnScheduleCalendarBean.getYear());
		shiftEditBean.setMonth(cmnScheduleCalendarBean.getMonth());
		shiftEditBean.setNowYm(cmnScheduleCalendarBean.getNowYm());
		shiftEditBean.setNextYm(cmnScheduleCalendarBean.getNextYm());
		shiftEditBean.setBeforeYm(cmnScheduleCalendarBean.getBeforeYm());
		shiftEditBean.setScheduleTimeEntity(scheduleTimeEntity);
		return shiftEditBean;
	}


	/**
	 * スケジュール時間修正機能<br>
	 * [Service] (/shift-edit/add)
	 *
	 * @param shiftEditAddForm RequestParam Form
	 * @return ShiftEditAddBean
	 */
	public ShiftEditAddBean shiftEditAdd(ShiftEditAddForm shiftEditAddForm) {

		//新規スケジュール時間区分を追加
		boolean isInsertScheduleTime = insertScheduleTimeByShiftEditModifyForm(shiftEditAddForm);
		//CmnScheduleCalendarServiceからカレンダー, 年月, 最終日を取得
		CmnScheduleCalendarBean cmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendarYmByYm(shiftEditAddForm.getNowYm());
		//スケジュール時間区分を取得
		ScheduleTimeEntity scheduleTimeEntity = selectScheduleTime(shiftEditAddForm.getNowYm());

		//Beanにセット
		ShiftEditAddBean shiftEditAddBean = new ShiftEditAddBean();
		shiftEditAddBean.setYear(cmnScheduleCalendarBean.getYear());
		shiftEditAddBean.setMonth(cmnScheduleCalendarBean.getMonth());
		shiftEditAddBean.setNowYm(cmnScheduleCalendarBean.getNowYm());
		shiftEditAddBean.setNextYm(cmnScheduleCalendarBean.getNextYm());
		shiftEditAddBean.setBeforeYm(cmnScheduleCalendarBean.getBeforeYm());
		shiftEditAddBean.setInsertScheduleTime(isInsertScheduleTime);
		shiftEditAddBean.setScheduleTimeEntity(scheduleTimeEntity);
		return shiftEditAddBean;
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


	/**
	 * [DB]スケジュール時間区分追加処理
	 *
	 * <p>shiftEditAddFormから登録済みスケジュール時間区分を更新, 新規追加する<br>
	 * また、スケジュール時間区分が何も登録されていないときは新規追加するスケジュール時間区分のみが登録される<br>
	 * スケジュール時間区分が登録されているときは新規追加したい月の前月で取得できるスケジュール時間区分の最終日付を更新する<br>
	 * endYmdが 20220131, 20220331, 99999999かつ新規追加スケジュール時間区分が202203～ -> 20220131, 20220228, 00000000, + 99999999になる
	 * </p>
	 *
	 * @param shiftEditAddForm RequestParam Form
	 * @return boolean <br>
	 * true: 登録済みのスケジュール時間区分の更新・新規追加に成功したとき<br>
	 * false: 登録済みのスケジュール時間区分の更新・新規追加に失敗したとき
	 */
	private boolean insertScheduleTimeByShiftEditModifyForm(ShiftEditAddForm shiftEditAddForm) {

		//Formから新しいスケジュール時間区分の開始月を取得し、日付(ymd)へ変換
		String startYm = shiftEditAddForm.getStartYm().replaceAll("-", "");
		String startYmd = startYm + "01";

		//startYmdをLocalDateへ変換
		CommonLogic commonLogic = new CommonLogic();
		LocalDate startYmdLd = commonLogic.getLocalDateByYmd(startYmd);

		//startYmdLdから前月の最終日の日付(ymd)を取得
		LocalDate beforeMonthLastYmdLd = startYmdLd.minusMonths(1);
		String beforeMonthLastYmd = commonLogic.getLastDateYmd(beforeMonthLastYmdLd.getYear(), beforeMonthLastYmdLd.getMonthValue());

		//前月の最終日で取得できるスケジュール時間区分を取得
		ScheduleTimeEntity recordedScheduleTimeEntity = scheduleTimeRepository.selectScheduleTimeByYmd(beforeMonthLastYmd);

		//スケジュール時間区分が1つも登録されていないとき
		if (recordedScheduleTimeEntity == null) {

			//新しいスケジュール時間区分の適用日付指定なしでセットした後、追加しtrueを返す
			ScheduleTimeEntity updateScheduleTimeEntity = new ScheduleTimeEntity(shiftEditAddForm, Const.SCHEDULE_TIME_INSERT_NEW_END_YMD);
			scheduleTimeRepository.save(updateScheduleTimeEntity);
			return true;
		}

		//前月の最終日で取得できるスケジュール時間区分を含む以降のスケジュール時間区分を全て取得する
		List<ScheduleTimeEntity> recordedScheduleTimeEntityList = scheduleTimeRepository.selectScheduleTimeALLByYmd(beforeMonthLastYmd);

		//取得したスケジュール時間区分だけループし、更新する
		for (int i = 0; i< recordedScheduleTimeEntityList.size(); i++) {

			//現在(要素i)のスケジュール時間区分を取得する
			ScheduleTimeEntity scheduleTimeEntity = recordedScheduleTimeEntityList.get(i);

			//前月の最終日で取得できるスケジュール時間区分(ループ回数が1回目)のとき
			if (i == 0) {

				//最終日付を前月の最終日に変更した後、更新しループに戻る
				scheduleTimeEntity.setEndYmd(beforeMonthLastYmd);
				scheduleTimeRepository.save(scheduleTimeEntity);
				continue;
			}

			//最終日付を修正済みに変更した後、更新する
			scheduleTimeEntity.setEndYmd(Const.SCHEDULE_TIME_INSERT_MODIFY_END_YMD);
			scheduleTimeRepository.save(scheduleTimeEntity);
		}

		//新しいスケジュール時間区分の適用日付指定なしでセットした後、追加しtrueを返す
		ScheduleTimeEntity updateScheduleTimeEntity = new ScheduleTimeEntity(shiftEditAddForm, Const.SCHEDULE_TIME_INSERT_NEW_END_YMD);
		scheduleTimeRepository.save(updateScheduleTimeEntity);
		return true;
	}
}

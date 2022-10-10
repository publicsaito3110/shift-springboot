package com.shift.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CmnScheduleLogic;
import com.shift.common.CommonLogic;
import com.shift.domain.model.bean.CmnScheduleCalendarBean;
import com.shift.domain.model.bean.ScheduleBean;
import com.shift.domain.model.bean.ScheduleModifyBean;
import com.shift.domain.model.entity.SchedulePreEntity;
import com.shift.domain.model.entity.ScheduleTimeEntity;
import com.shift.domain.repository.SchedulePreRepository;
import com.shift.domain.repository.ScheduleTimeRepository;
import com.shift.domain.service.common.CmnScheduleCalendarService;
import com.shift.form.ScheduleModifyForm;

/**
 * @author saito
 *
 */
@Service
public class ScheduleService extends BaseService {

	@Autowired
	private SchedulePreRepository schedulePreRepository;

	@Autowired
	private ScheduleTimeRepository scheduleTimeRepository;

	@Autowired
	private CmnScheduleCalendarService cmnScheduleCalendarService;


	/**
	 * [Service] (/schedule)
	 *
	 * @param ym RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return ScheduleBean
	 */
	public ScheduleBean schedule(String ym, String loginUser) {

		//CmnScheduleCalendarServiceからカレンダー, 年月, 最終日を取得
		CmnScheduleCalendarBean cmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendarYmByYm(ym);
		//スケジュール時間区分を取得
		ScheduleTimeEntity scheduleTimeEntity = selectScheduleTime(cmnScheduleCalendarBean.getLastDateYmd());
		//ログインユーザの1ヵ月分の予定スケジュールを取得
		SchedulePreEntity schedulePreEntity = selectSchedulePre(cmnScheduleCalendarBean.getYear(), cmnScheduleCalendarBean.getMonth(), loginUser);
		//スケジュールから登録済みスケジュールの判定をBooleanのListで取得
		List<Boolean[]> isScheduleRecordedArrayList = calcIsScheduleRecordedArrayListBySchedule(schedulePreEntity, scheduleTimeEntity);

		//Beanにセット
		ScheduleBean scheduleBean = new ScheduleBean();
		scheduleBean.setYear(cmnScheduleCalendarBean.getYear());
		scheduleBean.setMonth(cmnScheduleCalendarBean.getMonth());
		scheduleBean.setIsScheduleRecordedArrayList(isScheduleRecordedArrayList);
		scheduleBean.setCalendarList(cmnScheduleCalendarBean.getCalendarList());
		scheduleBean.setNowYm(cmnScheduleCalendarBean.getNowYm());
		scheduleBean.setAfterYm(cmnScheduleCalendarBean.getNextYm());
		scheduleBean.setBeforeYm(cmnScheduleCalendarBean.getBeforeYm());
		scheduleBean.setScheduleTimeEntity(scheduleTimeEntity);
		return scheduleBean;
	}


	/**
	 * [Service] (/schedule/modify)
	 *
	 * @param scheduleModifyForm RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return ScheduleBean
	 */
	public ScheduleModifyBean scheduleModify(ScheduleModifyForm scheduleModifyForm, String loginUser) {

		//スケジュール予定をDBに登録
		updateSchedulePre(scheduleModifyForm, loginUser);
		//CmnScheduleCalendarServiceからカレンダー, 年月, 最終日を取得
		CmnScheduleCalendarBean cmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendarYmByYm(scheduleModifyForm.getYm());
		//スケジュール時間区分を取得
		ScheduleTimeEntity scheduleTimeEntity = selectScheduleTime(cmnScheduleCalendarBean.getLastDateYmd());
		//ログインユーザの1ヵ月分の予定スケジュールを取得
		SchedulePreEntity schedulePreEntity = selectSchedulePre(cmnScheduleCalendarBean.getYear(), cmnScheduleCalendarBean.getMonth(), loginUser);
		//スケジュールから登録済みスケジュールの判定をBooleanのListで取得
		List<Boolean[]> isScheduleRecordedArrayList = calcIsScheduleRecordedArrayListBySchedule(schedulePreEntity, scheduleTimeEntity);

		//Beanにセット
		ScheduleModifyBean scheduleModifyBean = new ScheduleModifyBean();
		scheduleModifyBean.setYear(cmnScheduleCalendarBean.getYear());
		scheduleModifyBean.setMonth(cmnScheduleCalendarBean.getMonth());
		scheduleModifyBean.setIsScheduleRecordedArrayList(isScheduleRecordedArrayList);
		scheduleModifyBean.setCalendarList(cmnScheduleCalendarBean.getCalendarList());
		scheduleModifyBean.setNowYm(cmnScheduleCalendarBean.getNowYm());
		scheduleModifyBean.setAfterYm(cmnScheduleCalendarBean.getNextYm());
		scheduleModifyBean.setBeforeYm(cmnScheduleCalendarBean.getBeforeYm());
		scheduleModifyBean.setScheduleTimeEntity(scheduleTimeEntity);
		return scheduleModifyBean;
	}


	/**
	 * スケジュール登録済み判定List取得処理
	 *
	 * <p>schedulePreEntityとscheduleTimeListから登録済みのスケジュールとスケジュール時間区分を取得し、登録されているかを判別する<br>
	 * Listのエレメント(Boolean[])には1日ごとのスケジュール時間区分で登録済みかを判別する
	 * </p>
	 *
	 * @param schedulePreEntity DBから取得したSchedulePreEntity
	 * @param scheduleTimeEntity DBから取得したScheduleTimeEntity
	 * @return List<Boolean[]> (List&lt;Boolean[]&gt;)<br>
	 * エレメント(Boolean[])<br>
	 * true: スケジュール登録済み, false: スケジュール未登録<br>
	 * ただし、要素数はscheduleTimeListの要素数と必ず一致する
	 */
	private List<Boolean[]> calcIsScheduleRecordedArrayListBySchedule(SchedulePreEntity schedulePreEntity, ScheduleTimeEntity scheduleTimeEntity) {

		//SchedulePreEntityをインスタンス化
		SchedulePreEntity trimSchedulePreEntity = new SchedulePreEntity();

		//schedulePreEntityがnullでないとき、trimSchedulePreEntityにschedulePreEntityを代入
		if (schedulePreEntity != null) {
			trimSchedulePreEntity = schedulePreEntity;
		}

		//trimSchedulePreEntityに登録されている値(1ヵ月分)をListで取得
		List<String> scheduleList = trimSchedulePreEntity.getDayList();

		//スケジュール登録済みかを判定する共通Logicクラス
		CmnScheduleLogic cmnScheduleLogic = new CmnScheduleLogic();

		//1ヵ月単位でスケジュールが登録されているかを判別するList
		List<Boolean[]> isScheduleRecordedArrayList = new ArrayList<>();

		//scheduleListの要素数(1ヵ月の日付)の回数だけループする
		for (String schedule: scheduleList) {

			//スケジュールが登録されているかどうかを判別する配列(1日ごとのスケジュールにおいて要素0 -> scheduleTimeList(0), 要素1 -> scheduleTimeList(1)...)
			Boolean[] isScheduleRecordedArray = cmnScheduleLogic.toIsScheduleRecordedArrayBySchedule(schedule, scheduleTimeEntity);

			//isScheduleDisplayArrayListにセットする
			isScheduleRecordedArrayList.add(isScheduleRecordedArray);
		}


		return isScheduleRecordedArrayList;
	}


	/**
	 * [DB]スケジュール検索処理
	 *
	 * <p>現在の年月からログインユーザーの1ヵ月分のスケジュール予定を取得する<br>
	 * ただし、登録済みのスケジュールがないときはnullとなる<br>
	 * また、日付が存在しない日(2月 -> 30, 31日etc)は必ず登録されていない
	 * </p>
	 *
	 * @param year LocalDateから取得した年(int)
	 * @param month LocalDateから取得した月(int)
	 * @return SchedulePreEntity <br>
	 * フィールド(SchedulePreEntity)<br>
	 * id, ym, user, 1, 2, 3, 4, 5... 30, 31
	 *
	 */
	private SchedulePreEntity selectSchedulePre(int year, int month, String loginUser) {

		//year, monthをym(YYYYMM)に変換
		String ym = new CommonLogic().toStringYmFormatSixByYearMonth(year, month);

		//DBから取得し、返す
		SchedulePreEntity schedulePreEntity = schedulePreRepository.findByYmAndUser(ym, loginUser);
		return schedulePreEntity;
	}


	/**
	 * [DB]スケジュール登録処理
	 *
	 * <p>scheduleModifyFormとloginUserから1ヵ月分のスケジュール予定を登録する<br>
	 * ただし、登録済みのスケジュール時間区分ごとにとうろくされ、最大7区分まで登録される<br>
	 * スケジュール登録 -> 1, スケジュールを登録しない -> 0となる<br>
	 * また、日付が存在しない日(2月 -> 30, 31日etc)は登録スケジュールは登録されない
	 * </p>
	 *
	 * @param scheduleModifyForm RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return void
	 *
	 */
	private void updateSchedulePre(ScheduleModifyForm scheduleModifyForm, String loginUser) {

		//year, monthをym(YYYYMM)に変換
		String ym = scheduleModifyForm.getYm();

		//DBからymとloginUserで検索
		SchedulePreEntity schedulePreEntity = schedulePreRepository.findByYmAndUser(ym, loginUser);

		//スケジュールが未登録(schedulePreEntityがnull)のとき、インスタンス化
		if (schedulePreEntity == null) {
			schedulePreEntity = new SchedulePreEntity();
		}

		//scheduleModifyForm, loginUser, ymからschedulePreEntityのフィールドに値をセットし、DBに登録
		schedulePreEntity.setDayByScheduleModifyForm(scheduleModifyForm);
		schedulePreEntity.setUser(loginUser);
		schedulePreEntity.setYm(ym);
		schedulePreRepository.save(schedulePreEntity);
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

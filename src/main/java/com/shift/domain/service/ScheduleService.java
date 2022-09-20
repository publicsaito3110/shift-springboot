package com.shift.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CmnScheduleLogic;
import com.shift.common.CommonLogic;
import com.shift.domain.model.bean.CmnScheduleBean;
import com.shift.domain.model.bean.ScheduleBean;
import com.shift.domain.model.bean.ScheduleModifyBean;
import com.shift.domain.model.entity.SchedulePreEntity;
import com.shift.domain.model.entity.ScheduleTimeEntity;
import com.shift.domain.repository.SchedulePreRepository;
import com.shift.domain.repository.ScheduleTimeRepository;
import com.shift.domain.service.common.CmnScheduleService;
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
	private CmnScheduleService cmnScheduleService;


	/**
	 * [Service] (/schedule)
	 *
	 * @param ym RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return ScheduleBean
	 */
	public ScheduleBean schedule(String ym, String loginUser) {

		//CmnScheduleService(共通サービス)から処理結果を取得
		CmnScheduleBean cmnScheduleBean = cmnScheduleService.generateCalendarYmByYm(ym);
		//Service内の処理を実行
		List<ScheduleTimeEntity> scheduleTimeList = selectScheduleTime();
		SchedulePreEntity schedulePreEntity = selectSchedulePre(cmnScheduleBean.getYear(), cmnScheduleBean.getMonth(), loginUser);
		List<Boolean[]> isScheduleRecordedArrayList = calcIsScheduleRecordedArrayListBySchedule(schedulePreEntity, scheduleTimeList);

		//Beanにセット
		ScheduleBean scheduleBean = new ScheduleBean();
		scheduleBean.setYear(cmnScheduleBean.getYear());
		scheduleBean.setMonth(cmnScheduleBean.getMonth());
		scheduleBean.setIsScheduleRecordedArrayList(isScheduleRecordedArrayList);
		scheduleBean.setCalendarList(cmnScheduleBean.getCalendarList());
		scheduleBean.setNowYm(cmnScheduleBean.getNowYm());
		scheduleBean.setAfterYm(cmnScheduleBean.getNextYm());
		scheduleBean.setBeforeYm(cmnScheduleBean.getBeforeYm());
		scheduleBean.setScheduleTimeList(scheduleTimeList);
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
		//CmnScheduleService(共通サービス)から処理結果を取得
		CmnScheduleBean cmnScheduleBean = cmnScheduleService.generateCalendarYmByYm(scheduleModifyForm.getYm());
		//Service内の処理を実行
		List<ScheduleTimeEntity> scheduleTimeList = selectScheduleTime();
		SchedulePreEntity schedulePreEntity = selectSchedulePre(cmnScheduleBean.getYear(), cmnScheduleBean.getMonth(), loginUser);
		List<Boolean[]> isScheduleRecordedArrayList = calcIsScheduleRecordedArrayListBySchedule(schedulePreEntity, scheduleTimeList);

		//Beanにセット
		ScheduleModifyBean scheduleModifyBean = new ScheduleModifyBean();
		scheduleModifyBean.setYear(cmnScheduleBean.getYear());
		scheduleModifyBean.setMonth(cmnScheduleBean.getMonth());
		scheduleModifyBean.setIsScheduleRecordedArrayList(isScheduleRecordedArrayList);
		scheduleModifyBean.setCalendarList(cmnScheduleBean.getCalendarList());
		scheduleModifyBean.setNowYm(cmnScheduleBean.getNowYm());
		scheduleModifyBean.setAfterYm(cmnScheduleBean.getNextYm());
		scheduleModifyBean.setBeforeYm(cmnScheduleBean.getBeforeYm());
		scheduleModifyBean.setScheduleTimeList(scheduleTimeList);
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
	 * @param scheduleTimeList DBから取得したList<ScheduleTimeEntity> (List&lt;ScheduleTimeEntity&gt;)
	 * @return List<Boolean[]> (List&lt;Boolean[]&gt;)<br>
	 * エレメント(Boolean[])<br>
	 * true: スケジュール登録済み, false: スケジュール未登録<br>
	 * ただし、要素数はscheduleTimeListの要素数と必ず一致する
	 */
	private List<Boolean[]> calcIsScheduleRecordedArrayListBySchedule(SchedulePreEntity schedulePreEntity, List<ScheduleTimeEntity> scheduleTimeList) {

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
			Boolean[] isScheduleRecordedArray = cmnScheduleLogic.toIsScheduleRecordedArrayBySchedule(schedule, scheduleTimeList);

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

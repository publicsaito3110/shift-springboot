package com.shift.domain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CommonLogic;
import com.shift.common.CommonUtil;
import com.shift.common.Const;
import com.shift.domain.model.bean.ScheduleBean;
import com.shift.domain.model.bean.ScheduleModifyBean;
import com.shift.domain.model.entity.SchedulePreEntity;
import com.shift.domain.model.entity.ScheduleTimeEntity;
import com.shift.domain.repository.SchedulePreRepository;
import com.shift.domain.repository.ScheduleTimeRepository;
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


	/**
	 * [Service] (/schedule)
	 *
	 * @param ym RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return ScheduleBean
	 */
	public ScheduleBean schedule(String ym, String loginUser) {

		int[] yearMonthArray = changeYearMonthArray(ym);
		List<ScheduleTimeEntity> scheduleTimeList = selectScheduleTime();
		SchedulePreEntity schedulePreEntity = selectSchedulePre(yearMonthArray[0], yearMonthArray[1], loginUser);
		List<Boolean[]> isScheduleRecordedArrayList = calcIsScheduleRecordedArrayListBySchedule(schedulePreEntity, scheduleTimeList);
		List<Integer> calendarList = generateCalendar(yearMonthArray[0], yearMonthArray[1]);
		String[] nextBeforeYmArray = calcNextBeforYmArray(yearMonthArray[0], yearMonthArray[1]);

		//Beanにセット
		ScheduleBean scheduleBean = new ScheduleBean();
		scheduleBean.setYear(yearMonthArray[0]);
		scheduleBean.setMonth(yearMonthArray[1]);
		scheduleBean.setIsScheduleRecordedArrayList(isScheduleRecordedArrayList);
		scheduleBean.setCalendarList(calendarList);
		scheduleBean.setNowYm(nextBeforeYmArray[0]);
		scheduleBean.setAfterYm(nextBeforeYmArray[1]);
		scheduleBean.setBeforeYm(nextBeforeYmArray[2]);
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
		int[] yearMonthArray = changeYearMonthArray(null);
		List<ScheduleTimeEntity> scheduleTimeList = selectScheduleTime();
		SchedulePreEntity schedulePreEntity = selectSchedulePre(yearMonthArray[0], yearMonthArray[1], loginUser);
		List<Boolean[]> isScheduleRecordedArrayList = calcIsScheduleRecordedArrayListBySchedule(schedulePreEntity, scheduleTimeList);
		List<Integer> calendarList = generateCalendar(yearMonthArray[0], yearMonthArray[1]);
		String[] nextBeforeYmArray = calcNextBeforYmArray(yearMonthArray[0], yearMonthArray[1]);

		//Beanにセット
		ScheduleModifyBean scheduleModifyBean = new ScheduleModifyBean();
		scheduleModifyBean.setYear(yearMonthArray[0]);
		scheduleModifyBean.setMonth(yearMonthArray[1]);
		scheduleModifyBean.setIsScheduleRecordedArrayList(isScheduleRecordedArrayList);
		scheduleModifyBean.setCalendarList(calendarList);
		scheduleModifyBean.setNowYm(nextBeforeYmArray[0]);
		scheduleModifyBean.setAfterYm(nextBeforeYmArray[1]);
		scheduleModifyBean.setBeforeYm(nextBeforeYmArray[2]);
		scheduleModifyBean.setScheduleTimeList(scheduleTimeList);
		return scheduleModifyBean;
	}


	/**
	 * 年月変換処理
	 *
	 * <p>年と月をint型に変換し、int[]で返す<br>
	 * ただし、パラメーターがない(null)場合またはymがフォーマット通りでないときは現在の年月になる<br>
	 * int[0]が年, int[1]が月
	 * </p>
	 *
	 * @param ym YYYYMM<br>
	 * {RequestParameter}
	 *
	 * @return int[] intに変換した年[0]と月[1]<br>
	 * int[0]が年, int[1]が月
	 */
	private int[] changeYearMonthArray(String ym) {

		//ymをymd(YYMM01)に変換し、LocalDateを取得する
		String ymd = CommonUtil.changeEmptyByNull(ym) + "01";
		LocalDate ymdLd = new CommonLogic().getLocalDateByYmd(ymd);

		//ym(年月)が指定されていないまたはymがYYYYMMでないとき
		if (ymdLd == null) {

			//現在の日付を取得し、年月に変換
			LocalDate nowLd = LocalDate.now();
			int year = nowLd.getYear();
			int month = nowLd.getMonthValue();

			//年月をint[]に格納して返す
			int[] yearMonth = {year, month};
			return yearMonth;
		}

		//ymdLdから年月に変換
		int year = ymdLd.getYear();
		int month = ymdLd.getMonthValue();

		//年月をint[]に格納して返す
		int[] yearMonthArray = {year, month};
		return yearMonthArray;
	}


	/**
	 * カレンダー作成処理
	 *
	 * <p>year, monthから1ヵ月分のカレンダーを作成する<br>
	 * ただし、カレンダーのフォーマット(7×4 or 7×5 or 7×6)にするため、前月, 翌月も含む(前月, 翌月の日付は含まれない)<br>
	 * また、前月, 翌月分の日付はnullが格納される
	 * </p>
	 *
	 * @param year LocalDateから取得した年(int)
	 * @param month LocalDateから取得した月(int)
	 * @return List<Integer> 1ヵ月分のカレンダー<br>
	 * エレメント(Integer)<br>
	 * 日付をInteger型で格納される
	 */
	private List<Integer> generateCalendar(int year, int month) {

		//------------------------------------
		// 第1週目の日曜日～初日までを設定
		//------------------------------------

		//LocalDateから1日目の情報を取得
		LocalDate localDate = getLocalDateByYearMonth(year, month);

		//第1週目の初日の曜日を取得（月:1, 火:2.....日:7）
		int firstWeek = localDate.getDayOfWeek().getValue();

		//日付けとスケジュールを格納する
		List<Integer> calendarList = new ArrayList<>();

		//firstWeekが日曜日でないとき
		if (firstWeek != 7) {

			//初日が日曜を除く取得した曜日の回数分nullを代入してカレンダーのフォーマットに揃える
			for (int i = 1; i <= firstWeek; i ++) {
				calendarList.add(null);
			}
		}

		//-------------
		// 日付を設定
		//-------------

		//最終日をLocalDateから取得
		int lastDay = localDate.lengthOfMonth();

		//lastDayの回数だけループして日付を格納
		for (int i = 1; i <= lastDay; i++) {
			calendarList.add(i);
		}

		//------------------------------------
		// 最終週の終了日～土曜日までを設定
		//------------------------------------

		//calendarListに登録した要素数から残りの最終週の土曜日までの日数を取得
		int remainderWeek = 7 - (calendarList.size() % 7);

		// remainderWeekが7(最終日が土曜日)以外のとき
		if (remainderWeek != 7) {

			//remainderWeekの回数分nullを代入してカレンダーのフォーマットに揃える
			for (int i = 1; i <= remainderWeek; i ++) {
				calendarList.add(null);
			}
		}

		return calendarList;
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

		//1ヵ月単位でスケジュールが登録されているかを判別するList
		List<Boolean[]> isScheduleRecordedArrayList = new ArrayList<>();

		//scheduleListの要素数(1ヵ月の日付)の回数だけループする
		for (String schedule: scheduleList) {

			//スケジュールが登録されているかどうかを判別する配列(1日ごとのスケジュールにおいて要素0 -> scheduleTimeList(0), 要素1 -> scheduleTimeList(1)...)
			Boolean[] isScheduleRecordedArray = new Boolean[Const.SCHEDULE_RECORDABLE_MAX_DIVISION];

			//scheduleTimeListの要素数の回数だけループ
			for (int i = 0; i < scheduleTimeList.size(); i++) {

				//scheduleがnullまたは空文字のとき
				if (schedule == null || schedule.isEmpty()) {
					isScheduleRecordedArray[i] = false;
					continue;
				}

				//scheduleの文字数がiより小さい(1文字取得できない)とき
				if (schedule.length() <= i) {
					isScheduleRecordedArray[i] = false;
					continue;
				}

				// TODO schedule_time(DB)が更新されたときは未対応
				//ループの回数から1文字だけ取得
				String scheduleValueChara = String.valueOf(schedule.charAt(i));

				//スケジュールが(scheduleValueCharaが1でない)登録されていないとき
				if (!Const.SCHEDULE_PRE_DAY_RECORDED.equals(scheduleValueChara)) {
					isScheduleRecordedArray[i] = false;
					continue;
				}

				//スケジュールが登録されているときtrueを代入
				isScheduleRecordedArray[i] = true;
			}

			//isScheduleDisplayArrayListにセットする
			isScheduleRecordedArrayList.add(isScheduleRecordedArray);
		}


		return isScheduleRecordedArrayList;
	}


	/**
	 * 翌前月に取得処理
	 *
	 * <p>翌月と前月を計算して返す<br>
	 * ym(YYYYMM)に変換した現在の月[0], 翌月[1]と前月[2]
	 * </p>
	 *
	 * @param year LocalDateから取得した年(int)
	 * @param month LocalDateから取得した月(int)
	 * @return String[] 現在の月[0], 翌月のym[1]と前月のym[2]<br>
	 * String[0]が現在の月, String[1]が翌月, String[2]が前月
	 */
	private String[] calcNextBeforYmArray(int year, int month) {

		//year, monthから現在のLocalDateを取得し、nowYmに代入
		LocalDate nowLd = getLocalDateByYearMonth(year, month);
		int nowYear = nowLd.getYear();
		int nowMonth = nowLd.getMonthValue();

		//nowYear, nowMonthをym(YYYYMM)に変換
		String nowYm = toStringYmFormatSixByYearMonth(nowYear, nowMonth);

		//nowLdから前月のLocalDateを取得し、beforeYmに代入
		LocalDate beforeMonthLd = nowLd.minusMonths(1);
		int beforeYear = beforeMonthLd.getYear();
		int beforeMonth = beforeMonthLd.getMonthValue();

		//beforeYear, beforeMonthをym(YYYYMM)に変換
		String beforeYm = toStringYmFormatSixByYearMonth(beforeYear, beforeMonth);

		//nowLdから翌月のymをafterYmに代入
		LocalDate afterMonthLd = nowLd.plusMonths(1);
		int afterYear = afterMonthLd.getYear();
		int afterMonth = afterMonthLd.getMonthValue();

		//afterYear, afterMonthをym(YYYYMM)に変換
		String afterYm = toStringYmFormatSixByYearMonth(afterYear, afterMonth);

		//nowYm, beforeYm, afterYmをString[]に格納し、返す
		String[] nextBeforeYmArray = {nowYm, afterYm, beforeYm};
		return nextBeforeYmArray;
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
		String ym = toStringYmFormatSixByYearMonth(year, month);

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


	/**
	 * [privateメソッド共通処理]年月変換処理
	 *
	 * <p>year(int), month(int)をym(YYYYMM)に変換して返す</p>
	 *
	 * @param year LocalDateから取得した年(int)
	 * @param month LocalDateから取得した月(int)
	 * @return String ym(YYYYMM)
	 */
	private String toStringYmFormatSixByYearMonth(int year, int month) {

		//ym(YYYYMM)に変換する
		return String.valueOf(year) + String.format("%02d", month);
	}


	/**
	 * [privateメソッド共通処理]LoccalDate取得処理
	 *
	 * <p>year(int), month(int)からLocalDateを返す<br>
	 * ただし、正確な日付は初日となる
	 * </p>
	 *
	 * @param year LocalDateから取得した年(int)
	 * @param month LocalDateから取得した月(int)
	 * @return LocalDate ym(YYYYMM)
	 */
	private LocalDate getLocalDateByYearMonth(int year, int month) {

		//year, monthから初日のLocalDateで取得し、返す
		LocalDate localDate = LocalDate.of(year, month, 1);
		return localDate;
	}
}

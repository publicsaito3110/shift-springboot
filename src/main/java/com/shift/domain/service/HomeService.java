package com.shift.domain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CmnScheduleLogic;
import com.shift.common.CommonLogic;
import com.shift.common.Const;
import com.shift.domain.model.bean.CmnNewsBean;
import com.shift.domain.model.bean.HomeBean;
import com.shift.domain.model.bean.HomeDayScheduleBean;
import com.shift.domain.model.entity.ScheduleEntity;
import com.shift.domain.model.entity.ScheduleTimeEntity;
import com.shift.domain.model.entity.UserEntity;
import com.shift.domain.repository.ScheduleRepository;
import com.shift.domain.repository.ScheduleTimeRepository;
import com.shift.domain.repository.UserRepository;
import com.shift.domain.service.common.CmnNewsService;

/**
 * @author saito
 *
 */
@Service
public class HomeService extends BaseService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private ScheduleTimeRepository scheduleTimeRepository;

	@Autowired
	private CmnNewsService cmnNewsService;


	/**
	 * [Service] (/home)
	 *
	 * @param ymd RequestParameter 日付ymd
	 * @param loginUser Authenticationから取得したユーザID
	 * @return HomeBean
	 */
	public HomeBean home(String ymd, String loginUser) {

		//Service内の処理を実行
		UserEntity userEntity = selectUserByLoginUser(loginUser);
		//CmnNewsService(共通サービス)から表示可能なお知らせを取得
		CmnNewsBean cmnNewsBean = cmnNewsService.generateDisplayNowNews();
		//指定された日付とその日付の1週間前後の日付をymdで取得
		String[] nowBeforeAfterWeekYmdArray = calcNowBeforeAfterWeekYmdArray(ymd);
		//現在の日付から1日ごと(1週間分)に確定スケジュールを取得
		List<ScheduleEntity> oneweekScheduleList = selectScheduleForOnweek(nowBeforeAfterWeekYmdArray[0], loginUser);
		//現在の日付から1日ごと(1週間分)にスケジュール時間区分を取得
		List<ScheduleTimeEntity> oneweekScheduleTimeList = selectScheduleTimeForOneweek(nowBeforeAfterWeekYmdArray[0]);
		//取得したスケジュールから表示するスケジュールを1週間分のカレンダーで取得
		List<HomeDayScheduleBean> dayScheduleList = generateOneWeekCalendar(oneweekScheduleList, oneweekScheduleTimeList, nowBeforeAfterWeekYmdArray[0]);

		//Beanにセット
		HomeBean homeBean = new HomeBean();
		homeBean.setBeforeWeekYmd(nowBeforeAfterWeekYmdArray[1]);
		homeBean.setAfterWeekYmd(nowBeforeAfterWeekYmdArray[2]);
		homeBean.setUserEntity(userEntity);
		homeBean.setNewsList(cmnNewsBean.getNewsList());
		homeBean.setDayScheduleList(dayScheduleList);
		return homeBean;
	}


	/**
	 * 前翌週日付計算処理
	 *
	 * <p>指定された日付から指定された日付と1週間前の日付と1週間後の日付をymd(YYYYMMDD)で取得する<br>
	 * ただし、指定された日付がないまたは指定された日付が異常な値だった場合は現在の日付となる<br>
	 * String[0]が指定された日付, String[1]が指定された日付の1週間前の日付, String[2]が指定された日付の1週間後の日付
	 * </p>
	 *
	 * @param ymd RequestParameter 日付ymd
	 * @return String[] ymdフォーマットに変換された日付<br>
	 * String[0]が指定された日付, String[1]が指定された日付の1週間前の日付, String[2]が指定された日付の1週間後の日付
	 */
	private String[] calcNowBeforeAfterWeekYmdArray(String ymd) {

		//指定された日付のLocalDateを取得
		CommonLogic commonLogic = new CommonLogic();
		LocalDate ymdLd = commonLogic.getLocalDateByYmd(ymd);

		//ymdが指定されていないまたは指定した日付が取得できなかったとき、現在の日付のLocalDateを取得
		if (ymdLd == null) {
			ymdLd = LocalDate.now();
		}

		//指定された日付の1週間後のLocalDateを取得
		LocalDate afterWeekLd = ymdLd.plusWeeks(1);
		LocalDate beforeWeekLd = ymdLd.minusWeeks(1);

		//指定された日付と1週間後の日付をymdで取得
		String nowYmd = commonLogic.toStringYmdByYearMonthDay(ymdLd.getYear(), ymdLd.getMonthValue(), ymdLd.getDayOfMonth());
		String afterWeekYmd = commonLogic.toStringYmdByYearMonthDay(afterWeekLd.getYear(), afterWeekLd.getMonthValue(), afterWeekLd.getDayOfMonth());
		String beforeWeekYmd = commonLogic.toStringYmdByYearMonthDay(beforeWeekLd.getYear(), beforeWeekLd.getMonthValue(), beforeWeekLd.getDayOfMonth());

		//配列に格納し、返す
		String[] nowBeforeAfterWeekYmdArray = {nowYmd, beforeWeekYmd, afterWeekYmd};
		return nowBeforeAfterWeekYmdArray;
	}


	/**
	 * 1週間カレンダー取得処理
	 *
	 * <p>取得したスケジュールと指定された日付から1週間の確定スケジュールをカレンダー形式に変換する<br>
	 * 1日ごとに日付, 確定スケジュール判定, スケジュール時間区分が格納される
	 * </p>
	 *
	 * @param oneweekScheduleList DBから取得したList
	 * @param oneweekScheduleTimeList DBから取得したList
	 * @param nowYmd ymdフォーマットで取得した日付
	 * @return List<HomeDayScheduleBean> 1週間のスケジュール情報<br>
	 * フィールド(List&lt;HomeDayScheduleBean&gt;)<br>
	 * month, day, week, isScheduleRecordedArray, scheduleTimeEntity
	 */
	private List<HomeDayScheduleBean> generateOneWeekCalendar(List<ScheduleEntity> oneweekScheduleList, List<ScheduleTimeEntity> oneweekScheduleTimeList, String nowYmd) {

		//1週間のスケジュール情報を格納するための変数
		List<HomeDayScheduleBean> dayScheduleList = new ArrayList<>();

		//スケジュールが登録されているか判定するクラス
		CmnScheduleLogic cmnScheduleLogic = new CmnScheduleLogic();

		//指定された日付をLocalDateで取得
		CommonLogic commonLogic = new CommonLogic();
		LocalDate nowYmdLd = commonLogic.getLocalDateByYmd(nowYmd);

		//1日ごと(1週間分)に取得した確定スケジュールだけループする
		for (int i = 0; i < Const.HOME_DISPLAY_SCHEDULE_DAY; i++) {

			//現在のループ回数の要素目のスケジュールをScheduleEntityに格納
			ScheduleEntity scheduleEntity = oneweekScheduleList.get(i);

			//スケジュールが登録されていなかったとき、インスタンス化
			if (scheduleEntity == null) {
				scheduleEntity = new ScheduleEntity();
			}

			//スケジュールから日付ごとのスケジュールをListで取得
			List<String> scheduleDayList = scheduleEntity.getDayList();

			//ループ回数 + 指定された日付の日をintで取得
			LocalDate localDate = nowYmdLd.plusDays(i);
			int day = localDate.getDayOfMonth();

			//日付からスケジュールを取得
			int index = day - 1;
			String schedule = scheduleDayList.get(index);

			//日付からスケジュールが登録されているかを判定した配列を取得
			ScheduleTimeEntity scheduleTimeEntity = oneweekScheduleTimeList.get(i);
			Boolean[] isScheduleRecordedArray =  cmnScheduleLogic.toIsScheduleRecordedArrayBySchedule(schedule, oneweekScheduleTimeList.get(i));

			//スケジュール情報をHomeDayScheduleBeanにセットし、Listへ格納
			HomeDayScheduleBean homeDayScheduleBean = new HomeDayScheduleBean();
			homeDayScheduleBean.setIsScheduleRecordedArray(isScheduleRecordedArray);
			homeDayScheduleBean.setScheduleTimeEntity(scheduleTimeEntity);
			homeDayScheduleBean.setWeek(localDate.getDayOfWeek().getValue());
			homeDayScheduleBean.setMonth(localDate.getMonthValue());
			homeDayScheduleBean.setDay(day);
			dayScheduleList.add(homeDayScheduleBean);
		}

		return dayScheduleList;
	}


	/**
	 * [DB]ユーザ検索処理
	 *
	 * <p>loginUserと一致するユーザを取得する<br>
	 * ただし、一致するユーザーがいない場合はnullとなる
	 * </p>
	 *
	 * @param loginUser Authenticationから取得したユーザID
	 * @return UserEntity<br>
	 * フィールド(UserEntity)<br>
	 * id, name, nameKana, gender, password, address, tel, email, note, admin_flg, del_flg
	 */
	private UserEntity selectUserByLoginUser(String loginUser) {

		Optional<UserEntity> userEntityOpt = userRepository.findById(loginUser);

		//ユーザを取得できなかったとき
		if (!userEntityOpt.isPresent()) {
			return null;
		}

		//UserEntityを返す
		return userEntityOpt.get();
	}


	/**
	 * [DB]1日ごとユーザの確定スケジュール検索処理
	 *
	 * <p>取得したい日付(ymd)から該当する確定スケジュールを1日ごとに(1週間分)取得する<br>
	 * ただし、スケジュールが何も登録されていないときはエレメントはnullとなる
	 * </p>
	 *
	 * @param year LocalDateから取得した年(int)
	 * @param month LocalDateから取得した月(int)
	 * @param loginUser Authenticationから取得したユーザID
	 * @return List<ScheduleEntity> <br>
	 * フィールド(List&lt;ScheduleEntity&gt;)<br>
	 * id, ym, user, 1, 2, 3, 4, 5... 30, 31
	 *
	 */
	private List<ScheduleEntity> selectScheduleForOnweek(String nowYmd, String loginUser) {

		//1日ごと(1週間分)のスケジュールを格納するための変数
		List<ScheduleEntity> ScheduleEntityList = new ArrayList<>();

		//現在の日付をLocalDateで取得
		CommonLogic commonLogic = new CommonLogic();
		LocalDate nowYmdLd = commonLogic.getLocalDateByYmd(nowYmd);

		//7回(1週間分)ループする
		for (int i = 0; i < Const.HOME_DISPLAY_SCHEDULE_DAY; i++) {

			//現在の日付 + ループ回数日の年月を取得する
			LocalDate localDate = nowYmdLd.plusDays(i);
			String ym = commonLogic. toStringYmFormatSixByYearMonth(localDate.getYear(), localDate.getMonthValue());

			//取得した年月からスケジュールを取得し、ScheduleEntityListに格納
			ScheduleEntity scheduleEntity = scheduleRepository.findByYmAndUser(ym, loginUser);
			ScheduleEntityList.add(scheduleEntity);
		}

		return ScheduleEntityList;
	}


	/**
	 * [DB]1日ごとスケジュール時間区分取得処理
	 *
	 * <p>取得したい日付(ymd)から該当するスケジュール時間区分を1日ごとに(1週間分)取得する<br>
	 * また、現在日(ymd)に該当するスケジュール時間区分が複数登録されているときは最新のスケジュール時間区分が取得される<br>
	 * ただし、スケジュール時間区分が何も登録されていないときはエレメントはnullとなる
	 * </p>
	 *
	 * @param ymd 取得したいスケジュール時間区分の日付(YYYYMMDD)
	 * @return List<ScheduleTimeEntity><br>
	 * フィールド(List&lt;ScheduleTimeEntity&gt;)<br>
	 * id, endYmd, name1, startHm1, endHM1, restHm1... startHm7, endHM7, restHm7
	 */
	private List<ScheduleTimeEntity> selectScheduleTimeForOneweek(String nowYmd) {

		//1日ごと(1週間分)のスケジュール時間区分を格納するための変数
		List<ScheduleTimeEntity> ScheduleTimeEntityList = new ArrayList<>();

		//現在の日付をLocalDateで取得
		CommonLogic commonLogic = new CommonLogic();
		LocalDate nowYmdLd = commonLogic.getLocalDateByYmd(nowYmd);

		//7回(1週間分)ループする
		for (int i = 0; i < Const.HOME_DISPLAY_SCHEDULE_DAY; i++) {

			//現在の日付 + ループ回数日の年月を取得する
			LocalDate localDate = nowYmdLd.plusDays(i);
			String ymd = commonLogic. toStringYmdByYearMonthDay(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());

			//取得した年月からスケジュール時間区分を取得し、ScheduleTimeEntityListに格納
			ScheduleTimeEntity scheduleTimeEntity = scheduleTimeRepository.selectScheduleTimeByYmd(ymd);
			ScheduleTimeEntityList.add(scheduleTimeEntity);
		}

		return ScheduleTimeEntityList;
	}
}

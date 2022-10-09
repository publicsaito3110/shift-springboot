package com.shift.domain.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.Const;
import com.shift.domain.model.bean.CmnScheduleCalendarBean;
import com.shift.domain.model.bean.CmnScheduleUserNameBean;
import com.shift.domain.model.bean.ScheduleDecisionBean;
import com.shift.domain.model.bean.ScheduleDecisionModifyBean;
import com.shift.domain.model.bean.ScheduleDecisionModifyModifyBean;
import com.shift.domain.model.dto.ScheduleDayDto;
import com.shift.domain.model.dto.SchedulePreDayDto;
import com.shift.domain.model.entity.ScheduleEntity;
import com.shift.domain.model.entity.ScheduleTimeEntity;
import com.shift.domain.model.entity.UserEntity;
import com.shift.domain.repository.ScheduleDayRepository;
import com.shift.domain.repository.SchedulePreDayRepository;
import com.shift.domain.repository.ScheduleRepository;
import com.shift.domain.repository.ScheduleTimeRepository;
import com.shift.domain.repository.UserRepository;
import com.shift.domain.service.common.CmnScheduleCalendarService;
import com.shift.domain.service.common.CmnScheduleUserNameService;
import com.shift.form.ScheduleDecisionModifyForm;

/**
 * @author saito
 *
 */
@Service
public class ScheduleDecisionService extends BaseService {

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private ScheduleDayRepository scheduleDayRepository;

	@Autowired
	private SchedulePreDayRepository schedulePreDayRepository;

	@Autowired
	private ScheduleTimeRepository scheduleTimeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CmnScheduleCalendarService cmnScheduleCalendarService;

	@Autowired
	private CmnScheduleUserNameService cmnScheduleUserNameService;


	/**
	 * [Service] (/schedule-decision)
	 *
	 * @param ym RequestParameter
	 * @return ScheduleDecisionBean
	 */
	public ScheduleDecisionBean scheduleDecision(String ym) {

		//CmnScheduleCalendarService(共通サービス)から処理結果を取得
		CmnScheduleCalendarBean cmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendarYmByYm(ym);
		//CmnScheduleUserNameService(共通サービス)から処理結果を取得
		CmnScheduleUserNameBean cmnScheduleUserNameBean = cmnScheduleUserNameService.generateScheduleRecordedUserNameByYm(cmnScheduleCalendarBean.getYear(), cmnScheduleCalendarBean.getMonth());

		//Beanにセット
		ScheduleDecisionBean scheduleDecisionBean = new ScheduleDecisionBean();
		scheduleDecisionBean.setYear(cmnScheduleCalendarBean.getYear());
		scheduleDecisionBean.setMonth(cmnScheduleCalendarBean.getMonth());
		scheduleDecisionBean.setNowYm(cmnScheduleCalendarBean.getNowYm());
		scheduleDecisionBean.setCalendarList(cmnScheduleCalendarBean.getCalendarList());
		scheduleDecisionBean.setUserScheduleAllArray(cmnScheduleUserNameBean.getUserScheduleAllArray());
		scheduleDecisionBean.setAfterYm(cmnScheduleCalendarBean.getNextYm());
		scheduleDecisionBean.setBeforeYm(cmnScheduleCalendarBean.getBeforeYm());
		scheduleDecisionBean.setScheduleTimeList(cmnScheduleUserNameBean.getScheduleTimeList());
		return scheduleDecisionBean;
	}


	/**
	 * [Service] (/schedule-decision/modify)
	 *
	 * @param ym RequestParameter
	 * @param day RequestParameter
	 * @return ScheduleDecisionModifyBean
	 */
	public ScheduleDecisionModifyBean scheduleDecisionModify(String ym, String day) {

		//Service
		String[] yearMonthDayArray = calcYearMonthDayArray(ym, day);
		List<SchedulePreDayDto> schedulePreUserList = selectSchedulePreDay(ym, day);
		List<ScheduleDayDto> scheduleUserList = selectScheduleDay(ym, day);
		List<ScheduleTimeEntity> scheduleTimeList = selectScheduleTime();
		List<UserEntity> userDbList = selectUserNotDelFlg();
		List<UserEntity> userList = calcUserListForNewScheduleRecorded(scheduleUserList, userDbList);

		//Beanにセット
		ScheduleDecisionModifyBean scheduleDecisionModifyBean = new ScheduleDecisionModifyBean();
		scheduleDecisionModifyBean.setYear(yearMonthDayArray[0]);
		scheduleDecisionModifyBean.setMonth(yearMonthDayArray[1]);
		scheduleDecisionModifyBean.setDay(yearMonthDayArray[2]);
		scheduleDecisionModifyBean.setSchedulePreDayList(schedulePreUserList);
		scheduleDecisionModifyBean.setScheduleDayList(scheduleUserList);
		scheduleDecisionModifyBean.setScheduleTimeList(scheduleTimeList);
		scheduleDecisionModifyBean.setUserList(userList);
		return scheduleDecisionModifyBean;
	}


	/**
	 * [Service] (/schedule-decision/modify/modify)
	 *
	 * @param scheduleDecisionModifyForm RequestParameter
	 * @return ScheduleDecisionModifyModifyBean
	 */
	public ScheduleDecisionModifyModifyBean scheduleDecisionModifyModify(ScheduleDecisionModifyForm scheduleDecisionModifyForm) {

		//DBを更新する
		updateScheduleByScheduleDecsionModifyForm(scheduleDecisionModifyForm);
		//Service
		String[] yearMonthDayArray = calcYearMonthDayArray(scheduleDecisionModifyForm.getYm(), scheduleDecisionModifyForm.getDay());
		List<SchedulePreDayDto> schedulePreUserList = selectSchedulePreDay(scheduleDecisionModifyForm.getYm(), scheduleDecisionModifyForm.getDay());
		List<ScheduleDayDto> scheduleUserList = selectScheduleDay(scheduleDecisionModifyForm.getYm(), scheduleDecisionModifyForm.getDay());
		List<ScheduleTimeEntity> scheduleTimeList = selectScheduleTime();
		List<UserEntity> userDbList = selectUserNotDelFlg();
		List<UserEntity> userList = calcUserListForNewScheduleRecorded(scheduleUserList, userDbList);

		//Beanにセット
		ScheduleDecisionModifyModifyBean scheduleDecisionModifyModifyBean = new ScheduleDecisionModifyModifyBean();
		scheduleDecisionModifyModifyBean.setYear(yearMonthDayArray[0]);
		scheduleDecisionModifyModifyBean.setMonth(yearMonthDayArray[1]);
		scheduleDecisionModifyModifyBean.setDay(yearMonthDayArray[2]);
		scheduleDecisionModifyModifyBean.setSchedulePreDayList(schedulePreUserList);
		scheduleDecisionModifyModifyBean.setScheduleDayList(scheduleUserList);
		scheduleDecisionModifyModifyBean.setScheduleTimeList(scheduleTimeList);
		scheduleDecisionModifyModifyBean.setUserList(userList);
		return scheduleDecisionModifyModifyBean;
	}


	/**
	 * 年月日付取得処理
	 *
	 * <p>ym及びdayからyear, month, dayを計算し、配列で取得する<br>
	 * 年[0], 月[1]と日[2]
	 * </p>
	 * @param ym RequestParameter
	 * @param day RequestParameter
	 * @return String[] 年[0], 月[1]と日[2]<br>
	 * String[0]が年, String[1]が月, String[2]が日
	 */
	private String[] calcYearMonthDayArray(String ym, String day) {

		//year, month, dayを取得
		String year = ym.substring(0, 4);
		String month = ym.substring(4, 6);
		String trimDay = String.format("%02d", Integer.parseInt(day));

		//配列にセットし、返す
		String[] yearMonthDayArray = {year, month, trimDay};
		return yearMonthDayArray;
	}


	/**
	 * 確定スケジュール登録可能ユーザ取得処理
	 *
	 * <p>scheduleUserListに登録されているユーザを除く、登録可能ユーザをすべて取得する<br>
	 * ただし、scheduleUserListがEmpty(登録済みユーザがいない)ときは何もせずuserDbListを返す
	 * </p>
	 * @param scheduleUserList DBから取得したList<ScheduleDayDto> (List&lt;ScheduleDayDto&gt;)
	 * @param userDbList DBから取得したList<UserEntity> (List&lt;UserEntity&gt;)
	 * @return List<UserEntity> <br>
	 * フィールド(List&lt;UserEntity&gt;) 確定スケジュール登録可能ユーザ<br>
	 * id, name, nameKana, gender, password, address, tel, email, note, admin_flg, del_flg
	 */
	private List<UserEntity> calcUserListForNewScheduleRecorded(List<ScheduleDayDto> scheduleUserList, List<UserEntity> userDbList) {

		//scheduleUserListがEmpty(登録済みユーザがいない)とき、何もせずuserDbListを返す
		if (scheduleUserList.isEmpty()) {
			return userDbList;
		}

		//確定スケジュール(scheduleUserList)に登録されているユーザIDを格納するためのList
		List<String> scheduleRecordedUserIdList = new ArrayList<>();

		//scheduleUserListの回数だけループし、scheduleUserListに登録されているユーザIDを格納する
		for (ScheduleDayDto scheduleDayDto: scheduleUserList) {
			scheduleRecordedUserIdList.add(scheduleDayDto.getUserId());
		}

		//登録可能ユーザを取得するためのList
		List<UserEntity> userList = new ArrayList<>();

		//userDbListの回数だけループしする
		for (UserEntity userEntity: userDbList) {

			//scheduleRecordedUserIdListに含まれていないとき、userEntityをuserListに格納する
			if (!scheduleRecordedUserIdList.contains(userEntity.getId())) {
				userList.add(userEntity);
			}
		}

		return userList;
	}


	/**
	 * [DB]未退職ユーザー取得処理
	 *
	 * <p>未退職ユーザのみを取得する<br>
	 * ただし、退職フラグのあるユーザは除外される<br>
	 * 該当ユーザーがいない場合はEmptyとなる
	 * </p>
	 *
	 * @param void
	 * @return List<UserEntity> <br>
	 * フィールド(List&lt;UserEntity&gt;)<br>
	 * id, name, nameKana, gender, password, address, tel, email, note, admin_flg, del_flg
	 *
	 */
	private List<UserEntity> selectUserNotDelFlg() {

		List<UserEntity> userEntityList = userRepository.findByDelFlgNotOrDelFlgIsNull(Const.USER_DEL_FLG);
		return userEntityList;
	}


	/**
	 * [DB]確定スケジュール登録可能ユーザ取得処理
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
	 * ただし、該当の日付に登録済みのスケジュールが1つもないまたは存在しない日付のときはEmptyとなる<br>
	 * 該当の日付に登録されている場合のみユーザを取得する
	 * </p>
	 *
	 * @param ym 検索したい年月(YYYYMM)
	 * @param day 検索したい日付(1日 -> 1, 2日 -> 2...)
	 * @return List<ScheduleDayDto><br>
	 * フィールド(&lt;ScheduleDayDto&gt;)<br>
	 * id, userId, userName, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7
	 *
	 */
	private List<ScheduleDayDto> selectScheduleDay(String ym, String day) {

		//検索したい文字と文字列目を指定するためにフォーマットを"%1______%"に整える
		String schedule = Const.CHARACTER_PERCENT + Const.SCHEDULE_PRE_DAY_RECORDED + Const.CHARACTER_PERCENT;
		String schedule1 = Const.CHARACTER_PERCENT + Const.SCHEDULE_PRE_DAY_RECORDED + "______" + Const.CHARACTER_PERCENT;
		String schedule2 = Const.CHARACTER_PERCENT + "_" + Const.SCHEDULE_PRE_DAY_RECORDED + "_____" + Const.CHARACTER_PERCENT;
		String schedule3 = Const.CHARACTER_PERCENT + "__" + Const.SCHEDULE_PRE_DAY_RECORDED + "____" + Const.CHARACTER_PERCENT;
		String schedule4 = Const.CHARACTER_PERCENT + "___" + Const.SCHEDULE_PRE_DAY_RECORDED + "___" + Const.CHARACTER_PERCENT;
		String schedule5 = Const.CHARACTER_PERCENT + "____" + Const.SCHEDULE_PRE_DAY_RECORDED + "__" + Const.CHARACTER_PERCENT;
		String schedule6 = Const.CHARACTER_PERCENT + "_____" + Const.SCHEDULE_PRE_DAY_RECORDED + "_" + Const.CHARACTER_PERCENT;
		String schedule7 = Const.CHARACTER_PERCENT + "______" + Const.SCHEDULE_PRE_DAY_RECORDED + Const.CHARACTER_PERCENT;

		List<ScheduleDayDto> scheduleDayDtoList = new ArrayList<>();

		//day(Stringの2桁:DD)に変換する
		String trimDay = String.format("%02d", Integer.parseInt(day));

		//trimDayの値に応じて取得するスケジュールの日付を変える
		if ("01".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay1BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("02".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay2BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("03".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay3BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("04".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay4BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("05".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay5BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("06".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay6BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("07".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay7BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("08".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay8BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("09".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay9BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("10".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay10BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("11".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay11BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("12".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay12BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("13".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay13BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("14".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay14BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("15".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay15BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("16".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay16BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("17".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay17BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("18".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay18BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("19".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay19BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("20".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay20BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("21".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay21BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("22".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay22BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("23".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay23BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("24".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay24BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("25".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay25BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("26".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay26BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("27".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay27BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("28".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay28BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("29".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay29BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("30".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay30BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("31".equals(trimDay)) {
			scheduleDayDtoList = scheduleDayRepository.selectScheduleDay31BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		}

		return scheduleDayDtoList;
	}


	/**
	 * [DB]提出スケジュール取得処理
	 *
	 * <p>ym及びdayに該当する提出スケジュールをユーザごとに取得する<br>
	 * ym=200001,day=1のとき2000年1月1日, ym=200001,day=2のとき2000年1月2日...<br>
	 * ただし、該当の年月に提出済みのスケジュールが1つもないまたは存在しない日付のときはEmptyとなる<br>
	 * 対象の年月にスケジュールを提出したユーザがいる場合、日付に登録されていなくてもユーザは取得される
	 * </p>
	 *
	 * @param ym 検索したい年月(YYYYMM)
	 * @param day 検索したい日付(1日 -> 1, 2日 -> 2...)
	 * @return List<SchedulePreDayDto><br>
	 * フィールド(&lt;SchedulePreDayDto&gt;)<br>
	 * id, userId, userName, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7
	 *
	 */
	private List<SchedulePreDayDto> selectSchedulePreDay(String ym, String day) {

		//検索したい文字と文字列目を指定するためにフォーマットを"%1______%"に整える
		String schedule1 = Const.CHARACTER_PERCENT + Const.SCHEDULE_PRE_DAY_RECORDED + "______" + Const.CHARACTER_PERCENT;
		String schedule2 = Const.CHARACTER_PERCENT + "_" + Const.SCHEDULE_PRE_DAY_RECORDED + "_____" + Const.CHARACTER_PERCENT;
		String schedule3 = Const.CHARACTER_PERCENT + "__" + Const.SCHEDULE_PRE_DAY_RECORDED + "____" + Const.CHARACTER_PERCENT;
		String schedule4 = Const.CHARACTER_PERCENT + "___" + Const.SCHEDULE_PRE_DAY_RECORDED + "___" + Const.CHARACTER_PERCENT;
		String schedule5 = Const.CHARACTER_PERCENT + "____" + Const.SCHEDULE_PRE_DAY_RECORDED + "__" + Const.CHARACTER_PERCENT;
		String schedule6 = Const.CHARACTER_PERCENT + "_____" + Const.SCHEDULE_PRE_DAY_RECORDED + "_" + Const.CHARACTER_PERCENT;
		String schedule7 = Const.CHARACTER_PERCENT + "______" + Const.SCHEDULE_PRE_DAY_RECORDED + Const.CHARACTER_PERCENT;

		List<SchedulePreDayDto> schedulePreDayDtoList = new ArrayList<>();

		//day(Stringの2桁:DD)に変換する
		String trimDay = String.format("%02d", Integer.parseInt(day));

		//dayの値に応じて取得するスケジュールの日付を変える
		if ("01".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay1BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("02".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay2BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("03".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay3BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("04".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay4BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("05".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay5BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("06".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay6BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("07".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay7BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("08".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay8BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("09".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay9BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("10".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay10BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("11".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay11BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("12".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay12BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("13".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay13BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("14".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay14BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("15".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay15BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("16".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay16BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("17".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay17BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("18".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay18BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("19".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay19BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("20".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay20BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("21".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay21BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("22".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay22BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("23".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay23BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("24".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay24BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("25".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay25BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("26".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay26BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("27".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay27BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("28".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay28BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("29".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay29BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("30".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay30BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("31".equals(trimDay)) {
			schedulePreDayDtoList = schedulePreDayRepository.selectScheduleDay31BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		}

		return schedulePreDayDtoList;
	}


	/**
	 * [DB]確定スケジュール登録処理
	 *
	 * <p>scheduleDecisionModifyFormから確定スケジュールに新規でユーザとスケジュール及び更新するユーザとスケジュールを登録する<br>
	 * ただし、確定スケジュールに新規登録するユーザがいない(ユーザが指定されていないまたはスケジュールを登録していない)ときは登録されない<br>
	 * 既に確定スケジュールに登録済みのユーザはスケジュールが登録していないときでもその情報が登録される
	 * </p>
	 *
	 * @param scheduleDecisionModifyForm RequestParameter
	 * @return void
	 *
	 */
	private void updateScheduleByScheduleDecsionModifyForm(ScheduleDecisionModifyForm scheduleDecisionModifyForm) {

		//--------------------------
		//新規確定スケジュール追加
		//--------------------------

		//登録する日付を取得
		String ym = scheduleDecisionModifyForm.getYm();
		String day = String.format("%02d", Integer.parseInt(scheduleDecisionModifyForm.getDay()));

		//新規登録するユーザIDとスケジュールを取得
		String addUserId = scheduleDecisionModifyForm.getAddUserId();
		String[] addScheduleArray = scheduleDecisionModifyForm.getAddScheduleArray();

		//登録するスケジュールがある(addScheduleArrayに1が含まれているかつaddUserIdが""でない)とき
		if (Arrays.asList(addScheduleArray).contains(Const.SCHEDULE_DAY_RECORDED) && !addUserId.isEmpty()) {

			//新規で登録するユーザの確定スケジュールを検索する
			ScheduleEntity addNewUserScheduleEntity = scheduleRepository.findByYmAndUser(ym, addUserId);

			//ym及びaddUserIdに一致する確定スケジュールが登録されていない(scheduleEntityがnull)とき、scheduleEntityをインスタンス化し、値をセットするする
			if (addNewUserScheduleEntity == null) {
				addNewUserScheduleEntity = new ScheduleEntity();
				addNewUserScheduleEntity.setYm(ym);
				addNewUserScheduleEntity.setUser(addUserId);
			}

			//新規で登録するユーザのスケジュール情報を取得
			String addSchedule = scheduleDecisionModifyForm.getAddScheduleAll();

			//登録する日付(day)に応じてスケジュール情報をセットする
			if ("01".equals(day)) {
				addNewUserScheduleEntity.setDay1(addSchedule);
			} else if ("02".equals(day)) {
				addNewUserScheduleEntity.setDay2(addSchedule);
			} else if ("03".equals(day)) {
				addNewUserScheduleEntity.setDay3(addSchedule);
			} else if ("04".equals(day)) {
				addNewUserScheduleEntity.setDay4(addSchedule);
			} else if ("05".equals(day)) {
				addNewUserScheduleEntity.setDay5(addSchedule);
			} else if ("06".equals(day)) {
				addNewUserScheduleEntity.setDay6(addSchedule);
			} else if ("07".equals(day)) {
				addNewUserScheduleEntity.setDay7(addSchedule);
			} else if ("08".equals(day)) {
				addNewUserScheduleEntity.setDay8(addSchedule);
			} else if ("09".equals(day)) {
				addNewUserScheduleEntity.setDay9(addSchedule);
			} else if ("10".equals(day)) {
				addNewUserScheduleEntity.setDay10(addSchedule);
			} else if ("11".equals(day)) {
				addNewUserScheduleEntity.setDay11(addSchedule);
			} else if ("12".equals(day)) {
				addNewUserScheduleEntity.setDay12(addSchedule);
			} else if ("13".equals(day)) {
				addNewUserScheduleEntity.setDay13(addSchedule);
			} else if ("14".equals(day)) {
				addNewUserScheduleEntity.setDay14(addSchedule);
			} else if ("15".equals(day)) {
				addNewUserScheduleEntity.setDay15(addSchedule);
			} else if ("16".equals(day)) {
				addNewUserScheduleEntity.setDay16(addSchedule);
			} else if ("17".equals(day)) {
				addNewUserScheduleEntity.setDay17(addSchedule);
			} else if ("18".equals(day)) {
				addNewUserScheduleEntity.setDay18(addSchedule);
			} else if ("19".equals(day)) {
				addNewUserScheduleEntity.setDay19(addSchedule);
			} else if ("20".equals(day)) {
				addNewUserScheduleEntity.setDay20(addSchedule);
			} else if ("21".equals(day)) {
				addNewUserScheduleEntity.setDay21(addSchedule);
			} else if ("22".equals(day)) {
				addNewUserScheduleEntity.setDay22(addSchedule);
			} else if ("23".equals(day)) {
				addNewUserScheduleEntity.setDay23(addSchedule);
			} else if ("24".equals(day)) {
				addNewUserScheduleEntity.setDay24(addSchedule);
			} else if ("25".equals(day)) {
				addNewUserScheduleEntity.setDay25(addSchedule);
			} else if ("26".equals(day)) {
				addNewUserScheduleEntity.setDay26(addSchedule);
			} else if ("27".equals(day)) {
				addNewUserScheduleEntity.setDay27(addSchedule);
			} else if ("28".equals(day)) {
				addNewUserScheduleEntity.setDay28(addSchedule);
			} else if ("29".equals(day)) {
				addNewUserScheduleEntity.setDay29(addSchedule);
			} else if ("30".equals(day)) {
				addNewUserScheduleEntity.setDay30(addSchedule);
			} else if ("31".equals(day)) {
				addNewUserScheduleEntity.setDay31(addSchedule);
			}

			//新規で追加したスケジュールをDB更新
			scheduleRepository.save(addNewUserScheduleEntity);
		}


		//-------------------------
		//登録済みスケジュール更新
		//-------------------------

		//既存で登録されたユーザと更新スケジュールを取得
		String[][] userArray2 = scheduleDecisionModifyForm.getUserArray();
		String[][] scheduleArray2 = scheduleDecisionModifyForm.getScheduleArray();

		//確定スケジュールに登録済みのユーザがいない(userArray2またはscheduleArray2がnull)のとき、スケジュールを更新しない
		if (userArray2 == null || scheduleArray2 == null) {
			return;
		}

		//配列の要素1つ目を指定するため変数
		int array1Index = 0;

		//userArray2の1つ目の配列(ユーザごと)だけループする
		for (String[] userArray: userArray2) {

			//scheduleArray2を1次元配列で取得
			String[] scheduleArray1 = scheduleArray2[array1Index];

			//更新スケジュール情報を取得するための変数
			String modifySchedule = "";

			//スケジュール時間区分に登録可能な回数だけループする
			for (int i = 0; i < Const.SCHEDULE_RECORDABLE_MAX_DIVISION; i++) {

				//現在の要素にスケジュールがない(scheduleArray[array1Index]の要素が登録可能な回数より少ない)とき、未登録情報を代入
				if (scheduleArray1.length <= i) {
					modifySchedule += Const.SCHEDULE_DAY_NOT_RECORDED;
					continue;
				}

				//スケジュールが登録されていない(scheduleArray[array1Index][i]が未登録)のとき、未登録情報を代入
				if (!Const.SCHEDULE_DAY_RECORDED.equals(scheduleArray2[array1Index][i])) {
					modifySchedule += Const.SCHEDULE_DAY_NOT_RECORDED;
					continue;
				}

				//スケジュールが登録されている(scheduleArray[array1Index][i]が登録済み)のとき、登録済み情報を代入
				modifySchedule += Const.SCHEDULE_DAY_RECORDED;
			}

			//更新するユーザの確定スケジュールを検索する
			ScheduleEntity modifyUserScheduleEntity = scheduleRepository.findByYmAndUser(ym, userArray[0]);

			//ym及びaddUserIdに一致する確定スケジュールが登録されていない(scheduleEntityがnull)とき、scheduleEntityをインスタンス化し、値をセットする
			if (modifyUserScheduleEntity == null) {
				modifyUserScheduleEntity = new ScheduleEntity();
				modifyUserScheduleEntity.setYm(ym);
				modifyUserScheduleEntity.setUser(userArray[0]);
			}

			//更新する日付(day)に応じてスケジュール情報をセットする
			if ("01".equals(day)) {
				modifyUserScheduleEntity.setDay1(modifySchedule);
			} else if ("02".equals(day)) {
				modifyUserScheduleEntity.setDay2(modifySchedule);
			} else if ("03".equals(day)) {
				modifyUserScheduleEntity.setDay3(modifySchedule);
			} else if ("04".equals(day)) {
				modifyUserScheduleEntity.setDay4(modifySchedule);
			} else if ("05".equals(day)) {
				modifyUserScheduleEntity.setDay5(modifySchedule);
			} else if ("06".equals(day)) {
				modifyUserScheduleEntity.setDay6(modifySchedule);
			} else if ("07".equals(day)) {
				modifyUserScheduleEntity.setDay7(modifySchedule);
			} else if ("08".equals(day)) {
				modifyUserScheduleEntity.setDay8(modifySchedule);
			} else if ("09".equals(day)) {
				modifyUserScheduleEntity.setDay9(modifySchedule);
			} else if ("10".equals(day)) {
				modifyUserScheduleEntity.setDay10(modifySchedule);
			} else if ("11".equals(day)) {
				modifyUserScheduleEntity.setDay11(modifySchedule);
			} else if ("12".equals(day)) {
				modifyUserScheduleEntity.setDay12(modifySchedule);
			} else if ("13".equals(day)) {
				modifyUserScheduleEntity.setDay13(modifySchedule);
			} else if ("14".equals(day)) {
				modifyUserScheduleEntity.setDay14(modifySchedule);
			} else if ("15".equals(day)) {
				modifyUserScheduleEntity.setDay15(modifySchedule);
			} else if ("16".equals(day)) {
				modifyUserScheduleEntity.setDay16(modifySchedule);
			} else if ("17".equals(day)) {
				modifyUserScheduleEntity.setDay17(modifySchedule);
			} else if ("18".equals(day)) {
				modifyUserScheduleEntity.setDay18(modifySchedule);
			} else if ("19".equals(day)) {
				modifyUserScheduleEntity.setDay19(modifySchedule);
			} else if ("20".equals(day)) {
				modifyUserScheduleEntity.setDay20(modifySchedule);
			} else if ("21".equals(day)) {
				modifyUserScheduleEntity.setDay21(modifySchedule);
			} else if ("22".equals(day)) {
				modifyUserScheduleEntity.setDay22(modifySchedule);
			} else if ("23".equals(day)) {
				modifyUserScheduleEntity.setDay23(modifySchedule);
			} else if ("24".equals(day)) {
				modifyUserScheduleEntity.setDay24(modifySchedule);
			} else if ("25".equals(day)) {
				modifyUserScheduleEntity.setDay25(modifySchedule);
			} else if ("26".equals(day)) {
				modifyUserScheduleEntity.setDay26(modifySchedule);
			} else if ("27".equals(day)) {
				modifyUserScheduleEntity.setDay27(modifySchedule);
			} else if ("28".equals(day)) {
				modifyUserScheduleEntity.setDay28(modifySchedule);
			} else if ("29".equals(day)) {
				modifyUserScheduleEntity.setDay29(modifySchedule);
			} else if ("30".equals(day)) {
				modifyUserScheduleEntity.setDay30(modifySchedule);
			} else if ("31".equals(day)) {
				modifyUserScheduleEntity.setDay31(modifySchedule);
			}

			//更新したスケジュールをDB更新し、array1Indexの要素を1つ上げる
			scheduleRepository.save(modifyUserScheduleEntity);
			array1Index++;
		}
	}
}

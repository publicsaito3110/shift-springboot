package com.shift.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.Const;
import com.shift.domain.model.bean.CmnScheduleBean;
import com.shift.domain.model.bean.ScheduleDecisionBean;
import com.shift.domain.model.bean.ScheduleDecisionModifyBean;
import com.shift.domain.model.dto.SchedulePreUserDto;
import com.shift.domain.model.dto.ScheduleUserDto;
import com.shift.domain.model.entity.ScheduleTimeEntity;
import com.shift.domain.model.entity.UserEntity;
import com.shift.domain.repository.SchedulePreUserRepository;
import com.shift.domain.repository.ScheduleTimeRepository;
import com.shift.domain.repository.ScheduleUserRepository;
import com.shift.domain.repository.UserRepository;
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
	private SchedulePreUserRepository schedulePreUserRepository;

	@Autowired
	private ScheduleTimeRepository scheduleTimeRepository;

	@Autowired
	private UserRepository userRepository;

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
		String[] yearMonthDayArray = calcYearMonthDayArray(ym, day);
		List<SchedulePreUserDto> schedulePreUserList = selectSchedulePreDay(ym, day);
		List<ScheduleUserDto> scheduleUserList = selectScheduleDay(ym, day);
		List<ScheduleTimeEntity> scheduleTimeList = selectScheduleTime();
		List<UserEntity> userDbList = selectUserNotDelFlg();
		List<UserEntity> userList = calcUserListForNewScheduleRecorded(scheduleUserList, userDbList);

		//Beanにセット
		ScheduleDecisionModifyBean scheduleDecisionModifyBean = new ScheduleDecisionModifyBean();
		scheduleDecisionModifyBean.setYear(yearMonthDayArray[0]);
		scheduleDecisionModifyBean.setMonth(yearMonthDayArray[1]);
		scheduleDecisionModifyBean.setDay(yearMonthDayArray[2]);
		scheduleDecisionModifyBean.setSchedulePreUserList(schedulePreUserList);
		scheduleDecisionModifyBean.setScheduleUserList(scheduleUserList);
		scheduleDecisionModifyBean.setScheduleTimeList(scheduleTimeList);
		scheduleDecisionModifyBean.setUserList(userList);
		return scheduleDecisionModifyBean;
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
	 * @param scheduleUserList DBから取得したList<ScheduleUserDto> (List&lt;ScheduleUserDto&gt;)
	 * @param userDbList DBから取得したList<UserEntity> (List&lt;UserEntity&gt;)
	 * @return List<UserEntity> <br>
	 * フィールド(List&lt;UserEntity&gt;) 確定スケジュール登録可能ユーザ<br>
	 * id, name, nameKana, gender, password, address, tel, email, note, admin_flg, del_flg
	 */
	private List<UserEntity> calcUserListForNewScheduleRecorded(List<ScheduleUserDto> scheduleUserList, List<UserEntity> userDbList) {

		//scheduleUserListがEmpty(登録済みユーザがいない)とき、何もせずuserDbListを返す
		if (scheduleUserList.isEmpty()) {
			return userDbList;
		}

		//確定スケジュール(scheduleUserList)に登録されているユーザIDを格納するためのList
		List<String> scheduleRecordedUserIdList = new ArrayList<>();

		//scheduleUserListの回数だけループし、scheduleUserListに登録されているユーザIDを格納する
		for (ScheduleUserDto scheduleUserDto: scheduleUserList) {
			scheduleRecordedUserIdList.add(scheduleUserDto.getUserId());
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
	 * @return List<ScheduleUserDto><br>
	 * フィールド(&lt;ScheduleUserDto&gt;)<br>
	 * id, userId, userName, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7
	 *
	 */
	private List<ScheduleUserDto> selectScheduleDay(String ym, String day) {

		//検索したい文字と文字列目を指定するためにフォーマットを"%1______%"に整える
		String schedule = Const.CHARACTER_PERCENT + Const.SCHEDULE_PRE_DAY_RECORDED + Const.CHARACTER_PERCENT;
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
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay1BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("2".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay2BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("3".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay3BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("4".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay4BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("5".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay5BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("6".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay6BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("7".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay7BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("8".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay8BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("9".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay9BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("10".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay10BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("11".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay11BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("12".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay12BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("13".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay13BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("14".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay14BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("15".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay15BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("16".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay16BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("17".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay17BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("18".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay18BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("19".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay19BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("20".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay20BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("21".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay21BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("22".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay22BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("23".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay23BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("24".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay24BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("25".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay25BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("26".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay26BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("27".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay27BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("28".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay28BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("29".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay29BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("30".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay30BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("31".equals(day)) {
			scheduleDayDtoList = scheduleUserRepository.selectScheduleDay31BySchedulRepalceValueYm(schedule, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
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
	 * @return List<SchedulePreUserDto><br>
	 * フィールド(&lt;SchedulePreUserDto&gt;)<br>
	 * id, userId, userName, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7
	 *
	 */
	private List<SchedulePreUserDto> selectSchedulePreDay(String ym, String day) {

		//検索したい文字と文字列目を指定するためにフォーマットを"%1______%"に整える
		String schedule1 = Const.CHARACTER_PERCENT + Const.SCHEDULE_PRE_DAY_RECORDED + "______" + Const.CHARACTER_PERCENT;
		String schedule2 = Const.CHARACTER_PERCENT + "_" + Const.SCHEDULE_PRE_DAY_RECORDED + "_____" + Const.CHARACTER_PERCENT;
		String schedule3 = Const.CHARACTER_PERCENT + "__" + Const.SCHEDULE_PRE_DAY_RECORDED + "____" + Const.CHARACTER_PERCENT;
		String schedule4 = Const.CHARACTER_PERCENT + "___" + Const.SCHEDULE_PRE_DAY_RECORDED + "___" + Const.CHARACTER_PERCENT;
		String schedule5 = Const.CHARACTER_PERCENT + "____" + Const.SCHEDULE_PRE_DAY_RECORDED + "__" + Const.CHARACTER_PERCENT;
		String schedule6 = Const.CHARACTER_PERCENT + "_____" + Const.SCHEDULE_PRE_DAY_RECORDED + "_" + Const.CHARACTER_PERCENT;
		String schedule7 = Const.CHARACTER_PERCENT + "______" + Const.SCHEDULE_PRE_DAY_RECORDED + Const.CHARACTER_PERCENT;

		List<SchedulePreUserDto> schedulePreDayDtoList = new ArrayList<>();

		//dayの値に応じて取得するスケジュールの日付を変える
		if ("1".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay1BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("2".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay2BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("3".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay3BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("4".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay4BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("5".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay5BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("6".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay6BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("7".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay7BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("8".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay8BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("9".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay9BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("10".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay10BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("11".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay11BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("12".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay12BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("13".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay13BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("14".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay14BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("15".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay15BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("16".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay16BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("17".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay17BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("18".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay18BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("19".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay19BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("20".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay20BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("21".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay21BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("22".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay22BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("23".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay23BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("24".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay24BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("25".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay25BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("26".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay26BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("27".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay27BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("28".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay28BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("29".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay29BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("30".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay30BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		} else if ("31".equals(day)) {
			schedulePreDayDtoList = schedulePreUserRepository.selectScheduleDay31BySchedulRepalceValueYm(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7, Const.SCHEDULE_PRE_DAY_RECORDED, ym);
		}

		return schedulePreDayDtoList;
	}
}

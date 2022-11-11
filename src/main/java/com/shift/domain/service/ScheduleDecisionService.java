package com.shift.domain.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shift.common.CmnScheduleLogic;
import com.shift.common.CmnScheduleTimeLogic;
import com.shift.common.CommonLogic;
import com.shift.common.CommonUtil;
import com.shift.common.Const;
import com.shift.common.ExcelLogic;
import com.shift.domain.model.bean.CmnScheduleCalendarBean;
import com.shift.domain.model.bean.CmnScheduleUserNameBean;
import com.shift.domain.model.bean.ScheduleDecisionBean;
import com.shift.domain.model.bean.ScheduleDecisionDownloadShitXlsxBean;
import com.shift.domain.model.bean.ScheduleDecisionModifyBean;
import com.shift.domain.model.bean.ScheduleDecisionModifyModifyBean;
import com.shift.domain.model.bean.ScheduleDecisionReportBean;
import com.shift.domain.model.bean.ScheduleDecisionReportSearchCountBean;
import com.shift.domain.model.bean.ScheduleDecisionReportSearchTimeBean;
import com.shift.domain.model.bean.ScheduleTimeBean;
import com.shift.domain.model.dto.ScheduleDayDto;
import com.shift.domain.model.dto.SchedulePreDayDto;
import com.shift.domain.model.dto.ScheduleUserNameDto;
import com.shift.domain.model.dto.ScheduleWorkCountMonthDto;
import com.shift.domain.model.dto.ScheduleWorkCountYearDto;
import com.shift.domain.model.dto.ScheduleYearDto;
import com.shift.domain.model.entity.ScheduleEntity;
import com.shift.domain.model.entity.ScheduleTimeEntity;
import com.shift.domain.model.entity.UserEntity;
import com.shift.domain.repository.ScheduleDayRepository;
import com.shift.domain.repository.SchedulePreDayRepository;
import com.shift.domain.repository.ScheduleRepository;
import com.shift.domain.repository.ScheduleTimeRepository;
import com.shift.domain.repository.ScheduleUserNameRepository;
import com.shift.domain.repository.ScheduleWorkCountMonthRepository;
import com.shift.domain.repository.ScheduleWorkCountYearRepository;
import com.shift.domain.repository.ScheduleYearRepository;
import com.shift.domain.repository.UserRepository;
import com.shift.domain.service.common.CmnScheduleCalendarService;
import com.shift.domain.service.common.CmnScheduleUserNameService;
import com.shift.form.ScheduleDecisionModifyForm;

/**
 * @author saito
 *
 */
@Service
@Transactional
@PropertySource(value = "classpath:excel.properties")
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
	private ScheduleWorkCountMonthRepository scheduleWorkCountMonthRepository;

	@Autowired
	private ScheduleWorkCountYearRepository scheduleWorkCountYearRepository;

	@Autowired
	private ScheduleYearRepository scheduleYearRepository;

	@Autowired
	private ScheduleUserNameRepository scheduleUserNameRepository;

	@Autowired
	private CmnScheduleCalendarService cmnScheduleCalendarService;

	@Autowired
	private CmnScheduleUserNameService cmnScheduleUserNameService;

	@Value("${excel.schedule-templete-file-path}")
	private String templeteFilePath;

	@Value("${excel.schedule-out-file-path}")
	private String outFilePath;

	@Value("${excel.schedule-download-file-name}")
	private String downloadFileName;

	@Value("${excel.schedule-cell-sheet-name}")
	private String cellSheetName;

	@Value("${excel.schedule-cell-name-base}")
	private String cellNameBase;


	/**
	 * 確定スケジュール画面取得機能<br>
	 * [Service] (/schedule-decision)
	 *
	 * @param ym RequestParameter
	 * @return ScheduleDecisionBean
	 */
	public ScheduleDecisionBean scheduleDecision(String ym) {

		//CmnScheduleCalendarServiceからカレンダー, 年月, 最終日を取得
		CmnScheduleCalendarBean cmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendarYmByYm(ym);
		//CmnScheduleUserNameServiceから2次元配列の確定スケジュール, スケジュール時間区分を取得
		CmnScheduleUserNameBean cmnScheduleUserNameBean = cmnScheduleUserNameService.generateScheduleRecordedUserNameByYm(cmnScheduleCalendarBean.getYear(), cmnScheduleCalendarBean.getMonth(), cmnScheduleCalendarBean.getLastDateYmd());

		//Beanにセット
		ScheduleDecisionBean scheduleDecisionBean = new ScheduleDecisionBean();
		scheduleDecisionBean.setYear(cmnScheduleCalendarBean.getYear());
		scheduleDecisionBean.setMonth(cmnScheduleCalendarBean.getMonth());
		scheduleDecisionBean.setNowYm(cmnScheduleCalendarBean.getNowYm());
		scheduleDecisionBean.setCalendarList(cmnScheduleCalendarBean.getCalendarList());
		scheduleDecisionBean.setUserScheduleAllArray(cmnScheduleUserNameBean.getUserScheduleAllArray());
		scheduleDecisionBean.setAfterYm(cmnScheduleCalendarBean.getNextYm());
		scheduleDecisionBean.setBeforeYm(cmnScheduleCalendarBean.getBeforeYm());
		scheduleDecisionBean.setScheduleTimeEntity(cmnScheduleUserNameBean.getScheduleTimeEntity());
		return scheduleDecisionBean;
	}


	/**
	 * 確定スケジュール勤務状況取得機能<br>
	 * [Service] (/schedule-decision/report)
	 *
	 * @param ym RequestParameter 取得する確定スケジュールの年月
	 * @return ScheduleDecisionReportBean
	 */
	public ScheduleDecisionReportBean scheduleDecisionReport(String ym) {

		//ymが指定されていないとき、現在のymを取得し
		String trimYm = ym;
		if (trimYm == null) {
			LocalDate nowLd = LocalDate.now();
			trimYm = new CommonLogic().toStringYmFormatSixByYearMonth(nowLd.getYear(), nowLd.getMonthValue());
		}
		//year, month, dayをそれぞれStringの配列で取得
		String[] yearMonthDayArray = calcYearMonthDayArray(trimYm, "01");
		//月次の勤務日数を取得
		List<ScheduleWorkCountMonthDto> scheduleWorkCountMonthList = selectScheduleWorkCountMonth(trimYm, yearMonthDayArray[0]);
		//年次の勤務日数を取得
		List<ScheduleWorkCountYearDto> scheduleWorkCountYearList = selectScheduleWorkCountYear(yearMonthDayArray[0]);
		//ユーザごとの1年分のスケジュールを取得
		List<ScheduleYearDto> scheduleYearList = selectScheduleYear(yearMonthDayArray[0]);
		//ユーザごとの1ヵ月分のスケジュールを取得
		List<ScheduleUserNameDto> scheduleMonthList = selectScheduleUserName(trimYm, yearMonthDayArray[0]);
		//1年分のスケジュール時間区分を取得
		List<ScheduleTimeEntity> scheduleTimeList = selectScheduleTimeForYear(yearMonthDayArray[0]);
		//指定された月をintで取得
		int month = Integer.parseInt(yearMonthDayArray[1]);
		//指定された月に該当する月のユーザごとの1ヵ月の労働時間を計算
		String[] scheduleWorkTimeMonthArray = calcScheduleWorkTimeMonth(scheduleMonthList, scheduleTimeList.get(month - 1));
		//ユーザごとの1年の労働時間を計算
		String[] scheduleWorkTimeYearArray = calcScheduleWorkTimeYear(scheduleYearList, scheduleTimeList);

		//Beanにセット
		ScheduleDecisionReportBean scheduleDecisionReportBean = new ScheduleDecisionReportBean();
		scheduleDecisionReportBean.setYear(Integer.parseInt(yearMonthDayArray[0]));
		scheduleDecisionReportBean.setMonth(Integer.parseInt(yearMonthDayArray[1]));
		scheduleDecisionReportBean.setScheduleWorkCountMonthList(scheduleWorkCountMonthList);
		scheduleDecisionReportBean.setScheduleWorkCountYearList(scheduleWorkCountYearList);
		scheduleDecisionReportBean.setScheduleWorkTimeMonthArray(scheduleWorkTimeMonthArray);
		scheduleDecisionReportBean.setScheduleWorkTimeYearArray(scheduleWorkTimeYearArray);
		return scheduleDecisionReportBean;
	}


	/**
	 * 確定スケジュール勤務日数追加取得機能(非同期)<br>
	 * [Service] (/schedule-decision/report/search-count)
	 *
	 * @param ym RequestParameter 取得する確定スケジュールの年月
	 * @return ScheduleDecisionReportSearchCountBean
	 */
	public ScheduleDecisionReportSearchCountBean scheduleDecisionReportSearchCount(String ym) {

		//year, month, dayをそれぞれStringの配列で取得
		String[] yearMonthDayArray = calcYearMonthDayArray(ym, "01");
		//月次の勤務日数を取得
		List<ScheduleWorkCountMonthDto> scheduleWorkCountMonthList = selectScheduleWorkCountMonth(ym, yearMonthDayArray[0]);

		//Beanにセット
		ScheduleDecisionReportSearchCountBean scheduleDecisionReportSearchCountBean = new ScheduleDecisionReportSearchCountBean();
		scheduleDecisionReportSearchCountBean.setYear(Integer.parseInt(yearMonthDayArray[0]));
		scheduleDecisionReportSearchCountBean.setMonth(Integer.parseInt(yearMonthDayArray[1]));
		scheduleDecisionReportSearchCountBean.setScheduleWorkCountMonthList(scheduleWorkCountMonthList);
		return scheduleDecisionReportSearchCountBean;
	}


	/**
	 * 確定スケジュール勤務時間追加取得機能(非同期)<br>
	 * [Service] (/schedule-decision/report/search-time)
	 *
	 * @param ym RequestParameter 取得する確定スケジュールの年月
	 * @return ScheduleDecisionReportSearchTimeBean
	 */
	public ScheduleDecisionReportSearchTimeBean scheduleDecisionReportSearchTime(String ym) {

		//year, month, dayをそれぞれStringの配列で取得
		String[] yearMonthDayArray = calcYearMonthDayArray(ym, "01");
		//ユーザごとの1ヵ月分のスケジュールを取得
		List<ScheduleUserNameDto> scheduleMonthList = selectScheduleUserName(ym, yearMonthDayArray[0]);
		//スケジュール時間区分を取得
		ScheduleTimeEntity scheduleTimeEntity = selectScheduleTime(ym + "01");
		//指定された月に該当する月のユーザごとの1ヵ月の労働時間を計算
		String[] scheduleWorkTimeMonthArray = calcScheduleWorkTimeMonth(scheduleMonthList, scheduleTimeEntity);

		//Beanにセット
		ScheduleDecisionReportSearchTimeBean scheduleDecisionReportSearchTimeBean = new ScheduleDecisionReportSearchTimeBean();
		scheduleDecisionReportSearchTimeBean.setYear(Integer.parseInt(yearMonthDayArray[0]));
		scheduleDecisionReportSearchTimeBean.setMonth(Integer.parseInt(yearMonthDayArray[1]));
		scheduleDecisionReportSearchTimeBean.setScheduleWorkTimeMonthArray(scheduleWorkTimeMonthArray);
		return scheduleDecisionReportSearchTimeBean;
	}


	/**
	 * 確定スケジュールExcel出力機能<br>
	 * [Service] (/schedule-decision/download/shift.xlsx)
	 *
	 * @param ym RequestParameter ダウンロードする確定スケジュールの年月
	 * @return ScheduleDecisionDownloadShitXlsxBean
	 */
	public ScheduleDecisionDownloadShitXlsxBean scheduleDecisionDownloadShitXlsx(String ym) {

		//CmnScheduleCalendarServiceからカレンダー, 年月, 最終日を取得
		CmnScheduleCalendarBean cmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendarYmByYm(ym);
		//CmnScheduleUserNameServiceから2次元配列の確定スケジュール, スケジュール時間区分を取得
		CmnScheduleUserNameBean cmnScheduleUserNameBean = cmnScheduleUserNameService.generateScheduleRecordedUserNameByYm(cmnScheduleCalendarBean.getYear(), cmnScheduleCalendarBean.getMonth(), cmnScheduleCalendarBean.getLastDateYmd());
		//スケジュールをExcel書き出し
		writeExcelForSchedule(cmnScheduleCalendarBean.getCalendarList(), cmnScheduleUserNameBean.getUserScheduleAllArray(), cmnScheduleUserNameBean.getScheduleTimeEntity(), cmnScheduleCalendarBean.getYear(), cmnScheduleCalendarBean.getMonth());
		//出力されるファイル名に年月を追加
		String trimDownloadFileName = downloadFileName.replaceAll("YM", ym);

		//Beanにセット
		ScheduleDecisionDownloadShitXlsxBean scheduleDecisionDownloadShitXlsxBean = new ScheduleDecisionDownloadShitXlsxBean();
		scheduleDecisionDownloadShitXlsxBean.setOutFilePath(outFilePath);
		scheduleDecisionDownloadShitXlsxBean.setDownloadFileName(trimDownloadFileName);
		return scheduleDecisionDownloadShitXlsxBean;
	}


	/**
	 * 確定スケジュール修正画面取得機能<br>
	 * [Service] (/schedule-decision/modify)
	 *
	 * @param ym RequestParameter
	 * @param day RequestParameter
	 * @return ScheduleDecisionModifyBean
	 */
	public ScheduleDecisionModifyBean scheduleDecisionModify(String ym, String day) {

		//CmnScheduleCalendarServiceからカレンダー, 年月, 最終日を取得
		CmnScheduleCalendarBean cmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendarYmByYm(ym);
		//year, month, dayをそれぞれStringの配列で取得
		String[] yearMonthDayArray = calcYearMonthDayArray(ym, day);
		//1日分の予定スケジュールをユーザ毎に取得
		List<SchedulePreDayDto> schedulePreDayList = selectSchedulePreDay(ym, day);
		//1日分の確定スケジュールをユーザ毎に取得
		List<ScheduleDayDto> scheduleUserList = selectScheduleDay(ym, day);
		//スケジュール時間区分を取得
		ScheduleTimeEntity scheduleTimeEntity = selectScheduleTime(cmnScheduleCalendarBean.getLastDateYmd());
		//未退職ユーザを全て取得
		List<UserEntity> userDbList = selectUserNotDelFlg();
		//確定スケジュールに登録済みのユーザを除くユーザListに変換
		List<UserEntity> userList = calcUserListForNewScheduleRecorded(scheduleUserList, userDbList);

		//Beanにセット
		ScheduleDecisionModifyBean scheduleDecisionModifyBean = new ScheduleDecisionModifyBean();
		scheduleDecisionModifyBean.setYear(yearMonthDayArray[0]);
		scheduleDecisionModifyBean.setMonth(yearMonthDayArray[1]);
		scheduleDecisionModifyBean.setDay(yearMonthDayArray[2]);
		scheduleDecisionModifyBean.setSchedulePreDayList(schedulePreDayList);
		scheduleDecisionModifyBean.setScheduleDayList(scheduleUserList);
		scheduleDecisionModifyBean.setScheduleTimeEntity(scheduleTimeEntity);
		scheduleDecisionModifyBean.setUserList(userList);
		return scheduleDecisionModifyBean;
	}


	/**
	 * 確定スケジュール修正機能<br>
	 * [Service] (/schedule-decision/modify/modify)
	 *
	 * @param scheduleDecisionModifyForm RequestParameter
	 * @return ScheduleDecisionModifyModifyBean
	 */
	public ScheduleDecisionModifyModifyBean scheduleDecisionModifyModify(ScheduleDecisionModifyForm scheduleDecisionModifyForm) {

		//確定スケジュールを更新する
		updateScheduleByScheduleDecsionModifyForm(scheduleDecisionModifyForm);
		//CmnScheduleCalendarServiceからカレンダー, 年月, 最終日を取得
		CmnScheduleCalendarBean cmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendarYmByYm(scheduleDecisionModifyForm.getYm());
		//year, month, dayをそれぞれStringの配列で取得
		String[] yearMonthDayArray = calcYearMonthDayArray(scheduleDecisionModifyForm.getYm(), scheduleDecisionModifyForm.getDay());
		//1日分の予定スケジュールをユーザ毎に取得
		List<SchedulePreDayDto> schedulePreUserList = selectSchedulePreDay(scheduleDecisionModifyForm.getYm(), scheduleDecisionModifyForm.getDay());
		//1日分の確定スケジュールをユーザ毎に取得
		List<ScheduleDayDto> scheduleUserList = selectScheduleDay(scheduleDecisionModifyForm.getYm(), scheduleDecisionModifyForm.getDay());
		//スケジュール時間区分を取得
		ScheduleTimeEntity scheduleTimeEntity = selectScheduleTime(cmnScheduleCalendarBean.getLastDateYmd());
		//未退職ユーザを全て取得
		List<UserEntity> userDbList = selectUserNotDelFlg();
		//スケジュール時間区分を取得
		List<UserEntity> userList = calcUserListForNewScheduleRecorded(scheduleUserList, userDbList);

		//Beanにセット
		ScheduleDecisionModifyModifyBean scheduleDecisionModifyModifyBean = new ScheduleDecisionModifyModifyBean();
		scheduleDecisionModifyModifyBean.setYear(yearMonthDayArray[0]);
		scheduleDecisionModifyModifyBean.setMonth(yearMonthDayArray[1]);
		scheduleDecisionModifyModifyBean.setDay(yearMonthDayArray[2]);
		scheduleDecisionModifyModifyBean.setSchedulePreDayList(schedulePreUserList);
		scheduleDecisionModifyModifyBean.setScheduleDayList(scheduleUserList);
		scheduleDecisionModifyModifyBean.setScheduleTimeEntity(scheduleTimeEntity);
		scheduleDecisionModifyModifyBean.setUserList(userList);
		return scheduleDecisionModifyModifyBean;
	}


	/**
	 * 年月日付取得処理
	 *
	 * <p>ym及びdayからyear, month, dayを計算し、配列で取得する<br>
	 * 年[0], 月[1]と日[2]
	 * </p>
	 *
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
	 *
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

		//userDbListの回数だけループする
		for (UserEntity userEntity: userDbList) {

			//scheduleRecordedUserIdListに含まれていないとき、userEntityをuserListに格納する
			if (!scheduleRecordedUserIdList.contains(userEntity.getId())) {
				userList.add(userEntity);
			}
		}

		return userList;
	}


	/**
	 * 月次確定スケジュール勤務時間計算処理
	 *
	 * <p>scheduleMonthListに登録されている確定スケジュールから1ヵ月のユーザごとの勤務時間を計算し、Stringの配列で返す<br>
	 * ユーザごとに時間フォーマット(HH.M)で格納される<br>
	 * ただし、scheduleMonthListがEmpty(登録済みのスケジュールがない)ときは何もせず要素0のString[]が返される
	 * </p>
	 *
	 * @param scheduleMonthList DBから取得したList<ScheduleUserNameDto> (List&lt;ScheduleUserNameDto&gt;)<br>
	 * ただし、Emptyのときは要素0のString[]が返される
	 * @param scheduleTimeEntity DBから取得したScheduleTimeEntity<br>
	 * ただし、nullのときは要素0のString[]が返される
	 * @return String[] ユーザごとの勤務時間
	 */
	private String[] calcScheduleWorkTimeMonth(List<ScheduleUserNameDto> scheduleMonthList, ScheduleTimeEntity scheduleTimeEntity) {

		//登録済みのスケジュールがないまたはスケジュール時間区分がないとき、要素0の配列を返す
		if (scheduleMonthList.isEmpty() || scheduleTimeEntity == null) {
			String[] scheduleWorkTime0Array = new String[0];
			return scheduleWorkTime0Array;
		}

		//ユーザごとの勤務時間を格納する変数
		String[] scheduleWorkTimeArray = new String[scheduleMonthList.size()];

		//それぞれのスケジュール時間区分から勤務時間を取得する
		Long[] workTimeMsArray = new CmnScheduleTimeLogic().toWorkTimeMsArrayByScheduleTime(scheduleTimeEntity);

		//スケジュールが登録されているか判定する共通クラス
		CmnScheduleLogic cmnScheduleLogic = new CmnScheduleLogic();

		//共通クラス
		CommonLogic commonLogic = new CommonLogic();

		//1ヵ月分の確定スケジュールに登録されているユーザだけループする
		for (int i = 0; i < scheduleMonthList.size(); i++) {

			//現在のループ目の要素iのScheduleUserNameDtoを取得
			ScheduleUserNameDto scheduleUserNameDto = scheduleMonthList.get(i);

			//ユーザの勤務時間(ミリ秒)の合計を格納する変数
			long sumWorkTimeMs = 0;

			//登録済みのスケジュールをListで取得
			List<String> dayList = scheduleUserNameDto.getDayList();

			//dayListの要素数だけループする
			for (String schedule: dayList) {

				//スケジュールが登録済みであるかどうかの判定をBooleanの配列で取得
				Boolean[] isScheduleRecordedArray = cmnScheduleLogic.toIsScheduleRecordedArrayBySchedule(schedule, scheduleTimeEntity);

				//1日ごとの判定したスケジュールの数だけループする
				for (int j = 0; j < isScheduleRecordedArray.length; j++) {

					//スケジュールが登録されているとき、スケジュールに対応する勤務時間をsumWorkTimeMsに追加する
					if (isScheduleRecordedArray[j] != null && isScheduleRecordedArray[j]) {
						sumWorkTimeMs += workTimeMsArray[j];
					}
				}
			}

			//判定で取得した勤務時間の合計値をworkTimeArrayに格納する
			scheduleWorkTimeArray[i] = commonLogic.toStringFormatTime(sumWorkTimeMs);
		}

		return scheduleWorkTimeArray;
	}


	/**
	 * 年次確定スケジュール勤務時間計算処理
	 *
	 * <p>scheduleYearListに登録されている確定スケジュールから1年のユーザごとの勤務時間を計算し、Stringの配列で返す<br>
	 * ユーザごとに時間フォーマット(HH.M)で格納される<br>
	 * ただし、scheduleMonthListがEmpty(登録済みのスケジュールがない)ときは何もせず要素0のString[]が返される
	 * </p>
	 *
	 * @param scheduleYearList DBから取得したList<ScheduleYearDto> (List&lt;ScheduleYearDto&gt;)<br>
	 * ただし、Emptyのときは要素0のString[]が返される
	 * @param scheduleTimeEntityList DBから取得したList<ScheduleTimeEntity> (List&lt;ScheduleTimeEntity&gt;)<br>
	 * ただし、Emptyのときは要素0のString[]が返される
	 * @return String[] ユーザごとの勤務時間
	 */
	private String[] calcScheduleWorkTimeYear(List<ScheduleYearDto> scheduleYearList, List<ScheduleTimeEntity> scheduleTimeEntityList) {

		//登録済みのスケジュールがないまたはスケジュール時間区分がないとき、とき、要素0の配列を返す
		if (scheduleYearList.isEmpty() || scheduleTimeEntityList.isEmpty()) {
			String[] scheduleWorkTime0Array = new String[0];
			return scheduleWorkTime0Array;
		}

		//ユーザごとの勤務時間を格納する変数
		String[] scheduleWorkTimeArray = new String[scheduleYearList.size()];

		//それぞれの月とスケジュール時間区分から勤務時間を保持する配列 [月][該当月のスケジュール時間区分]
		Long[][] workTimeMsArray2 = new Long[scheduleTimeEntityList.size()][Const.SCHEDULE_RECORDABLE_MAX_DIVISION];

		//スケジュール時間区分をの勤務時間を計算するための共通クラス
		CmnScheduleTimeLogic cmnScheduleTimeLogic = new CmnScheduleTimeLogic();

		//スケジュールが登録されているか判定する共通クラス
		CmnScheduleLogic cmnScheduleLogic = new CmnScheduleLogic();

		//共通クラス
		CommonLogic commonLogic = new CommonLogic();

		//取得したスケジュール時間区分の要素数だけループする
		for (int i = 0; i < scheduleTimeEntityList.size(); i++) {

			//それぞれのスケジュール時間区分の勤務時間(ミリ秒)を計算し、格納
			Long[] workTimeMsArray = cmnScheduleTimeLogic.toWorkTimeMsArrayByScheduleTime(scheduleTimeEntityList.get(i));
			workTimeMsArray2[i] = workTimeMsArray;
		}

		//1ヵ月分の確定スケジュールに登録されているユーザだけループする
		for (int i = 0; i < scheduleYearList.size(); i++) {

			//現在のループ目の要素iのScheduleUserNameDtoを取得
			ScheduleYearDto scheduleYearDto = scheduleYearList.get(i);

			//ユーザの勤務時間(ミリ秒)の合計を格納する変数
			long sumWorkTimeMs = 0;

			//登録済みのスケジュールをListで取得
			List<String> dayList = scheduleYearDto.getDayList();

			//dayListの要素数だけループする
			for (String schedule: dayList) {

				//workTimeMsArray2の要素数(登録されている月の数)だけループする
				for (int j = 0; j < workTimeMsArray2.length; j++) {

					//現在の月に該当するスケジュールの文字列を取得するために指定する文字列の桁数
					int startCharLength = j * Const.SCHEDULE_RECORDABLE_MAX_DIVISION;
					int endCharLength = (j + 1) * Const.SCHEDULE_RECORDABLE_MAX_DIVISION;

					//該当月のスケジュールを取得
					String monthSchedule = "";
					if (endCharLength <= schedule.length()) {

						//該当月のスケジュールを取得することができるとき、スケジュールを取得
						monthSchedule = schedule.substring(startCharLength, endCharLength);
					} else {

						//該当月のスケジュールを取得することができないとき、スケジュールが未登録であることを代入
						monthSchedule = Const.SCHEDULE_NOT_RECORDED_ALL;
					}


					//スケジュールが登録済みであるかどうかの判定をBooleanの配列で取得
					Boolean[] isScheduleRecordedArray = cmnScheduleLogic.toIsScheduleRecordedArrayBySchedule(monthSchedule, scheduleTimeEntityList.get(j));

					//1日ごとの判定したスケジュールの数だけループする
					for (int k = 0; k < isScheduleRecordedArray.length; k++) {

						//スケジュールが登録されているとき、スケジュールに対応する勤務時間をsumWorkTimeMsに追加する
						if (isScheduleRecordedArray[k] != null && isScheduleRecordedArray[k]) {
							sumWorkTimeMs += workTimeMsArray2[j][k];
						}
					}
				}
			}

			//判定で取得した勤務時間の合計値をworkTimeArrayに格納する
			scheduleWorkTimeArray[i] = commonLogic.toStringFormatTime(sumWorkTimeMs);
		}

		return scheduleWorkTimeArray;
	}


	/**
	 * 確定スケジュールExcell書き込み処理
	 *
	 * <p>Excell(テンプレート)を取得し、スケジュール時間区分及び確定スケジュールに登録されているユーザをExcelに書き出す<br>
	 * ただし、引数がnullまたはEmpty, Excell及び指定したセルに書き込めないときはエラーとなる
	 * </p>
	 *
	 * @param calendarList CmnScheduleCalendarServiceから取得したカレンダー (List&lt;Integer&gt;)
	 * @param userScheduleAllArray CmnScheduleCalendarServiceから取得した配列
	 * @param scheduleTimeEntity CmnScheduleCalendarServiceから取得したスケジュール時間区分
	 * @param year CmnScheduleCalendarServiceから取得した年
	 * @param month CmnScheduleCalendarServiceから取得した月
	 * @return boolean<br>
	 * true: スケジュールのExcelへの書き出しが成功したとき<br>
	 * false: スケジュールのExcelへの書き出しが失敗したとき
	 */
	private boolean writeExcelForSchedule(List<Integer> calendarList, String[][] userScheduleAllArray, ScheduleTimeEntity scheduleTimeEntity, int year, int month) {

		try (FileInputStream fileInputStream = new FileInputStream(templeteFilePath);
				Workbook workBook = WorkbookFactory.create(fileInputStream);
				OutputStream fileOutputStream =  new FileOutputStream(outFilePath);) {

			//ワークブックからシートを取得
			Sheet sheet1 = workBook.getSheet(cellSheetName);

			//Apach POIを扱うLogicクラス
			ExcelLogic excelLogic = new ExcelLogic();

			//セルに改行コードが存在するとき折り返して全体を表示するように設定
			CellStyle cellStyle = workBook.createCellStyle();
			cellStyle.setWrapText(true);

			//---------------------
			//Cellへ日付の書き込み
			//---------------------

			//年月を日付フォーマットに変換
			String date = String.valueOf(year) + "年" + String.valueOf(month) + "月";

			//列情報を取得
			Row cellDateRow = excelLogic.getRow(workBook, sheet1, cellNameBase, 0);

			//セルへ書き込み
			excelLogic.writeCellValueForCell(cellDateRow, 1, date);

			//--------------------------
			//Cellへカレンダーの書き込み
			//--------------------------

			//値を挿入したいセルの列を指定(ベースとなるセルからの距離)
			int distanceBaseCellRow = 1;

			//列情報を取得
			Row cellCalendarRow = excelLogic.getRow(workBook, sheet1, cellNameBase, distanceBaseCellRow);

			//シフトの文字をセルへ書き込み
			excelLogic.writeCellValueForCell(cellCalendarRow, 0, "シフト");

			//calendarListの要素数だけループする
			for (int i = 0; i < calendarList.size(); i++) {

				//カレンダーの日付を取得
				Integer calendarDay = calendarList.get(i);

				//日付が取得できなかったとき、ループに戻る
				if (calendarDay == null) {
					continue;
				}

				//書き込む行をカレンダーの日付から取得
				int cols = calendarDay;

				//セルへ書き込み
				excelLogic.writeCellValueForCell(cellCalendarRow, cols, String.valueOf(calendarDay));
			}

			//-----------------------------
			//Cellへスケジュールの書き込み
			//-----------------------------

			//書き込む対象の列を1列下げる
			distanceBaseCellRow++;

			//スケジュール時間区分をListで取得
			List<ScheduleTimeBean> ScheduleTimeList = scheduleTimeEntity.scheduleTimeFormatList();

			//スケジュール時間区分の要素数だけループする
			for (int i = 0; i < ScheduleTimeList.size(); i++) {

				//ループ回数目のスケジュール時間区分を取得
				ScheduleTimeBean scheduleTimeBean = ScheduleTimeList.get(i);

				//Cellに書き出すスケジュール時間区分の情報を取得
				String scheduleTime = scheduleTimeBean.getName() + Const.CHARACTER_CODE_BREAK_LINE + scheduleTimeBean.startHmsFormatTime() + "～" + scheduleTimeBean.endHmsFormatTime();

				//列情報を取得
				Row cellScheduleRow = excelLogic.getRow(workBook, sheet1, cellNameBase, distanceBaseCellRow);

				//セルへ書き込み、改行表示スタイルを適用
				Cell scheduleTimeCell = excelLogic.writeCellValueForCell(cellScheduleRow, 0, scheduleTime);
				scheduleTimeCell.setCellStyle(cellStyle);

				//スケジュールの要素(日付)だけループする
				for (int j = 0; j < userScheduleAllArray.length; j++) {

					//スケジュール時間iと日付jに該当するスケジュールを取得
					String schedule = CommonUtil.changeEmptyByNull(userScheduleAllArray[j][i]);

					//HTMLの改行タグを改行コードに変換
					String trimSchedule = schedule.replaceAll(Const.HTML_TAG_BR, Const.CHARACTER_CODE_BREAK_LINE);

					//書き込む行をループ回数目 + 1行目に指定
					int cols = j + 1;

					//セルへ書き込み、改行表示スタイルを適用
					Cell scheduleCell = excelLogic.writeCellValueForCell(cellScheduleRow, cols, trimSchedule);
					scheduleCell.setCellStyle(cellStyle);
				}

				//書き込む対象の列を1列下げる
				distanceBaseCellRow++;
			}


			//書き込んだセルをExcelへ書き出し、trueを返す
			excelLogic.writeAllCellForExcel(workBook, fileOutputStream);
			return true;
		} catch (Exception e) {

			//例外発生時、ログを出力しfalseを返す
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * [DB]勤務日数(月次)検索処理
	 *
	 * <p>確定スケジュールから該当する年月の勤務日数をユーザ毎に取得する<br>
	 * 該当する年月にスケジュールが未登録でも該当する年にスケジュールが登録済みであれば、出勤日数がユーザ毎に取得される<br>
	 * ただし、該当する年にもスケジュールが登録されていないときはEmptyとなる
	 * </p>
	 *
	 * @param ym 取得したい出勤日の年月
	 * @param year 取得したい出勤日の年月に該当する年
	 * @return List<ScheduleWorkCountMonthDto><br>
	 * フィールド(List&lt;ScheduleWorkCountMonthDto&gt;)<br>
	 * userId, userName, workCount
	 */
	private List<ScheduleWorkCountMonthDto> selectScheduleWorkCountMonth(String ym, String year) {

		//LIKEで検索されるため、yearのフォーマットをYYYY%に変える
		String trimYear = year + Const.CHARACTER_PERCENT;

		List<ScheduleWorkCountMonthDto> scheduleWorkCountMonthList = scheduleWorkCountMonthRepository.selectScheduleWorkCountMonth(ym, trimYear);
		return scheduleWorkCountMonthList;
	}


	/**
	 * [DB]勤務日数(年次)検索処理
	 *
	 * <p>確定スケジュールから該当する年の勤務日数をユーザ毎に取得する<br>
	 * ただし、該当する年にスケジュールが登録されていないときはEmptyとなる
	 * </p>
	 *
	 * @param year 取得したい出勤日の年月に該当する年
	 * @return List<ScheduleWorkCountYearDto><br>
	 * フィールド(List&lt;ScheduleWorkCountYearDto&gt;)<br>
	 * userId, userName, workCount
	 */
	private List<ScheduleWorkCountYearDto> selectScheduleWorkCountYear(String year) {

		//LIKEで検索されるため、yearのフォーマットをYYYY%に変える
		String trimYear = year + Const.CHARACTER_PERCENT;

		List<ScheduleWorkCountYearDto> scheduleWorkCountYearList = scheduleWorkCountYearRepository.selectScheduleWorkCountYear(trimYear);
		return scheduleWorkCountYearList;
	}


	/**
	 * [DB]ユーザの1年分確定スケジュール検索処理
	 *
	 * <p>ユーザーごとの1年分の確定スケジュールを取得する<br>
	 * 1年分(1～12月)のスケジュールが結合されて取得される<br>
	 * ただし、1年間(1～12月)のうち登録済みのスケジュールが登録されていないときはEmptyとなる<br>
	 * また、日付が存在しない日(2月 -> 30, 31日etc)は必ず登録されていない
	 * </p>
	 *
	 * @param year 取得したい年
	 * @return ScheduleYearDto<br>
	 * フィールド(ScheduleYearDto)<br>
	 * id, userName, 1, 2, 3, 4, 5... 30, 31
	 */
	private List<ScheduleYearDto> selectScheduleYear(String year){

		//取得したい年に対応する月(1～12月)をymで取得
		String ym1 = year + "01";
		String ym2 = year + "02";
		String ym3 = year + "03";
		String ym4 = year + "04";
		String ym5 = year + "05";
		String ym6 = year + "06";
		String ym7 = year + "07";
		String ym8 = year + "08";
		String ym9 = year + "09";
		String ym10 = year + "10";
		String ym11 = year + "11";
		String ym12 = year + "12";

		//yearを"YYYY%"に変換
		String trimYear = year + Const.CHARACTER_PERCENT;

		//ユーザーごとの1年分のスケジュールを取得し、返す
		List<ScheduleYearDto> scheduleYearDtoList = scheduleYearRepository.selectScheduleYear(trimYear, ym1, ym2, ym3, ym4, ym5, ym6, ym7, ym8, ym9, ym10, ym11, ym12);
		return scheduleYearDtoList;
	}


	/**
	 * [DB]年間の確定スケジュール登録済みユーザ取得処理
	 *
	 * <p>年間を通した確定スケジュールに登録済みのユーザを取得する<br>
	 * 対象の年月にスケジュールが登録されていなくても、その年の一度でもスケジュールが登録されていれば取得される
	 * ただし、取得したい年月の年に登録済みのユーザが1人もないときはEmptyとなる
	 * </p>
	 *
	 * @param ym 取得したい年月
	 * @param year 取得したい年月の年<br>
	 * ただし、LIKE検索されるため"2022%"でなければならない
	 * @return List<ScheduleUserNameDto><br>
	 * フィールド(&lt;ScheduleUserNameDto&gt;)<br>
	 * id, userName, day1, day2, day3... day30, day31
	 */
	private List<ScheduleUserNameDto> selectScheduleUserName(String ym, String year){

		//yearを"YYYY%"に変換
		String trimYear = year + Const.CHARACTER_PERCENT;

		//ユーザーごとの1ヵ月分のスケジュールを取得し、返す
		List<ScheduleUserNameDto> scheduleUserNameDto = scheduleUserNameRepository.selectScheduleUserNameForYear(ym, trimYear);
		return scheduleUserNameDto;
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
	 * [DB]年間スケジュール時間区分取得処理
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
	private List<ScheduleTimeEntity> selectScheduleTimeForYear(String year) {

		//取得したスケジュール時間区分を格納する変数
		List<ScheduleTimeEntity> scheduleTimeEntityList = new ArrayList<>();

		//日付を取得するための共通ロジッククラス
		CommonLogic commonLogic = new CommonLogic();

		//1年分(12回)ループする
		for (int i = 1; i <= 12; i++) {

			//現在のループから日付を取得
			String ymd = commonLogic.toStringYmdByYearMonthDay(Integer.parseInt(year), i, 1);

			//取得した日付に該当するスケジュール時間区分を取得し、Listに格納
			ScheduleTimeEntity scheduleTimeEntity = scheduleTimeRepository.selectScheduleTimeByYmd(ymd);
			scheduleTimeEntityList.add(scheduleTimeEntity);
		}
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

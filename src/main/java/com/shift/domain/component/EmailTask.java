package com.shift.domain.component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shift.common.CommonLogic;
import com.shift.common.Const;
import com.shift.common.EmailLogic;
import com.shift.domain.model.entity.SchedulePreEntity;
import com.shift.domain.model.entity.UserEntity;
import com.shift.domain.repository.SchedulePreRepository;
import com.shift.domain.repository.UserRepository;

/**
 * @author saito
 *
 */
@Component
@PropertySource(value = "classpath:task.properties")
public class EmailTask {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SchedulePreRepository schedulePreRepository;

	@Autowired
	private MailSender mailSender;


	/**
	 * [Task] シフト予定未提出ユーザのメール自動送信処理
	 *
	 * <p>指定日時を経過したとき、来月のスケジュール予定の未提出ユーザにシフト未提出のメールを自動で送信する<br>
	 * ただし、ユーザにメールが設定されていない場合は送信されない<br>
	 * バッチ処理開始日時はtask.propertiesに記載されている
	 * </p>
	 *
	 * @param void
	 * @return void
	 */
	@Scheduled(cron = "${task.send-mail-schedule-unsubmitted-cron}", zone = "${task.send-mail-schedule-unsubmitted-zone}")
	public void sendMailScheduleUnsubmittedScheduled() {

		//来月のymを取得
		String nextMonthYm = calcNextMonthYm();
		//未退職ユーザを取得
		List<UserEntity> userList = selectUserNotDelFlg();
		//来月のスケジュール予定の提出済みユーザを取得
		List<SchedulePreEntity> SchedulePreList = selectSchedulePreForNextMonth(nextMonthYm);
		//未提出ユーザにメールを送信する
		sendMailForUnsubmittedUser(userList, SchedulePreList);
	}


	/**
	 * 来月のym取得処理
	 *
	 * <p>来月のym(YYYYMM)を取得する</p>
	 *
	 * @param void
	 * @return String 来月のym(YYYYMM)
	 */
	private String calcNextMonthYm() {

		//来月の日付をLocalDateで取得
		LocalDate nowLd = LocalDate.now();
		LocalDate nextMonthLd = nowLd.plusMonths(1);

		//フォーマットを来月のym(YYYYMM)に変換し、返す
		String nextMonthYm = new CommonLogic().toStringYmFormatSixByYearMonth(nextMonthLd.getYear(), nextMonthLd.getMonthValue());
		return nextMonthYm;
	}


	/**
	 * スケジュール予定未提出ユーザメール処理
	 *
	 * <p>スケジュール予定を提出していないユーザを判定し、スケジュール予定の未提出ユーザに未提出メールを送信する<br>
	 * ただし、ユーザにメールが設定されていない場合は送信されない
	 * </p>
	 *
	 * @param void
	 * @return boolean<br>
	 * true: 未提出ユーザにメールを送信処理が全て成功したとき<br>
	 * false: 未提出ユーザにメールを送信中に異常が発生したとき
	 */
	private boolean sendMailForUnsubmittedUser(List<UserEntity> userList, List<SchedulePreEntity> SchedulePreList) {

		//スケジュール予定提出済みユーザのIDを格納するための変数
		List<String> submittedUserList = new ArrayList<>();

		//提出されたスケジュール予定の回数だけループし、ユーザIDを格納する
		for (SchedulePreEntity schedulePreEntity: SchedulePreList) {
			submittedUserList.add(schedulePreEntity.getUser());
		}

		//メールを送信するためのクラス
		EmailLogic emailLogic = new EmailLogic();

		//メールのタイトル及び内容を設定
		String emailTitle = "シフト未提出のお知らせ";
		String templateContent = "来月のシフトが未提出です。" + Const.CHARACTER_CODE_BREAK_LINE + "シフトの提出をお願いします。";

		//登録済みの未退職ユーザの回数だけループする
		for (UserEntity userEntity : userList) {

			//スケジュール予定提出済みユーザと一致しない(スケジュール予定が未提出)とき
			if (!submittedUserList.contains(userEntity.getId())) {

				//メールの内容を設定し、メールを送信する
				String emailContent = userEntity.getName() + "さんの" + templateContent;
				emailLogic.sendEmail(mailSender, userEntity.getEmail(), emailTitle, emailContent);
			}
		}

		return true;
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
	 */
	private List<UserEntity> selectUserNotDelFlg() {

		List<UserEntity> userEntityList = userRepository.findByDelFlgNotOrDelFlgIsNull(Const.USER_DEL_FLG);
		return userEntityList;
	}


	/**
	 * [DB]スケジュール予定検索処理
	 *
	 * <p>年月から提出済みの全ユーザのスケジュール予定を取得する<br>
	 * ただし、登録済みのスケジュール予定がないときはEmptyとなる<br>
	 * また、日付が存在しない日(2月 -> 30, 31日etc)は必ず登録されていない
	 * </p>
	 *
	 * @param ym 現在の年月(YYYYMM)
	 * @return List<SchedulePreEntity> <br>
	 * フィールド(List&lt;SchedulePreEntity&gt;)<br>
	 * id, ym, user, 1, 2, 3, 4, 5... 30, 31
	 */
	private List<SchedulePreEntity> selectSchedulePreForNextMonth(String ym) {

		List<SchedulePreEntity> SchedulePreEntityList = schedulePreRepository.findByYm(ym);
		return SchedulePreEntityList;
	}
}

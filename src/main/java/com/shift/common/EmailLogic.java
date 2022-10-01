package com.shift.common;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * @author saito
 *
 */
public class EmailLogic {


	/**
	 * メール送信処理
	 *
	 * <p>メールアドレス及び送信するタイトル,内容からメールを送信する<br>
	 * ただし、引数が異常及び受信できるメールアドレスの送信可能文字数を超えている場合は必ず失敗する<br>
	 * (注意) Spring mail及びGmailの仕様上、Googleの2段階認証とアプリパスワードを設定したメールアドレスをapplication.propertiesに記述していないと必ず失敗する<br>
	 * メール内容に改行したい場合は改行タグ(\n\r)が必要になる
	 * </p>
	 *
	 * @param mailSender メールを送信するためのクラス
	 * @param sendToEmailAddress 送信先のメールアドレス<br>
	 * ただし、nullまたは存在しないメールアドレスのときは必ず失敗する
	 * @param emailTitle 送信するメールのタイトル<br>
	 * ただし、nullのときは必ず失敗する
	 * @param emailContent 送信するメールの内容<br>
	 * ただし、nullのときは必ず失敗する
	 * @return boolean<br>
	 * true: メール送信に成功したとき, false: メール送信に失敗したとき
	 */
	public boolean sendEmail(MailSender mailSender, String sendToEmailAddress, String emailTitle, String emailContent) {

		try {

			//メールのフォーマットを指定する変数
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

			//送信先メールアドレス
	    	simpleMailMessage.setTo(sendToEmailAddress);

	    	//メール件名をセット
	    	simpleMailMessage.setSubject(emailTitle);

	    	//メール内容をセット
	    	simpleMailMessage.setText(emailContent);

	    	//メールを送信
	    	mailSender.send(simpleMailMessage);

	    	//メール送信成功時、trueを返す
	    	return true;
		}catch (Exception e) {

			//例外発生時、falseを返す
	    	return false;
		}
	}
}

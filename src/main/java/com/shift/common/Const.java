package com.shift.common;

/**
 * @author saito
 *
 */
public class Const {
	private Const() {
		//インスタンス化を禁止
	}


	//-----
	//Dm
	//-----
	public static final String DM_READ_FLG = "1";
	public static final String HTML_CLASS_DM_MSG_LOGIN_USER = "login-user";
	public static final String HTML_CLASS_DM_MSG_NON_LOGIN_USER = "non-login-user";
	public static final int DM_CHAT_LIMIT = 20;


	//-----
	//Home
	//-----
	public static final int HOME_NEWS_SELECT_LIMIT = 5;
	public static final int HOME_NEWS_DISPLAY_NEW_ICON_LIMIT_DATE = 14;
	public static final String HOME_NEWS_NEW_ICON_SRC = "/png/icon/new-icon.png";
	public static final String HOME_NEWS_CATEGORY_ICON_SRC = "/png/icon/category-icon";
	public static final int HOME_DISPLAY_SCHEDULE_DAY = 7;


	//---------
	//Login
	//---------
	public static final int LOGIN_FOGOT_PASSWORD_AUTH_CODE_COUNT = 6;
	public static final int LOGIN_FOGOT_PASSWORD_URL_PARAM_COUNT = 100;


	//---------
	//NewsEdit
	//---------
	public static final String[] NEWS_CATEGORY_ALL_ARRAY = {"お知らせ", "重要", "シフト"};
	public static final int NEWS_RECORDABLE_MAX_MONTH = 3;


	//---------
	//Schedule
	//---------
	public static final String SCHEDULE_PRE_DAY_NOT_RECORDED = "0";
	public static final String SCHEDULE_PRE_DAY_RECORDED = "1";
	public static final String SCHEDULE_DAY_NOT_RECORDED = "0";
	public static final String SCHEDULE_DAY_RECORDED = "1";
	public static final int SCHEDULE_RECORDABLE_MAX_DIVISION = 7;
	public static final String SCHEDULE_NOT_RECORDED_ALL = "0000000";
	public static final String[] SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY = {"teal", "orange", "pink", "yellow", "purple", "cyan", "gray"};
	public static final String[] SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY = {"bg-teal", "bg-orange", "bg-pink", "bg-yellow", "bg-purple", "bg-cyan", "bg-gray"};
	public static final String[] SCHEDULE_HTML_CLASS_DISPLAY_BTN_COLOR_ARRAY = {"btn-teal", "btn-orange", "btn-pink", "btn-yellow", "btn-purple", "btn-cyan", "btn-gray"};
	public static final String SCHEDULE_TIME_INSERT_NEW_END_YMD = "99999999";
	public static final String SCHEDULE_TIME_INSERT_MODIFY_END_YMD = "00000000";


	//------
	//User
	//------
	public static final String USER_DEL_FLG = "1";
	public static final int USER_SELECT_LIMIT = 5;
	public static final int USER_LIST_PAGINATION_LIMIT_PAGE_ODD = 5;
	public static final String USER_GENDER_1_MAN = "男性";
	public static final String USER_GENDER_2_WOMEN = "女性";
	public static final String[] USER_GENDER_ALL_ARRAY = {"男性", "女性"};
	public static final String USER_ADMIN_FLG = "1";
	public static final String HTML_SRC_USER_DEFOULT_ICON = "/user-icon/common/guest.png";
	public static final String HTML_SRC_USER_ICON_DIR = "/user-icon/";
	public static final String USER_ICON_RECORD_ROOT_DIR = "static/user-icon/";
	public static final String[] USER_ICON_ALLOW_FILE_EXTENSION_ARRAY = {".jpg", ".jpeg", ".png"};
	public static final String USER_ICON_FLG_JPG = "1";
	public static final String USER_ICON_FLG_JPEG = "2";
	public static final String USER_ICON_FLG_PNG = "3";


	//-------
	//Common
	//-------

	//HTMLタグ
	public static final String HTML_TAG_BR = "<br>";
	public static final String HTML_SPACE = "&nbsp;";

	//記号
	public static final String CHARACTER_PERCENT = "%";
	public static final String CHARACTER_SPACE = " ";
	public static final String CHARACTER_CODE_BREAK_LINE = "\r\n";
	public static final String CHARACTER_CODE_BREAK_LINE_ALL = "\r\n|\r|\n";
	//セッション
	public static final String SESSION_KEYWORD_ACCOUNT_BEAN = "KEY1_SESSION_ACCOUNT_BEAN";
	public static final String SESSION_KEYWORD_DM_UNREAD_COUNT = "KEY2_SESSION_DM_UNREAD_COUNT";

	//ロール(役職)
	public static final String ROLE_USER_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_USER_GENERAL = "ROLE_GENERAL";

	//タイムゾーン
	public static final long SIMPLE_DATE_FORMAT_SERVER_TIME_ZONE_JP_DISTANCE = -32400000;

	//------------------------
	//バリデーションパターン
	//------------------------

	//共通
	public static final String PATTERN_CHARACTER_NOT_NG_CHAR = "[^!\"#$%&'()\\*\\+\\-\\.,\\/:;<=>?@\\[\\\\\\]^_`{|}~]+";
	public static final String PATTERN_CHARACTER_WHITESPACE = "[\r|\n|\r\n|\\s]+";

	public static final String PATTERN_ROLE_USER_ADMIN = "1";
	public static final String PATTERN_ROLE_USER_GENERAL = "^$";

	//dm
	public static final int PATTERN_DM_MSG_LENGTH_MIN_INPUT = 1;
	public static final int PATTERN_DM_MSG_LENGTH_MAX_INPUT = 200;
	public static final String PATTERN_DM_RECEIVE_USER_INPUT = "^[A-Za-z0-9]{4}";
	public static final int PATTERN_DM_RECEIVE_USER_LENGTH_MIN_INPUT = 1;
	public static final int PATTERN_DM_RECEIVE_USER_LENGTH_MAX_INPUT = 4;

	//news
	public static final String PATTERN_NEWS_CATEGORY_INPUT = "1|2|3";
	public static final int PATTERN_NEWS_CATEGORY_LENGTH_MIN_INPUT = 1;
	public static final int PATTERN_NEWS_CATEGORY_LENGTH_MAX_INPUT = 1;
	public static final int PATTERN_NEWS_CONTENT_LENGTH_MIN_INPUT = 1;
	public static final int PATTERN_NEWS_CONTENT_LENGTH_MAX_INPUT = 200;
	public static final int PATTERN_NEWS_TITLE_LENGTH_MIN_INPUT = 1;
	public static final int PATTERN_NEWS_TITLE_LENGTH_MAX_INPUT = 20;
	public static final String PATTERN_NEWS_YMD_INPUT = "^[0-9]{4}+(0[1-9]|1[0-2])+(0[1-9]|[12][0-9]|3[01])$";
	public static final int PATTERN_NEWS_YMD_LENGTH_MIN_INPUT = 8;
	public static final int PATTERN_NEWS_YMD_LENGTH_MAX_INPUT = 8;
	public static final String PATTERN_NEWS_UNIQUE_DATE_INPUT = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";

	//schedule
	public static final String PATTERN_SCHEDULE_YM_INPUT = "^[0-9]{4}+(0[1-9]|1[0-2])$";
	public static final int PATTERN_SCHEDULE_YM_LENGTH_MIN_INPUT = 6;
	public static final int PATTERN_SCHEDULE_YM_LENGTH_MAX_INPUT = 6;
	public static final String PATTERN_SCHEDULE_DAY_INPUT = "^(0[1-9]|[12][0-9]|3[01])$";
	public static final int PATTERN_SCHEDULE_DAY_LENGTH_MIN_INPUT = 2;
	public static final int PATTERN_SCHEDULE_DAY_LENGTH_MAX_INPUT = 2;
	public static final String PATTERN_SCHEDULE_USER_INPUT_OPTIONAL = "^[A-Za-z0-9]{4}|^$";
	public static final int PATTERN_SCHEDULE_USER_LENGTH_MIN_INPUT_OPTIONAL = 0;
	public static final int PATTERN_SCHEDULE_USER_LENGTH_MAX_INPUT_OPTIONAL = 4;

	//schedule_pre
	public static final String PATTERN_SCHEDULE_PRE_YM_INPUT = "^[0-9]{4}+(0[1-9]|1[0-2])$";
	public static final int PATTERN_SCHEDULE_PRE_YM_LENGTH_MIN_INPUT = 6;
	public static final int PATTERN_SCHEDULE_PRE_YM_LENGTH_MAX_INPUT = 6;

	//schedule_time
	public static final String PATTERN_SCHEDULE_TIME_UNIQUE_START_YM_INPUT = "^[0-9]{4}-(0[1-9]|1[0-2])$";
	public static final int PATTERN_SCHEDULE_TIME_UNIQUE_START_YM_LENGTH_MIN_INPUT = 7;
	public static final int PATTERN_SCHEDULE_TIME_UNIQUE_START_YM_LENGTH_MAX_INPUT = 7;
	public static final int PATTERN_SCHEDULE_TIME_NAME_LENGTH_MIN_INPUT = 1;
	public static final int PATTERN_SCHEDULE_TIME_NAME_LENGTH_MAX_INPUT = 20;
	public static final int PATTERN_SCHEDULE_TIME_NAME_LENGTH_MIN_INPUT_OPTIONAL = 0;
	public static final int PATTERN_SCHEDULE_TIME_NAME_LENGTH_MAX_INPUT_OPTIONAL = 20;
	public static final String PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";
	public static final int PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT = 5;
	public static final int PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT = 5;
	public static final String PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$|^$";
	public static final int PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL = 0;
	public static final int PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL = 5;


	//user
	public static final String PATTERN_USER_ADDRESS_INPUT = "^(?![\\s\\S]{101,})(^(\\s+)(\\S+).*$|^(\\S+)(\\s+).*$|\\S).*$|^$";
	public static final int PATTERN_USER_ADDRESS_LENGTH_MIN_INPUT = 0;
	public static final int PATTERN_USER_ADDRESS_LENGTH_MAX_INPUT = 100;
	public static final String PATTERN_USER_ADMIN_FLG = "1";
	public static final String PATTERN_USER_ADMIN_FLG_INPUT = "1|^$";
	public static final int PATTERN_USER_ADMIN_LENGTH_MIN_INPUT = 0;
	public static final int PATTERN_USER_ADMIN_LENGTH_MAX_INPUT = 1;
	public static final String PATTERN_USER_DEL_FLG = "1";
	public static final String PATTERN_USER_EMAIL_INPUT = "^[a-zA-Z0-9-_\\.]+@[a-zA-Z0-9-_\\.]+$|^$";
	public static final int PATTERN_USER_EMAIL_LENGTH_MIN_INPUT = 0;
	public static final int PATTERN_USER_EMAIL_LENGTH_MAX_INPUT = 254;
	public static final String PATTERN_USER_GENDER_INPUT = "1|2";
	public static final int PATTERN_USER_GENDER_LENGTH_MIN_INPUT = 1;
	public static final int PATTERN_USER_GENDER_LENGTH_MAX_INPUT = 1;
	public static final String PATTERN_USER_GENDER_1_MAN = "1";
	public static final String PATTERN_USER_GENDER_2_WOMEN = "2";
	public static final String PATTERN_USER_ICON_KBN_ALL = "1|2|3";
	public static final String PATTERN_USER_ICON_KBN_JPG = "1";
	public static final String PATTERN_USER_ICON_KBN_JPEG = "2";
	public static final String PATTERN_USER_ICON_KBN_PNG = "3";
	public static final String PATTERN_USER_ID_INPUT = "^[A-Za-z0-9]{4}";
	public static final int PATTERN_USER_ID_LENGTH_MIN_INPUT = 4;
	public static final int PATTERN_USER_ID_LENGTH_MAX_INPUT = 4;
	public static final int PATTERN_USER_NAME_LENGTH_MIN_INPUT = 1;
	public static final int PATTERN_USER_NAME_LENGTH_MAX_INPUT = 20;
	public static final String PATTERN_USER_NAME_KANA_INPUT = "^[ァ-ンー]+";
	public static final int PATTERN_USER_NAME_KANA_LENGTH_MIN_INPUT = 1;
	public static final int PATTERN_USER_NAME_KANA_LENGTH_MAX_INPUT = 40;
	public static final int PATTERN_USER_NOTE_LENGTH_MIN_INPUT = 0;
	public static final int PATTERN_USER_NOTE_LENGTH_MAX_INPUT = 400;
	public static final String PATTERN_USER_PASSWORD_INPUT = "^[A-Za-z0-9]{4,80}";
	public static final int PATTERN_USER_PASSWORD_LENGTH_MIN_INPUT = 4;
	public static final int PATTERN_USER_PASSWORD_LENGTH_MAX_INPUT = 80;
	public static final String PATTERN_USER_TEL_INPUT = "^0\\d{9,10}+$|^$";
	public static final int PATTERN_USER_TEL_LENGTH_MIN_INPUT = 0;
	public static final int PATTERN_USER_TEL_LENGTH_MAX_INPUT = 11;
}

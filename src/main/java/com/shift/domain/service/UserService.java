package com.shift.domain.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shift.common.CommonUtil;
import com.shift.common.Const;
import com.shift.common.ExcelLogic;
import com.shift.common.FileUploadLogic;
import com.shift.domain.model.bean.UserAddAddBean;
import com.shift.domain.model.bean.UserBean;
import com.shift.domain.model.bean.UserDownloadUserXlsxBean;
import com.shift.domain.model.bean.UserListBean;
import com.shift.domain.model.bean.UserModifyBean;
import com.shift.domain.model.bean.UserModifyModifyBean;
import com.shift.domain.model.dto.UserListDto;
import com.shift.domain.model.entity.UserEntity;
import com.shift.domain.repository.UserListRepository;
import com.shift.domain.repository.UserRepository;
import com.shift.form.UserAddForm;
import com.shift.form.UserModifyForm;

/**
 * @author saito
 *
 */
@Service
@Transactional
@PropertySource(value = "classpath:excel.properties")
public class UserService extends BaseService {

	@Autowired
	private UserListRepository userListRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${excel.user-templete-file-path}")
	private String templeteFilePath;

	@Value("${excel.user-out-file-path}")
	private String outFilePath;

	@Value("${excel.user-download-file-name}")
	private String downloadFileName;

	@Value("${excel.user-cell-sheet-name}")
	private String cellSheetName;

	@Value("${excel.user-cell-name-base}")
	private String cellNameBase;


	/**
	 * ユーザ詳細画面取得機能<br>
	 * [Service] (/user)
	 *
	 * @param userId RequestParameter 指定されたユーザID<br>
	 * ただし、指定がない場合(null)もある
	 * @param loginUser Authenticationから取得したログインユーザID
	 * @return UserBean
	 */
	public UserBean user(String userId, String loginUser) {

		UserEntity userEntity = new UserEntity();
		if (userId == null || userId.isEmpty()) {
			//userIdがnullまたは何もないときはloginUserからユーザを取得
			userEntity = selectUserEntityByUserId(loginUser);
		} else {
			//userIdからユーザを取得
			userEntity = selectUserEntityByUserId(userId);
		}
		boolean isUserModifyForm = false;
		if (loginUser.equals(userEntity.getId())) {
			//取得したユーザがログインユーザであるとき、isLoginUserModifyFormをtrue
			isUserModifyForm = true;
		}

		//Beanにセット
		UserBean userModifyBean = new UserBean(userEntity, isUserModifyForm);
		return userModifyBean;
	}


	/**
	 * ユーザ一覧画面<br>
	 * [Service] (/user/list)
	 *
	 * @param page RequestParameter 指定したページ<br>
	 * ただし、指定がない場合(null)もある
	 * @param keyword RequestParameter ユーザーを検索するためのキーワード<br>
	 * ただし、指定がない場合(null)もある
	 * @param loginUser Authenticationから取得したログインユーザID
	 * @return UserListBean
	 */
	public UserListBean userList(String page, String keyword, String loginUser) {

		//指定されたページから検索開始件目を取得
		int offset = calcOffsetByPage(page);
		//キーワードと検索開始件目から登録済みのユーザを取得
		List<UserListDto>  userList = selectUserByKeyword(keyword, offset);
		//検索結果から該当件数を取得
		int searchHitCount = calcSearchHitCountByUserList(userList);
		//指定されたページと該当件数からページネーションを取得
		List<Integer> paginationList = calcPaginationByPage(page, searchHitCount);
		//前後のページを取得
		int[] nextBeforePageArray = calcNextBeforePageByPage(page, searchHitCount);

		//Beanにセット
		UserListBean userListBean = new UserListBean();
		userListBean.setKeyword(keyword);
		userListBean.setUserList(userList);
		userListBean.setSearchHitCount(searchHitCount);
		userListBean.setPaginationList(paginationList);
		userListBean.setAfterPage(nextBeforePageArray[0]);
		userListBean.setBeforePage(nextBeforePageArray[1]);
		return userListBean;
	}


	/**
	 * ユーザ追加機能<br>
	 * [Service] (/user/add/add)
	 *
	 * @param userAddForm RequestParam ユーザを追加するForm
	 * @return UserAddAddBean
	 */
	public UserAddAddBean userAddAdd(UserAddForm userAddForm) {

		boolean isSuccessInsert = insertUserByUserAddForm(userAddForm);
		UserAddAddBean userAddAddBean = new UserAddAddBean(isSuccessInsert);
		return userAddAddBean;
	}


	/**
	 * ユーザ一覧Excelダウンロード機能<br>
	 * [Service] (/user/download/download/user.xlsx)
	 *
	 * @param response HttpServletResponse
	 * @return void
	 */
	public UserDownloadUserXlsxBean userDownloadUserXlsx() {

		List<UserListDto> userList = selectUserAll();
		writeExcelForUser(userList);

		//Beanにセット
		UserDownloadUserXlsxBean userDownloadUserXlsxBean = new UserDownloadUserXlsxBean(outFilePath, downloadFileName);
		return userDownloadUserXlsxBean;
	}


	/**
	 * ユーザ修正画面取得機能<br>
	 * [Service] (/user/modify)
	 *
	 * @param loginUser Authenticationから取得したログインユーザID
	 * @return UserListBean
	 */
	public UserModifyBean userModify(String loginUser) {

		//loginUserと一致するユーザーを取得
		UserEntity userEntity = selectUserEntityByUserId(loginUser);

		//Beanにセット
		UserModifyBean userModifyBean = new UserModifyBean(userEntity);
		return userModifyBean;
	}


	/**
	 * ユーザ修正機能<br>
	 * [Service] (/user/modify/modify)
	 *
	 * @param userModifyForm RequestParam ユーザを更新するForm
	 * @param loginUser Authenticationから取得したログインユーザID
	 * @return UserModifyModifyBean
	 */
	public UserModifyModifyBean userModifyModify(UserModifyForm userModifyForm, String loginUser) {

		//アップロードするファイル拡張子の区分を取得
		String uploadFileIconKbn = uploadFileByMultipartFile(userModifyForm.getUploadFile(), loginUser);
		boolean isSucessUpdate = false;
		if (uploadFileIconKbn != null) {
			//ファイルアップロードが成功している(uploadFileIconKbnがnullでない)とき、ユーザ情報をを更新し、結果を取得
			isSucessUpdate = updateUserByUser(userModifyForm, uploadFileIconKbn, loginUser);
		}
		//loginUserと一致するユーザーを取得
		UserEntity userEntity = selectUserEntityByUserId(loginUser);

		//Beanにセット
		UserModifyModifyBean userModifyModifyBean = new UserModifyModifyBean(isSucessUpdate, userEntity);
		return userModifyModifyBean;
	}


	/**
	 * 検索開始件目計算処理
	 *
	 * <p>指定したページに対応した検索開始件目(offset)を計算する<br>
	 * ただし、ページが指定されていないときはoffsetは0となる
	 * </p>
	 *
	 * @param page RequestParam
	 * @return int 指定したページから対応するn件目～
	 */
	private int calcOffsetByPage(String page) {

		//ページ数を指定されていないとき
		if (page == null || page.isEmpty()) {
			return 0;
		}

		//指定したページから表示すべき件目を計算し、返す
		int nowPage = Integer.parseInt(page);
		int offset = (nowPage - 1) * Const.USER_SELECT_LIMIT;
		return offset;
	}


	/**
	 * pagination計算処理
	 *
	 * <p>指定したページと検索件数からpaginationに表示するページを取得する<br>
	 * 指定したページが1かつ検索結果のページが10 -> 1 2 3 4 5<br>
	 * 指定したページが8かつ検索結果のページが10 -> 6 7 8 9 10<br>
	 * 指定したページが10かつ検索結果のページが10 -> 6 7 8 9 10<br>
	 * <br>
	 * のようにpaginationのページが動的になる<br>
	 * ただし、ページが指定されていないとき又は検索結果が0のときは必ず1ページとなる
	 * </p>
	 *
	 * @param page RequestParameter
	 * @param searchHitCount Serviceから取得した検索件数取
	 * @return List<Integer> 現在のページとSQLの検索結果からpaginationを計算したもの<br>
	 */
	private List<Integer> calcPaginationByPage(String page, int searchHitCount) {

		//デフォルトを1ページ目で指定
		int nowPage = 1;

		//ページ数を指定されているとき
		if (page != null) {
			nowPage = Integer.parseInt(page);
		}

		//-----------------------------
		//検索件数から最終ページを計算
		//-----------------------------

		//SQLの結果(COUNT) ÷ 1ページあたりの表示件数 = 最終ページ数(切り上げ)
		BigDecimal searchHitCountBd = new BigDecimal(String.valueOf(searchHitCount));
		BigDecimal paginationLimitBd = new BigDecimal(Const.USER_LIST_PAGINATION_LIMIT_PAGE_ODD);
		BigDecimal lastPageBd = searchHitCountBd.divide(paginationLimitBd, 0, RoundingMode.UP);

		//最終ページをintで取得する
		int lastPage = lastPageBd.intValue();


		//-----------------
		//表示ページの計算
		//-----------------

		//paginationを格納するための変数
		List<Integer> paginationList = new ArrayList<>();

		//lastPageがpaginationで表示する最大ページ数未満のとき
		if (lastPage <= Const.USER_LIST_PAGINATION_LIMIT_PAGE_ODD) {

			//lastPageの回数分ページを代入
			for (int i = 1; i <= lastPage; i++) {
				paginationList.add(i);
			}

			return paginationList;
		}

		//nowPage(現在のページ)が2ページ未満のとき
		if (nowPage <= 2) {

			//lastPageの回数分ページを代入
			for (int i = 1; i <= lastPage; i++) {

				//paginationで表示する最大ページ数を超えたとき
				if (Const.USER_LIST_PAGINATION_LIMIT_PAGE_ODD <= i) {
					paginationList.add(i);
					break;
				}

				paginationList.add(i);
			}

			return paginationList;
		}

		//paginationの中央値を計算
		BigDecimal num2Bd = new BigDecimal("2");
		BigDecimal medianPageBd = paginationLimitBd.divide(num2Bd, 0, RoundingMode.UP);
		int medianPage = medianPageBd.intValue();

		//中央値とpaginationの差分を計算(3 -> 1, 5 -> 2...)
		int defferenceMedianPagination = Const.USER_LIST_PAGINATION_LIMIT_PAGE_ODD - medianPage;

		//現在のページ + 中央値の差分がlastPageより小さいとき
		if (nowPage + defferenceMedianPagination <= lastPage) {

			//中央値の差分からpaginationの最初のページを取得
			int setPage = nowPage - defferenceMedianPagination;

			//paginationPageからスタートし、lastPage(最終ページ)までページを代入
			for (int i = 1; i <= Const.USER_LIST_PAGINATION_LIMIT_PAGE_ODD; i++) {

				//paginationで表示する最大ページ数を超えたとき
				if (Const.USER_LIST_PAGINATION_LIMIT_PAGE_ODD <= i) {
					paginationList.add(setPage);
					break;
				}

				paginationList.add(setPage);
				setPage++;
			}

			return paginationList;
		}

		//paginationの最初のページをpaginationLimitPageから逆算して取得する
		int setPage = lastPage - Const.USER_LIST_PAGINATION_LIMIT_PAGE_ODD + 1;

		//逆算したsetPageからpaginationLimitPageまで代入
		for (int i = 1; i <= Const.USER_LIST_PAGINATION_LIMIT_PAGE_ODD; i++) {
			paginationList.add(setPage);
			setPage++;
		}

		return paginationList;
	}


	/**
	 * 前後ページ計算処理
	 *
	 * <p>現在のページから前後のページを計算する<br>
	 * ただし、検索結果ページ
	 * </p>
	 *
	 * @param page RequestParameter
	 * @param searchHitCount Serviceから取得したDBの検索件数
	 * @return int[]
	 */
	private int[] calcNextBeforePageByPage(String page, int searchHitCount) {

		//nullまたは""または1ページ目のとき
		if (page == null || page.isEmpty() || "1".equals(page)) {

			//次のページを1, 前ページを1に設定し、返す
			int nextPage = 1;
			int beforePage = 1;
			int[] nextBeforePageArray = {nextPage, beforePage};
			return nextBeforePageArray;
		}

		//現在のページを取得
		int nowPage = Integer.parseInt(page);

		//現在のページがsearchHitCountを超えているとき
		if (searchHitCount <= nowPage) {

			//次のページをsearchHitCount, 前ページをnowPage - 1に設定し、返す
			int nextPage = searchHitCount;
			int beforePage = nowPage - 1;
			int[] nextBeforePageArray = {nextPage, beforePage};
			return nextBeforePageArray;
		}

		//次のページをnowPage + 1, 前ページをnowPage - 1に設定し、返す
		int nextPage = nowPage + 1;
		int beforePage = nowPage - 1;
		int[] nextBeforePageArray = {nextPage, beforePage};
		return nextBeforePageArray;
	}


	/**
	 * 検索件数取得処理
	 *
	 * <p>指定したページに対応した件目(offset)を計算する<br>
	 * ただし、ページが指定されていないときはoffsetは0となる
	 * </p>
	 *
	 * @param userList DBから取得したList<UserListDto> (&lt;UserListDto&gt;)
	 * @return int 指定したページから対応するn件目～
	 */
	private int calcSearchHitCountByUserList(List<UserListDto> userList) {

		//ページ数を指定されていないとき
		if (userList.isEmpty()) {
			return 0;
		}

		//userListから検索結果(count)をintで取得し、返す
		int searchHitCount = Integer.parseInt(userList.get(0).getCount());
		return searchHitCount;
	}


	/**
	 * ファイルアップロード処理
	 *
	 * <p>Formから取得したファイル(uploadFile)を指定のパスへアップロードする<br>
	 * また、アップロードの成功の可否及びアップロードしたファイル拡張子によって返す値が変わる<br>
	 * ただし、ファイルアップロードに失敗またアップロードが許容されていないファイル拡張子のときは必ず"0"となる
	 * </p>
	 *
	 * @param uploadFile アップロードされたファイル(MultipartFile)
	 * @param loginUser Authenticationから取得したユーザID
	 * @return String アップロード処理及びアップロードされたファイルの拡張子<br>
	 * 0: アップロードするファイルを選択肢していないとき
	 * 1: アップロードが成功かつアップロード後のファイル拡張子が".jpg"のとき<br>
	 * 2: アップロードが成功かつアップロード後のファイル拡張子が".jpeg"のとき<br>
	 * 3: アップロードが成功かつアップロード後のファイル拡張子が".png"のとき<br>
	 * null: アップロードに失敗またはアップロードが許容されていないファイル拡張子のとき
	 */
	private String uploadFileByMultipartFile(MultipartFile uploadFile, String loginUser) {

		//uploadFileがnullまたはファイルが存在しないとき、何もせず"0"を返す
		if (uploadFile == null || uploadFile.isEmpty()) {
			return "0";
		}

		//ファイルの種類を取得(image/*** になる)
		String fileType = uploadFile.getContentType();

		//fileTypeをファイルの拡張子名に変換する(.***)
		String fileExtension = fileType.replace("image/", ".");

		//ファイル名をユーザーID + ファイルの拡張子で設定
		String fileName = loginUser + fileExtension;

		//ファイルアップロードを実行し、結果を取得
		FileUploadLogic fileUploadLogic = new FileUploadLogic();
		boolean isSuccessFileUpload = fileUploadLogic.uploadFileByMultipartFile(uploadFile, Const.USER_ICON_RECORD_ROOT_DIR, fileName);

		//ファイルアップロード処理が失敗したとき、nullを返す
		if (!isSuccessFileUpload) {
			return null;
		}

		if (Const.USER_ICON_ALLOW_FILE_EXTENSION_ARRAY[0].equals(fileExtension)) {

			//ファイル拡張子が".jpg"のとき、アップロードしたファイルの拡張子が"jpg"であることを返す
			return Const.USER_ICON_FLG_JPG;
		} else if (Const.USER_ICON_ALLOW_FILE_EXTENSION_ARRAY[1].equals(fileExtension)) {

			//ファイル拡張子が".jpeg"のとき、アップロードしたファイルの拡張子が"jpeg"であることを返す
			return Const.USER_ICON_FLG_JPEG;
		} else if (Const.USER_ICON_ALLOW_FILE_EXTENSION_ARRAY[2].equals(fileExtension)) {

			//ファイル拡張子が".png"のとき、アップロードしたファイルの拡張子が"png"であることを返す
			return Const.USER_ICON_FLG_PNG;
		} else {

			//許容された拡張子でないとき、nullを返す
			return null;
		}
	}


	/**
	 * Excell書き込み処理
	 *
	 * <p>Excell(テンプレート)を取得し、ユーザ一覧を書き出す<br>
	 * ただし、userListがEmptyであるまたはExcell及び指定したセルに書き込めないときはエラーとなる
	 * </p>
	 *
	 * @param userList DBから取得したList (List&lt;UserListDto&gt;)
	 * @return void
	 */
	private void writeExcelForUser(List<UserListDto> userList) {

		try (FileInputStream fileInputStream = new FileInputStream(templeteFilePath);
				Workbook workBook = WorkbookFactory.create(fileInputStream);
				OutputStream fileOutputStream =  new FileOutputStream(outFilePath);) {

			//ワークブックからシートを取得
			Sheet sheet1 = workBook.getSheet(cellSheetName);

			//Apach POIを扱うLogicクラス
			ExcelLogic excelLogic = new ExcelLogic();

			//値を挿入したいセルの列を指定
			int distanceBaseCellRow = 1;

			for (UserListDto userListDto: userList) {

				//列情報を取得
				Row cellRow = excelLogic.getRow(workBook, sheet1, cellNameBase, distanceBaseCellRow);

				//セルへ書き込み
				excelLogic.writeCellValueForCell(cellRow, 1, userListDto.getId());
				excelLogic.writeCellValueForCell(cellRow, 2, userListDto.getName());
				excelLogic.writeCellValueForCell(cellRow, 3, userListDto.getNameKana());
				excelLogic.writeCellValueForCell(cellRow, 4, userListDto.genderFormatMF());

				//書き込む対象の列を1列下げる
				distanceBaseCellRow++;
			}

			//書き込んだセルをExcelへ書き出し
			excelLogic.writeAllCellForExcel(workBook, fileOutputStream);
		} catch (Exception e) {

			//例外発生時、ログを出力
			e.printStackTrace();
		}
	}


	/**
	 * [DB]ユーザ一覧検索処理
	 *
	 * <p>offsetとkeywordからユーザ一覧を取得する<br>
	 * また、取得されるユーザ数は5件までとなる<br>
	 * ただし、keywordがない場合は全件を取得
	 * </p>
	 *
	 * @param keyword RequestParameter
	 * @param offset Serviceから取得した検索開始件目
	 * @return List<UserListDto><br>
	 * フィールド(List&lt;UserListDto&gt;)<br>
	 * id, name, nameKana, gender, count
	 */
	private List<UserListDto> selectUserByKeyword(String keyword, int offset) {

		//keyWordをLIKEで一致するように検索する
		String trimKeyword = Const.CHARACTER_PERCENT + CommonUtil.changeEmptyByNull(keyword) + Const.CHARACTER_PERCENT;
		List<UserListDto> userList = userListRepository.selectUserByKeyWordLimitOffset(trimKeyword, Const.USER_SELECT_LIMIT, offset);
		return userList;
	}


	/**
	 * [DB]ユーザ一覧検索処理
	 *
	 * <p>登録済みユーザを全て取得する</p>
	 *
	 * @param void
	 * @return List<UserListDto><br>
	 * フィールド(List&lt;UserListDto&gt;)<br>
	 * id, name, nameKana, gender, count
	 */
	private List<UserListDto> selectUserAll() {

		List<UserListDto> userList = userListRepository.selectUserALL();
		return userList;
	}


	/**
	 * [DB]ユーザ(Entity)検索処理
	 *
	 * <p>userIdからユーザを取得する</p>
	 *
	 * @param userId Request Param
	 * @return UserEntity<br>
	 * フィールド(UserEntity)<br>
	 * id, name, nameKana, gender, password, address, tel, email, note, admin_flg, del_flg
	 */
	private UserEntity selectUserEntityByUserId(String userId) {

		Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
		UserEntity userEntity = new UserEntity();

		//SQLの検索結果があるとき
		if (userEntityOptional.isPresent()) {
			userEntity = userEntityOptional.get();
		}

		return userEntity;
	}


	/**
	 * [DB]ユーザ更新処理
	 *
	 * <p>ユーザを更新する<br>
	 * 更新する内容は"id, name, name_kana, gender, note, iconKbn" となる<br>
	 * ただし、ユーザ更新に失敗したらfalseが返される<br>
	 * また、uploadFileIconKbnの値が許容されているファイル拡張子区分と一致しない場合でも更新失敗にはならない
	 * </p>
	 *
	 * @param userModifyForm RequestParameter
	 * @param uploadFileIconKbn アップロードしたファイル拡張子の区分<br>
	 * ただし、許容されているファイル拡張子区分と一致しない場合は更新されない
	 * @param loginUser Authenticationから取得したユーザID
	 * @return boolean<br>
	 * true: ユーザ情報を更新したとき<br>
	 * false: ユーザ情報を更新できなかったとき
	 */
	private boolean updateUserByUser(UserModifyForm userModifyForm, String uploadFileIconKbn,String loginUser) {

		//loginUserからユーザを取得
		Optional<UserEntity> userEntityOpt = userRepository.findById(loginUser);

		//ユーザを取得できなかったとき、何もせずfalseを返す
		if (!userEntityOpt.isPresent()) {
			return false;
		}

		//userEntityに値をセットし、更新後trueを返す
		UserEntity userEntity = userEntityOpt.get();
		userEntity.setName(userModifyForm.getName());
		userEntity.setNameKana(userModifyForm.getNameKana());
		userEntity.setGender(userModifyForm.getGender());
		userEntity.setNote(userModifyForm.getNote());
		if (CommonUtil.isSuccessValidation(uploadFileIconKbn, Const.PATTERN_USER_ICON_KBN_ALL)) {
			//uploadFileIconKbnが許容されているファイル拡張子区分と一致する場合、値をセットする
			userEntity.setIconKbn(uploadFileIconKbn);
		}
		userRepository.save(userEntity);
		return true;
	}


	/**
	 * [DB]ユーザ新規追加処理
	 *
	 * <p>ユーザを新規追加する<br>
	 * ただし、新規追加する内容は"id, name, name_kana, gender, password, address, tel, email, note, adminFlg" となる<br>
	 * また、passwordはハッシュ化される(b-crypt)
	 * </p>
	 *
	 * @param userAddForm RequestParameter
	 * @return boolean<br>
	 * true: ユーザ情報を追加したとき<br>
	 * false: ユーザ情報を追加できなかったとき
	 */
	private boolean insertUserByUserAddForm(UserAddForm userAddForm) {

		//idとメールアドレスがどちらか一方でも一致するユーザを検索
		List<UserEntity> checkUserEntityList = userRepository.findByIdOrEmail(userAddForm.getUserId(), userAddForm.getEmail());

		//idとメールアドレスがどちらか一方でも一致するユーザが存在したとき、何もせずfalseを返す
		if (!checkUserEntityList.isEmpty()) {
			return false;
		}

		//パスワードをハッシュ化
		String encodingPassword = passwordEncoder.encode(userAddForm.getPassword());

		//userEntityに値をセットし、追加後trueを返す
		UserEntity userEntity = new UserEntity();
		userEntity.setId(userAddForm.getUserId());
		userEntity.setName(userAddForm.getName());
		userEntity.setNameKana(userAddForm.getNameKana());
		userEntity.setGender(userAddForm.getGender());
		userEntity.setPassword(encodingPassword);
		userEntity.setAddress(userAddForm.getAddress());
		userEntity.setTel(userAddForm.getTel());
		userEntity.setEmail(userAddForm.getEmail());
		userEntity.setNote(userAddForm.getNote());
		userEntity.setAdminFlg(userAddForm.getAdminFlg());
		userRepository.save(userEntity);
		return true;
	}
}

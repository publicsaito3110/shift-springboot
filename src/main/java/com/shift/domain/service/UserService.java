package com.shift.domain.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shift.common.CommonUtil;
import com.shift.common.Const;
import com.shift.domain.model.bean.AccountBean;
import com.shift.domain.model.bean.UserBean;
import com.shift.domain.model.bean.UserModifyBean;
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
@PropertySource(value = "classpath:excel.properties")
public class UserService extends BaseService {

	@Autowired
	private HttpSession httpSession;

	@Autowired
	private UserListRepository userListRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	//フィールド
	private String userId;

	private boolean isAdminUser;

	private int offset;

	private List<UserListDto> userList;

	private int searchHitCount;

	private int lastPage;

	private int beforePage;

	private int afterPage;

	@Value("${excel.templete-file-pass}")
	private String templeteFilePass;

	@Value("${excel.out-file-pass}")
	private String outFilePass;

	@Value("${excel.download-file-name}")
	private String downloadFileName;



	/**
	 * [Service] (/user)
	 *
	 * @param page Request Param
	 * @param keyword Request Param
	 * @return UserBean
	 */
	public UserBean user(String page, String keyword) {

		this.chackUserIdAdminUser();
		this.calcOffsetByPage(page);

		boolean isPaginationIndex = false;
		//管理者であるとき
		if (this.isAdminUser) {
			this.selectUserByKeyword(keyword);
			isPaginationIndex = true;
		}
		//管理者でないとき
		if (!this.isAdminUser) {
			this.selectUserByUserId();
		}

		List<Integer> paginationList = this.calcPaginationByPage(page);
		this.calcAfterBeforePageByPage(page);

		//Beanにセット
		UserBean userBean = new UserBean();
		userBean.setKeyword(keyword);
		userBean.setUserList(this.userList);
		userBean.setSearchHitCount(this.searchHitCount);
		userBean.setPaginationList(paginationList);
		userBean.setPaginationIndex(isPaginationIndex);
		userBean.setBeforePage(this.beforePage);
		userBean.setAfterPage(this.afterPage);
		return userBean;
	}


	/**
	 * [Service] (/user/add/add)
	 *
	 * @param void
	 * @return void
	 */
	public void userAddAdd(UserAddForm userAddForm) {

		this.insertUserByUserAddForm(userAddForm);
	}


	/**
	 * [Service] (/user/download/download/user-template.xlsx)
	 *
	 * @param void
	 * @return void
	 */
	public void userDownloadUserTemplateXlsx(HttpServletResponse response) {

		this.outputUserForExcel(response);
	}


	/**
	 * [Service] (/user/modify)
	 *
	 * @param void
	 * @return UserBean
	 */
	public UserModifyBean userModify(String userId) {

		UserEntity userEntity = this.selectUserByUserId(userId);

		//Beanにセット
		UserModifyBean userModifyBean = new UserModifyBean(userEntity);
		return userModifyBean;
	}


	/**
	 * [Service] (/user/modify/modify)
	 *
	 * @param void
	 * @return void
	 */
	public void userModifyModify(UserModifyForm userModifyForm) {

		this.updateUserByUserModifyForm(userModifyForm);
	}



	/**
	 * 管理者判定処理
	 *
	 * <p>Sessionから管理者かどうかを取得する<br>
	 * 管理者かどうかの判定をisAdminUser(フィールド)にセットする
	 * </p>
	 *
	 * @param void
	 * @return void
	 */
	private void chackUserIdAdminUser() {

		AccountBean accountBean = (AccountBean)httpSession.getAttribute(Const.SESSION_KEYWORD_ACCOUNT_BEAN);
		this.userId = accountBean.getUserId();
		this.isAdminUser = accountBean.isAdminUser();
	}


	/**
	 * 件目計算処理
	 *
	 * <p>指定したページに対応した件目(offset)を計算する<br>
	 * ただし、ページが指定されていないときはoffsetは0となる
	 * </p>
	 *
	 * @param page Request Param
	 * @return void
	 */
	private void calcOffsetByPage(String page) {

		//ページ数を指定されていないとき
		if (page == null || page.isEmpty()) {

			this.offset = 0;
			return;
		}

		//現在のページから表示すべき件目を計算する
		int nowPage = Integer.parseInt(page);
		int offset = (nowPage - 1) * Const.USER_SELECT_LIMIT;
		this.offset = offset;
	}


	/**
	 * Excell書き込み処理
	 *
	 * <p>Excell(テンプレート)を取得し、ユーザ一覧を書き出す<br>
	 * ただし、Excell及び指定したセルに書き込めないときはエラーとなる
	 * </p>
	 *
	 * @param response HttpServletResponse<br>
	 * ファイルダウンロード処理のみ使用
	 * @return void
	 */
	private void outputUserForExcel(HttpServletResponse response) {

		//Excel(テンプレート)のファイルパス
		String templeteFilePass = this.templeteFilePass;

		//出力するファイルパス
		String outFilePass = this.outFilePass;

		try (FileInputStream fileInputStream = new FileInputStream(templeteFilePass);
				Workbook workBook = WorkbookFactory.create(fileInputStream);
				OutputStream outputStream =  new FileOutputStream(outFilePass);
				OutputStream responseOutputStream =  response.getOutputStream();) {

			//-----------------
			// EXCELへ書き込み
			//-----------------

			//ワークブックからシート名を指定して取得
			Sheet sheet1 = workBook.getSheet("sheet1");

			//名前付きのセルを取得
			XSSFCell cellXSSFC = this.getCellXSSFCByWorkbookSheetBaseCellNameDistanceBaseCellRow(workBook, sheet1, "user_id", 1);
			cellXSSFC.setCellValue("あいう");

			//書き込んだセルをExcelへ書き出し
			workBook.write(outputStream);

			//------------------
			//ダウンロード処理
			//------------------
			Path filePath = Paths.get(outFilePass);
			byte[] fileByte = Files.readAllBytes(filePath);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(downloadFileName, "UTF-8") + "\"");
			response.setContentLength(fileByte.length);
			responseOutputStream.write(fileByte);
			responseOutputStream.flush();
		} catch (Exception e) {

			//例外発生時、ログを出力
			e.printStackTrace();
		}
	}


	/**
	 * pagination計算処理
	 *
	 * <p>セルの名前から対象のセルを取得する<br>
	 * ただし、対象のセルが存在しないときはnullとなる<br>
	 * </p>
	 *
	 * @param page Request Param
	 * @return List<Integer> 現在のページとSQLの検索結果からpaginationを計算したもの<br>
	 */
	private XSSFCell getCellXSSFCByWorkbookSheetBaseCellNameDistanceBaseCellRow(Workbook workBook, Sheet sheet, String baseCellName, int distanceBaseCellRow) {

		XSSFCell cellXSSFC = null;

		try {

			//名前付きのセルを取得
			XSSFName cellNameXSSFN = (XSSFName)workBook.getName(baseCellName);
			CellReference cellReference = new CellReference(cellNameXSSFN.getRefersToFormula());

			//名前付きのセルからdistanceBaseCellRowだけ下のセルを指定し、取得する
			XSSFRow cellRowXSSFR = (XSSFRow)sheet.createRow(cellReference.getRow() + distanceBaseCellRow);
			cellXSSFC = cellRowXSSFR.createCell(cellReference.getCol());
		}catch (Exception e) {

			//例外発生時、nullを返す
			return null;
		}

		return cellXSSFC;
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
	 * ただし、ページが指定されていないときは1ページ目となる<br>
	 * 検索結果が0のときはnullが返される<br>
	 * </p>
	 *
	 * @param page Request Param
	 * @return List<Integer> 現在のページとSQLの検索結果からpaginationを計算したもの<br>
	 */
	private List<Integer> calcPaginationByPage(String page) {

		int nowPage = 1;

		//ページ数を指定されているとき
		if (page != null) {
			nowPage = Integer.parseInt(page);
		}

		//-----------------------------
		//検索件数から最終ページを計算
		//-----------------------------

		//paginationで表示するページの数
		int paginationLimitPage = Const.USER_LIST_PAGINATION_LIMIT_PAGE_ODD;

		//SQLの検索件数
		int searchHitCount = 0;

		//検索結果があるときは検索件数を取得
		if (!this.userList.isEmpty()) {
			searchHitCount = Integer.parseInt(this.userList.get(0).getCount());
		}

		//SQLの結果(COUNT) ÷ 1ページあたりの表示件数 = 最終ページ数(切り上げ) とし、フィールドにセット
		BigDecimal searchHitCountBd = new BigDecimal(searchHitCount);
		BigDecimal paginationLimitBd = new BigDecimal(paginationLimitPage);
		BigDecimal lastPageBd = searchHitCountBd.divide(paginationLimitBd, 0, RoundingMode.UP);
		this.searchHitCount = searchHitCount;

		//最終ページを取得し、フィールドにセット
		int lastPage = lastPageBd.intValue();
		this.lastPage = lastPage;


		//-----------------
		//表示ページの計算
		//-----------------

		//paginationを格納するための変数
		List<Integer> paginationList = new ArrayList<>();

		//lastPageがpaginationLimitPage未満のとき
		if (lastPage <= paginationLimitPage) {

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

				//paginationLimitPageの回数を超えたとき
				if (paginationLimitPage <= i) {
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
		int defferenceMedianPagination = paginationLimitPage - medianPage;

		//現在のページ + 中央値の差分がlastPageより小さいとき
		if (nowPage + defferenceMedianPagination <= lastPage) {

			//中央値の差分からpaginationの最初のページを取得
			int setPage = nowPage - defferenceMedianPagination;

			//paginationPageからスタートし、lastPage(最終ページ)までページを代入
			for (int i = 1; i <= paginationLimitPage; i++) {

				//paginationLimitPageの回数を超えたとき
				if (paginationLimitPage <= i) {
					paginationList.add(setPage);
					break;
				}

				paginationList.add(setPage);
				setPage++;
			}

			return paginationList;
		}

		//paginationの最初のページをpaginationLimitPageから逆算して取得する
		int setPage = lastPage - paginationLimitPage + 1;

		//逆算したsetPageからpaginationLimitPageまで代入
		for (int i = 1; i <= paginationLimitPage; i++) {
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
	 * @param page Request Param
	 * @return void
	 */
	private void calcAfterBeforePageByPage(String page) {

		//nullまたは""または1ページ目のとき
		if (page == null || page.isEmpty() || "1".equals(page)) {
			this.beforePage = 1;
			this.afterPage = 1;
			return;
		}

		//現在のページと検索結果ページを取得
		int nowPage = Integer.parseInt(page);
		int lastPage = this.lastPage;

		//現在のページがindexLastPageを超えているとき
		if (lastPage <= nowPage) {
			this.beforePage = Integer.parseInt(page) - 1;
			this.afterPage = lastPage;
			return;
		}

		this.beforePage = Integer.parseInt(page) - 1;
		this.afterPage = Integer.parseInt(page) + 1;
	}


	/**
	 * [DB]ユーザ一覧検索処理
	 *
	 * <p>ユーザ一覧を取得する<br>
	 * ただし、keywordがない場合は全件を取得<br>
	 * 管理者ユーザのみの処理
	 * </p>
	 *
	 * @param keyword Request Param
	 * @return void
	 */
	private void selectUserByKeyword(String keyword) {

		keyword = CommonUtil.changeEmptyByNull(keyword);

		//keyWordをLIKEで一致するように検索する
		keyword = "%" + keyword + "%";
		List<UserListDto> userList = userListRepository.selectUserByKeyWordLimitOffset(keyword, Const.USER_SELECT_LIMIT, this.offset);
		this.userList = userList;
	}


	/**
	 * [DB]ユーザ検索処理
	 *
	 * <p>ログインユーザを取得する<br>
	 * 一般ユーザのみの処理
	 * </p>
	 *
	 * @param void
	 * @return void
	 */
	private void selectUserByUserId() {

		List<UserListDto> userList = userListRepository.selectUserByUserId(this.userId);
		this.userList = userList;
	}


	/**
	 * [DB]ユーザ一覧検索処理
	 *
	 * <p>ユーザ一覧を取得する</p>
	 *
	 * @param keyword Request Param
	 * @return void
	 */
	private void selectUserAll() {

		List<UserListDto> userList = userListRepository.findAll();
		this.userList = userList;
	}


	/**
	 * [DB]ユーザ検索処理
	 *
	 * <p>userIdからユーザを取得する</p>
	 *
	 * @param userId Request Param
	 * @return UserEntity
	 */
	private UserEntity selectUserByUserId(String userId) {

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
	 * ただし、更新する内容は"id, name, name_kana, gender, note" となる
	 * </p>
	 *
	 * @param userModifyForm Request Param
	 * @return void
	 */
	private void updateUserByUserModifyForm(UserModifyForm userModifyForm) {

		UserEntity userEntity = this.selectUserByUserId(userModifyForm.getUserId());
		userEntity.setName(userModifyForm.getName());
		userEntity.setNameKana(userModifyForm.getNameKana());
		userEntity.setGender(userModifyForm.getGender());
		userEntity.setNote(userModifyForm.getNote());

		this.userRepository.save(userEntity);
	}


	/**
	 * [DB]ユーザ新規追加処理
	 *
	 * <p>ユーザを新規追加する<br>
	 * ただし、新規追加する内容は"id, name, name_kana, gender, password, address, tel, email, note, adminFlg" となる<br>
	 * また、passwordはハッシュ化される(b-crypt)
	 * </p>
	 *
	 * @param userAddForm Request Param
	 * @return void
	 */
	private void insertUserByUserAddForm(UserAddForm userAddForm) {

		//パスワードをハッシュ化
		String encodingPassword = this.passwordEncoder.encode(userAddForm.getPassword());

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

		this.userRepository.save(userEntity);
	}
}

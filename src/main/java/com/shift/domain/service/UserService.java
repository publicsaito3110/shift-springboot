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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shift.common.CommonUtil;
import com.shift.common.Const;
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
	private UserListRepository userListRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${excel.user-templete-file-pass}")
	private String templeteFilePass;

	@Value("${excel.user-out-file-pass}")
	private String outFilePass;

	@Value("${excel.user-download-file-name}")
	private String downloadFileName;

	@Value("${excel.user-cell-sheet-name}")
	private String cellSheetName;

	@Value("${excel.user-cell-name-base}")
	private String cellNameBase;


	/**
	 * [Service] (/user)
	 *
	 * @param page RequestParameter
	 * @param keyword RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @param userRoleArray Authenticationから取得したユーザROLE
	 * @return UserBean
	 */
	public UserBean user(String page, String keyword, String loginUser, String[] userRoleArray) {

		int offset = calcOffsetByPage(page);
		List<UserListDto> userList = new ArrayList<>();
		boolean isPaginationIndex = false;
		if (Arrays.asList(userRoleArray).contains(Const.ROLE_USER_ADMIN)) {
			//管理者であるとき
			userList = selectUserByKeyword(keyword, offset);
			isPaginationIndex = true;
		} else {
			//管理者でないとき
			userList = selectUserByUserId(loginUser);
		}
		int searchHitCount = calcSearchHitCountByUserList(userList);
		List<Integer> paginationList = calcPaginationByPage(page, searchHitCount);
		int[] nextBeforePageArray = calcNextBeforePageByPage(page, searchHitCount);

		//Beanにセット
		UserBean userBean = new UserBean();
		userBean.setKeyword(keyword);
		userBean.setUserList(userList);
		userBean.setSearchHitCount(searchHitCount);
		userBean.setPaginationList(paginationList);
		userBean.setPaginationIndex(isPaginationIndex);
		userBean.setAfterPage(nextBeforePageArray[0]);
		userBean.setBeforePage(nextBeforePageArray[1]);
		return userBean;
	}


	/**
	 * [Service] (/user/add/add)
	 *
	 * @param void
	 * @return void
	 */
	public void userAddAdd(UserAddForm userAddForm) {

		insertUserByUserAddForm(userAddForm);
	}


	/**
	 * [Service] (/user/download/download/user.xlsx)
	 *
	 * @param response HttpServletResponse
	 * @return void
	 */
	public void userDownloadUserXlsx(HttpServletResponse response) {

		List<UserListDto> userList = selectUserAll();
		outputUserForExcel(userList, response);
	}


	/**
	 * [Service] (/user/modify)
	 *
	 * @param void
	 * @return UserBean
	 */
	public UserModifyBean userModify(String userId) {

		UserEntity userEntity = selectUserEntityByUserId(userId);

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

		updateUserByUserModifyForm(userModifyForm);
	}


	/**
	 * 検索開始件目計算処理
	 *
	 * <p>指定したページに対応した検索開始件目(offset)を計算する<br>
	 * ただし、ページが指定されていないときはoffsetは0となる
	 * </p>
	 *
	 * @param page Request Param
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

		//SQLの結果(COUNT) ÷ 1ページあたりの表示件数 = 最終ページ数(切り上げ)
		BigDecimal searchHitCountBd = new BigDecimal(String.valueOf(searchHitCount));
		BigDecimal paginationLimitBd = new BigDecimal(paginationLimitPage);
		BigDecimal lastPageBd = searchHitCountBd.divide(paginationLimitBd, 0, RoundingMode.UP);

		//最終ページをintで取得する
		int lastPage = lastPageBd.intValue();


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
	private void outputUserForExcel(List<UserListDto> userList, HttpServletResponse response) {

		try (FileInputStream fileInputStream = new FileInputStream(templeteFilePass);
				Workbook workBook = WorkbookFactory.create(fileInputStream);
				OutputStream outputStream =  new FileOutputStream(outFilePass);
				OutputStream responseOutputStream =  response.getOutputStream();) {

			//-----------------
			// EXCELへ書き込み
			//-----------------

			//対象のシート名と基準となるセル名を取得
			String cellSheetName = this.cellSheetName;
			String cellNameBase = this.cellNameBase;

			//ワークブックからシートを取得
			Sheet sheet1 = workBook.getSheet(cellSheetName);

			//列情報を格納するための変数
			Row cellRow = null;

			//値を挿入したいセルの列を指定
			int distanceBaseCellRow = 1;

			for (UserListDto userListDto: userList) {

				//列情報を取得
				cellRow = this.getRowByWorkbookSheetBaseCellNameDistanceBaseCellRow(workBook, sheet1, cellNameBase, distanceBaseCellRow);

				//セルへ書き込み
				cellRow.createCell(1).setCellValue(userListDto.getId());
				cellRow.createCell(2).setCellValue(userListDto.getName());
				cellRow.createCell(3).setCellValue(userListDto.getNameKana());
				cellRow.createCell(4).setCellValue(userListDto.genderFormatMF());

				//書き込む対象の列を1列下げる
				distanceBaseCellRow++;
			}

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
	 * 列取得処理 {Excel POI}
	 *
	 * <p>セルの名前から対象のセルを取得し、distanceBaseCellRowだけ下の列情報を取得する<br>
	 * ただし、列情報を取得できないときはnullとなる<br>
	 * </p>
	 *
	 * @param workBook Excel読み込み済みのWorkBook
	 * @param sheet Excelのシート名取得済みのSheet
	 * @param baseCellName シートに設定したセル名
	 * @param distanceBaseCellRow 指定したセルから取得したい列の行数
	 * @return Row Excelの列情報が代入されたRow
	 */
	private Row getRowByWorkbookSheetBaseCellNameDistanceBaseCellRow(Workbook workBook, Sheet sheet, String baseCellName, int distanceBaseCellRow) {

		Row cellRow = null;
		try {

			//名前付きのセルを取得
			Name cellName = workBook.getName(baseCellName);
			CellReference cellReference = new CellReference(cellName.getRefersToFormula());

			//名前付きのセルからdistanceBaseCellRowだけ下のセルを指定し、取得する
			cellRow = sheet.createRow(cellReference.getRow() + distanceBaseCellRow);
		}catch (Exception e) {

			//例外発生時、nullを返す
			return null;
		}

		return cellRow;
	}


	/**
	 * [DB]ユーザ一覧検索処理
	 *
	 * <p>ユーザ一覧を取得する<br>
	 * ただし、keywordがない場合は全件を取得<br>
	 * 管理者ユーザのみの処理
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
		String trimKeyword = "%" + CommonUtil.changeEmptyByNull(keyword) + "%";
		List<UserListDto> userList = userListRepository.selectUserByKeyWordLimitOffset(trimKeyword, Const.USER_SELECT_LIMIT, offset);
		return userList;
	}


	/**
	 * [DB]ユーザ検索処理
	 *
	 * <p>ログインユーザを取得する<br>
	 * 一般ユーザのみの処理
	 * </p>
	 *
	 * @param loginUser Authenticationから取得したユーザID
	 * @return List<UserListDto><br>
	 * フィールド(List&lt;UserListDto&gt;)<br>
	 * id, name, nameKana, gender, count
	 */
	private List<UserListDto> selectUserByUserId(String loginUser) {

		List<UserListDto> userList = userListRepository.selectUserByUserId(loginUser);
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
	 * ただし、更新する内容は"id, name, name_kana, gender, note" となる
	 * </p>
	 *
	 * @param userModifyForm RequestParameter
	 * @return void
	 */
	private void updateUserByUserModifyForm(UserModifyForm userModifyForm) {

		//userEntityに値をセットし、更新
		UserEntity userEntity = selectUserEntityByUserId(userModifyForm.getUserId());
		userEntity.setName(userModifyForm.getName());
		userEntity.setNameKana(userModifyForm.getNameKana());
		userEntity.setGender(userModifyForm.getGender());
		userEntity.setNote(userModifyForm.getNote());
		userRepository.save(userEntity);
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
	 * @return void
	 */
	private void insertUserByUserAddForm(UserAddForm userAddForm) {

		//パスワードをハッシュ化
		String encodingPassword = passwordEncoder.encode(userAddForm.getPassword());

		//userEntityに値をセットし、追加
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
	}
}

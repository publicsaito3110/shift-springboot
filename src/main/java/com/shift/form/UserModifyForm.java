package com.shift.form;

import java.util.Arrays;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.web.multipart.MultipartFile;

import com.shift.common.Const;
import com.shift.domain.model.entity.UserEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saito
 *
 */
@Data
@NoArgsConstructor
public class UserModifyForm {

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_NAME_INPUT, message = "20文字以内で入力してください")
	private String name;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_NAME_KANA_INPUT, message = "全角カナ40文字以内で入力してください")
	private String nameKana;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_GENDER_INPUT, message = "入力値が不正です")
	private String gender;

	@Pattern(regexp = Const.PATTERN_USER_NOTE_INPUT, message = "400文字以内で入力してください")
	private String note;

	private MultipartFile uploadFile;


	//コンストラクタ
	public UserModifyForm(UserEntity userEntity) {
		name = userEntity.getName();
		nameKana = userEntity.getNameKana();
		gender = userEntity.getGender();
		note = userEntity.getNote();
	}


	/**
	 * バリデーションエラー判定(単項目)<br>
	 *
	 * <p>uploadFile(MultipartFile)のバリデーションエラーチェック<br>
	 * 許容可能なファイルであるまたはファイルがアップロードされていないときはバリデーション成功(false)となる<br>
	 * ただし、許容可能なファイルでないときはバリデーション失敗(true)となる
	 * </p>
	 * @param uploadFile RequestParameter
	 * @return boolean<br>
	 * true: 許容可能なファイルでない(バリデーションエラー)とき
	 * false: 許容可能なファイルである(バリデーション成功)とき
	 */
	public boolean isErrorValidUploadFile() {

		//uploadFileがnullまたはファイルが存在しないとき、falseを返す
		if (uploadFile == null || uploadFile.isEmpty()) {
			return false;
		}

		//ファイルの種類を取得(+++/*** になる)
		String fileType = uploadFile.getContentType();

		//fileTypeをファイルの拡張子名に変換する(.***)
				String fileExtension = fileType.replace("image/", ".");

		//登録可能な拡張子であるとき、falseを返す
		if (Arrays.asList(Const.USER_ICON_ALLOW_FILE_EXTENSION_ARRAY).contains(fileExtension)) {
			return false;
		}

		//許容されていないファイルのとき、trueを返す
		return true;
	}
}

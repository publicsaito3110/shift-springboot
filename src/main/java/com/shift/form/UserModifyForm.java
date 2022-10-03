package com.shift.form;

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
}

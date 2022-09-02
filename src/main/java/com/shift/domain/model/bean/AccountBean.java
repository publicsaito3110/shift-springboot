package com.shift.domain.model.bean;

import com.shift.common.CommonUtil;
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
public class AccountBean {

	//フィールド
	private String userId;

	private String name;

	private String nameKana;

	private boolean isAdminUser;


	//コンストラクタ
	public AccountBean(UserEntity userEntity) {
		userId = userEntity.getId();
		name = userEntity.getName();
		nameKana = userEntity.getNameKana();

		//管理者かどうかの判定
		boolean isAdminUser = false;
		if (CommonUtil.isSuccessValidation(userEntity.getAdminFlg(), Const.PATTERN_USER_ADMIN_FLG)) {
			isAdminUser = true;
		}

		this.isAdminUser = isAdminUser;
	}
}

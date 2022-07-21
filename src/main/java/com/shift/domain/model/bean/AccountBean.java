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
		this.userId = userEntity.getId();
		this.name = userEntity.getName();
		this.nameKana = userEntity.getNameKana();

		//管理者かどうかの判定
		boolean isAdminUser = false;
		String adminFlg = CommonUtil.changeEmptyByNull(userEntity.getAdminFlg());
		if (Const.PATTERN_USER_ADMIN_FLG.matches(adminFlg)) {
			isAdminUser = true;
		}
		this.isAdminUser = isAdminUser;
	}
}

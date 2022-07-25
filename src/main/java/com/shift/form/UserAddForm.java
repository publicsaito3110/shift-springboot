package com.shift.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saito
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddForm {

	private String userId;
	private String name;
	private String nameKana;
	private String gender;
	private String password;
	private String address;
	private String tel;
	private String email;
	private String note;
	private String adminFlg;
}

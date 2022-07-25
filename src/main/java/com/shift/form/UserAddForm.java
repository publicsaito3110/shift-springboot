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
	private String note;
}

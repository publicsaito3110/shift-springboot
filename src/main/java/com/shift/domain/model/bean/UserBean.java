package com.shift.domain.model.bean;

import java.util.List;

import com.shift.common.CommonUtil;
import com.shift.domain.model.dto.UserListDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saito
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBean {

	private String keyword;

	private String page;

	private List<UserListDto> userList;

	private int indexCount;

	private List<Integer> paginationList;

	private boolean isPaginationIndex;

	private int beforePage;

	private int afterPage;


	//メソッド
	public String getKeywordFormatNotNull() {
		return CommonUtil.changeEmptyByNull(this.keyword);
	}
}

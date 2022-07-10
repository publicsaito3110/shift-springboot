package com.shift.domain.model.bean;

import java.util.List;

import com.shift.domain.model.dto.DmMenuDto;

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
public class DmBean {

	private List<DmMenuDto> dmHistoryList;
}

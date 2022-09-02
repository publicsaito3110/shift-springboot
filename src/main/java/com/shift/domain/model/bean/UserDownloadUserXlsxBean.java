package com.shift.domain.model.bean;

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
public class UserDownloadUserXlsxBean {

	private String outFilePath;

	private String downloadFileName;
}

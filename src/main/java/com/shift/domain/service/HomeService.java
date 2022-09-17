package com.shift.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.domain.model.bean.CmnNewsBean;
import com.shift.domain.model.bean.HomeBean;
import com.shift.domain.service.common.CmnNewsService;

/**
 * @author saito
 *
 */
@Service
public class HomeService extends BaseService {

	@Autowired
	private CmnNewsService cmnNewsService;


	/**
	 * [Service] (/home)
	 *
	 * @param void
	 * @return HomeBean
	 */
	public HomeBean home() {

		//CmnNewsService(共通サービス)から処理結果を取得
		CmnNewsBean cmnNewsBean = cmnNewsService.generateDisplayNowNews();

		//Beanにセット
		HomeBean homeBean = new HomeBean(cmnNewsBean.getNewsList());
		return homeBean;
	}
}

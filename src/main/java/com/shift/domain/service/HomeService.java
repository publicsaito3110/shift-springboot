package com.shift.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.domain.model.bean.CmnNewsBean;
import com.shift.domain.model.bean.HomeBean;
import com.shift.domain.model.entity.UserEntity;
import com.shift.domain.repository.UserRepository;
import com.shift.domain.service.common.CmnNewsService;

/**
 * @author saito
 *
 */
@Service
public class HomeService extends BaseService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CmnNewsService cmnNewsService;


	/**
	 * [Service] (/home)
	 *
	 * @param loginUser Authenticationから取得したユーザID
	 * @return HomeBean
	 */
	public HomeBean home(String loginUser) {

		//Service内の処理を実行
		UserEntity userEntity = selectUserByLoginUser(loginUser);
		//CmnNewsService(共通サービス)から処理結果を取得
		CmnNewsBean cmnNewsBean = cmnNewsService.generateDisplayNowNews();

		//Beanにセット
		HomeBean homeBean = new HomeBean(userEntity, cmnNewsBean.getNewsList());
		return homeBean;
	}


	/**
	 * [DB]ユーザ検索処理
	 *
	 * <p>loginUserと一致するユーザを取得する<br>
	 * ただし、一致するユーザーがいない場合はnullとなる
	 * </p>
	 *
	 * @param loginUser Authenticationから取得したユーザID
	 * @return UserEntity<br>
	 * フィールド(UserEntity)<br>
	 * id, name, nameKana, gender, password, address, tel, email, note, admin_flg, del_flg
	 */
	private UserEntity selectUserByLoginUser(String loginUser) {

		Optional<UserEntity> userEntityOpt = userRepository.findById(loginUser);

		//ユーザを取得できなかったとき
		if (!userEntityOpt.isPresent()) {
			return null;
		}

		//UserEntityを返す
		return userEntityOpt.get();
	}
}

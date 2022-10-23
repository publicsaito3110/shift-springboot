package com.shift.domain.repository;


import org.springframework.stereotype.Repository;

import com.shift.domain.model.entity.TempPasswordEntity;

/**
 * @author saito
 *
 */
@Repository
public interface TempPasswordRepository extends BaseRepository<TempPasswordEntity, Integer>{


	/**
	 * [DB]ワンタイムパスワード情報検索処理
	 *
	 * <p>userとurlParamに一致するワンタイムパスワード情報を取得する<br>
	 * ただし、一致するワンタイムパスワード情報がないときはnullになる
	 * </p>
	 *
	 * @param user パスワードを再設定する際に登録したユーザID
	 * @param urlParam パスワードを再設定する際に登録したURLパラメータ
	 * @return TempPasswordEntity<br>
	 * フィールド(TempPasswordEntity)<br>
	 * id, user, urlParam, authCode, insertDate
	 */
	public TempPasswordEntity findByUserAndUrlParam(String user, String urlParam);


	/**
	 * [DB]ワンタイムパスワード情報検索処理
	 *
	 * <p>user, urlParam, authCodeに一致するワンタイムパスワード情報を取得する<br>
	 * ただし、一致するワンタイムパスワード情報がないときはnullになる
	 * </p>
	 *
	 * @param user パスワードを再設定する際に登録したユーザID
	 * @param urlParam パスワードを再設定する際に登録したURLパラメータ
	 * @param authCode パスワードを再設定する際に登録した認証コード
	 * @return TempPasswordEntity<br>
	 * フィールド(TempPasswordEntity)<br>
	 * id, user, urlParam, authCode, insertDate
	 */
	public TempPasswordEntity findByUserAndUrlParamAndAuthCode(String userId, String urlParam, String authCode);
}

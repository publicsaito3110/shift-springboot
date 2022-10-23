package com.shift.domain.repository;


import java.util.List;

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
	 * <p>userに一致するワンタイムパスワード情報を取得する<br>
	 * ただし、許容可能日時の範囲内ではないまたは一致するワンタイムパスワード情報がない場合はnullとなる<br>
	 * また、minDate, maxDateがMySQLで許容されている日時フォーマット出ない場合はエラーが発生する
	 * </p>
	 *
	 * @param user パスワードを再設定する際に登録したユーザID
	 * @param minDate 検索最小日時
	 * @param maxDate 検索最大日時
	 * @return List<TempPasswordEntity><br>
	 * フィールド(TempPasswordEntity)<br>
	 * id, user, urlParam, authCode, insertDate
	 */
	public List<TempPasswordEntity> findByUserAndInsertDateBetween(String user, String minDate, String maxDate);


	/**
	 * [DB]ワンタイムパスワード情報検索処理
	 *
	 * <p>userとurlParamに一致するワンタイムパスワード情報を取得する<br>
	 * ただし、許容可能日時の範囲内ではないまたは一致するワンタイムパスワード情報がない場合はnullとなる<br>
	 * また、minDate, maxDateがMySQLで許容されている日時フォーマット出ない場合はエラーが発生する
	 * </p>
	 *
	 * @param user パスワードを再設定する際に登録したユーザID
	 * @param urlParam パスワードを再設定する際に登録したURLパラメータ
	 * @param minDate 検索最小日時
	 * @param maxDate 検索最大日時
	 * @return TempPasswordEntity<br>
	 * フィールド(TempPasswordEntity)<br>
	 * id, user, urlParam, authCode, insertDate
	 */
	public TempPasswordEntity findByUserAndUrlParamAndInsertDateBetween(String user, String urlParam, String minDate, String maxDate);


	/**
	 * [DB]ワンタイムパスワード情報検索処理
	 *
	 * <p>user, urlParam, authCodeに一致するワンタイムパスワード情報を取得する<br>
	 * ただし、許容可能日時の範囲内ではないまたは一致するワンタイムパスワード情報がない場合はnullとなる<br>
	 * また、minDate, maxDateがMySQLで許容されている日時フォーマット出ない場合はエラーが発生する
	 * </p>
	 *
	 * @param user パスワードを再設定する際に登録したユーザID
	 * @param urlParam パスワードを再設定する際に登録したURLパラメータ
	 * @param authCode パスワードを再設定する際に登録した認証コード
	 * @param minDate 検索最小日時
	 * @param maxDate 検索最大日時
	 * @return TempPasswordEntity<br>
	 * フィールド(TempPasswordEntity)<br>
	 * id, user, urlParam, authCode, insertDate
	 */
	public TempPasswordEntity findByUserAndUrlParamAndAuthCodeAndInsertDateBetween(String userId, String urlParam, String authCode, String minDate, String maxDate);
}

package com.shift.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shift.domain.model.entity.NewsEntity;

/**
 * @author saito
 *
 */
@Repository
public interface NewsRepository extends BaseRepository<NewsEntity, Integer> {

	/**
	 * [DB]ニュース検索処理
	 *
	 * <p>ホーム画面に表示するニュースを取得する<br>
	 * 取得するお知らせは現在日を含む過去5件までとなる<br>
	 * ただし、表示可能なお知らせがないときはEmptyとなる
	 * </p>
	 *
	 * @param nowYmd 現在の日付(YYYYMMDD)
	 * @param newsLimit 表示するお知らせの件数
	 * @return List<NewsEntity><br>
	 * フィールド(List&lt;NewsEntity&gt;)<br>
	 * id, ymd, category, title, content
	 */
	@Query(value = "SELECT * FROM news WHERE ymd <= :nowYmd ORDER BY ymd DESC LIMIT :newsLimit",  nativeQuery = true)
	public List<NewsEntity> selectNewsBeforeNowByNowYmdNewsLimit(String nowYmd, int newsLimit);

	/**
	 * [DB]登録済みお知らせ検索処理
	 *
	 * <p>現在の日付以降の登録済みのお知らせを全て取得する<br>
	 * ただし、登録済みのお知らせがないときはEmptyとなる
	 * </p>
	 *
	 * @param nowYmd 現在の日付(YYYYMMDD)
	 * @return List<NewsEntity><br>
	 * フィールド(List&lt;NewsEntity&gt;)<br>
	 * id, ymd, category, title, content
	 */
	public List<NewsEntity> findByYmdGreaterThanOrderByYmd(String nowYmd);
}

package com.shift.domain.repository;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.shift.domain.model.entity.ScheduleEntity;

/**
 * @author saito
 *
 */
@Repository
public interface ScheduleRepository extends BaseRepository<ScheduleEntity, String> {

	/**
	 * [DB]スケジュール検索処理
	 *
	 * <p>現在の日付から1ヵ月分のスケジュールを取得する<br>
	 * ただし、登録済みのお知らせがないときはEmptyとなる
	 * </p>
	 *
	 * @param ym 現在の年月(YYYYMM)<br>
	 * ただし、LIKE句であるため、"ym%" でなければならない
	 * @return List<ScheduleEntity><br>
	 * フィールド(List&lt;ScheduleEntity&gt;)<br>
	 * ymd, user1, user2, user3, memo1, memo2, memo3
	 */
	public List<ScheduleEntity> findByYmdLike(String ym);
}

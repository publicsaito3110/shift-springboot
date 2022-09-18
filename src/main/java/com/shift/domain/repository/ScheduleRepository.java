package com.shift.domain.repository;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.shift.domain.model.entity.ScheduleEntity;

/**
 * @author saito
 *
 */
@Repository
public interface ScheduleRepository extends BaseRepository<ScheduleEntity, Integer> {

	/**
	 * [DB]スケジュール検索処理
	 *
	 * <p>現在の日付から1ヵ月分の確定スケジュールを取得する<br>
	 * また、ユーザID, ymd(昇順)順になる<br>
	 * ただし、登録済みの確定スケジュールがないときはEmptyとなる
	 * </p>
	 *
	 * @param ym 現在の年月(YYYYMM)<br>
	 * ただし、LIKE句であるため、"ym%" でなければならない
	 * @param loginUser Authenticationから取得したユーザID
	 *
	 * @return List<ScheduleEntity><br>
	 * フィールド(List&lt;ScheduleEntity&gt;)<br>
	 * id, ymd, user, schedule
	 */
	public List<ScheduleEntity> findByYmdLikeAndUserOrderByYmd(String ym, String loginUser);
}

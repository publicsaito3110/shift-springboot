package com.shift.domain.repository;


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
	 * <p>現在の年月からユーザーの1ヵ月分のスケジュール予定を取得する<br>
	 * ただし、登録済みのスケジュールがないときはnullとなる<br>
	 * なた、日付が存在しない日(2月 -> 30, 31日etc)は必ず登録されていない
	 * </p>
	 *
	 * @param ym 現在の年月(YYYYMM)
	 * @param loginUser ログインしているユーザー
	 * @return ScheduleEntity<br>
	 * フィールド(ScheduleEntity)<br>
	 * id, ym, user, 1, 2, 3, 4, 5... 30, 31
	 */
	public ScheduleEntity findByYmAndUser(String ym, String loginUser);
}

package com.shift.domain.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shift.domain.model.entity.ScheduleTimeEntity;

/**
 * @author saito
 *
 */
@Repository
public interface ScheduleTimeRepository extends BaseRepository<ScheduleTimeEntity, Integer> {

	/**
	 * [DB]スケジュール時間区分取得処理
	 *
	 * <p>取得したい日付(ymd)から該当するスケジュール時間区分を取得する<br>
	 * また、現在日(ymd)に該当するスケジュール時間区分が複数登録されているときは最新のスケジュール時間区分が取得される<br>
	 * ただし、スケジュール時間区分が何も登録されていないときはnullとなる
	 * </p>
	 *
	 * @param ymd 取得したいスケジュール時間区分の日付(YYYYMMDD)
	 * @return ScheduleTimeEntity<br>
	 * フィールド(ScheduleTimeEntity)<br>
	 * id, endYmd, name1, startHm1, endHM1, restHm1... startHm7, endHM7, restHm7
	 */
	@Query(value = "SELECT a.* FROM (SELECT DISTINCT s.* FROM schedule_time s WHERE :ymd <= s.end_ymd AND s.end_ymd = (SELECT MIN(c.end_ymd) FROM schedule_time c WHERE :ymd <= c.end_ymd) ORDER BY s.id DESC) a GROUP BY a.end_ymd", nativeQuery = true)
	public ScheduleTimeEntity selectScheduleTimeByYmd(String ymd);
}

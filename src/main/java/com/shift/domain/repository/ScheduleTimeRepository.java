package com.shift.domain.repository;


import java.util.List;

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
	@Query(value = " SELECT s.* FROM schedule_time s WHERE s.end_ymd = (SELECT MIN(c.end_ymd) FROM schedule_time c WHERE :ymd <= c.end_ymd) AND s.id = (SELECT MAX(h.id) FROM schedule_time h WHERE h.end_ymd = (SELECT MIN(e.end_ymd) FROM schedule_time e WHERE :ymd <= e.end_ymd))", nativeQuery = true)
	public ScheduleTimeEntity selectScheduleTimeByYmd(String ymd);


	/**
	 * [DB]指定日以降スケジュール時間区分取得処理
	 *
	 * <p>取得したい日付(ymd)以降のスケジュール時間区分を全て取得する<br>
	 * また、同じ日付(ymd)にスケジュール時間区分が複数登録されているときは最新のスケジュール時間区分が取得される<br>
	 * ただし、スケジュール時間区分が何も登録されていないときはEmptyとなる
	 * </p>
	 *
	 * @param ymd 取得したいスケジュール時間区分の日付(YYYYMMDD)
	 * @return List<ScheduleTimeEntity><br>
	 * フィールド(List&lt;ScheduleTimeEntity&gt;)<br>
	 * id, endYmd, name1, startHm1, endHM1, restHm1... startHm7, endHM7, restHm7
	 */
	@Query(value = "SELECT a.* FROM (SELECT s.*, RANK() OVER (PARTITION BY s.end_ymd ORDER BY s.id DESC) AS rk FROM schedule_time s WHERE :ymd <= s.end_ymd) a WHERE :ymd <= a.end_ymd AND a.rk = 1", nativeQuery = true)
	public List<ScheduleTimeEntity> selectScheduleTimeALLByYmd(String ymd);
}

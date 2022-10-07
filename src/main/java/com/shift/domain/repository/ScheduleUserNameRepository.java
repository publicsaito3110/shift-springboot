package com.shift.domain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shift.domain.model.dto.ScheduleUserNameDto;

/**
 * @author saito
 *
 */
@Repository
public interface ScheduleUserNameRepository extends BaseRepository<ScheduleUserNameDto, Integer> {


	/**
	 * [DB]確定スケジュール登録済みユーザ取得処理
	 *
	 * <p>確定スケジュールに登録済みのユーザを全てをユーザ名で取得する<br>
	 * ただし、登録済みのユーザが1人もないときはEmptyとなる
	 * </p>
	 *
	 * @param ym 検索したい年月(YYYYMM)
	 * @return List<ScheduleUserNameDto><br>
	 * フィールド(&lt;ScheduleUserNameDto&gt;)<br>
	 * id, ym, userName, day1, day2, day3... day30, day31
	 */
	@Query(value = "SELECT s.id, s.ym, u.name AS user_name, s.day1, s.day2, s.day3, s.day4, s.day5, s.day6, s.day7, s.day8, s.day9, s.day10, s.day11, s.day12, s.day13, s.day14, s.day15, s.day16, s.day17, s.day18, s.day19, s.day20, s.day21, s.day22, s.day23, s.day24, s.day25, s.day26, s.day27, s.day28, s.day29, s.day30, s.day31 FROM schedule s INNER JOIN user u ON u.id = s.user WHERE s.ym = :ym ORDER BY u.id", nativeQuery = true)
	public List<ScheduleUserNameDto> selectScheduleUserNameByYm(String ym);
}

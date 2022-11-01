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
public interface ScheduleUserNameRepository extends BaseRepository<ScheduleUserNameDto, String> {


	/**
	 * [DB]確定スケジュール登録済みユーザ取得処理
	 *
	 * <p>確定スケジュールに登録済みのユーザを全てをユーザ名で取得する<br>
	 * ただし、登録済みのユーザが1人もないときはEmptyとなる
	 * </p>
	 *
	 * @param ym 取得したい年月(YYYYMM)
	 * @return List<ScheduleUserNameDto><br>
	 * フィールド(&lt;ScheduleUserNameDto&gt;)<br>
	 * id, userName, day1, day2, day3... day30, day31
	 */
	@Query(value = "SELECT u.id, u.name AS user_name, s.day1, s.day2, s.day3, s.day4, s.day5, s.day6, s.day7, s.day8, s.day9, s.day10, s.day11, s.day12, s.day13, s.day14, s.day15, s.day16, s.day17, s.day18, s.day19, s.day20, s.day21, s.day22, s.day23, s.day24, s.day25, s.day26, s.day27, s.day28, s.day29, s.day30, s.day31 FROM schedule s INNER JOIN user u ON u.id = s.user WHERE s.ym = :ym ORDER BY u.id", nativeQuery = true)
	public List<ScheduleUserNameDto> selectScheduleUserNameByYm(String ym);


	/**
	 * [DB]年間比較の1ヵ月分確定スケジュール登録済みユーザ取得処理
	 *
	 * <p>年間で登録されているうち1ヵ月分の確定スケジュールに登録済みのユーザを取得する<br>
	 * 対象の年月にスケジュールが登録されていなくても、その年の一度でもスケジュールが登録されていれば取得される
	 * ただし、取得したい年月の年に登録済みのユーザが1人もないときはEmptyとなる
	 * </p>
	 *
	 * @param ym 取得したい年月
	 * @param year 取得したい年月の年<br>
	 * ただし、LIKE検索されるため"2022%"でなければならない
	 * @return List<ScheduleUserNameDto><br>
	 * フィールド(&lt;ScheduleUserNameDto&gt;)<br>
	 * id, userName, day1, day2, day3... day30, day31
	 */
	@Query(value = "SELECT u.id, u.name AS user_name, (SELECT CASE COUNT(s.day1) WHEN 0 THEN '0000000' ELSE s.day1 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day1, (SELECT CASE COUNT(s.day2) WHEN 0 THEN '0000000' ELSE s.day2 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day2, (SELECT CASE COUNT(s.day3) WHEN 0 THEN '0000000' ELSE s.day3 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day3, (SELECT CASE COUNT(s.day4) WHEN 0 THEN '0000000' ELSE s.day4 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day4, (SELECT CASE COUNT(s.day5) WHEN 0 THEN '0000000' ELSE s.day5 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day5, (SELECT CASE COUNT(s.day6) WHEN 0 THEN '0000000' ELSE s.day6 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day6, (SELECT CASE COUNT(s.day7) WHEN 0 THEN '0000000' ELSE s.day7 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day7, (SELECT CASE COUNT(s.day8) WHEN 0 THEN '0000000' ELSE s.day8 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day8, (SELECT CASE COUNT(s.day9) WHEN 0 THEN '0000000' ELSE s.day9 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day9, (SELECT CASE COUNT(s.day10) WHEN 0 THEN '0000000' ELSE s.day10 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day10, (SELECT CASE COUNT(s.day11) WHEN 0 THEN '0000000' ELSE s.day11 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day11, (SELECT CASE COUNT(s.day12) WHEN 0 THEN '0000000' ELSE s.day12 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day12, (SELECT CASE COUNT(s.day13) WHEN 0 THEN '0000000' ELSE s.day13 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day13, (SELECT CASE COUNT(s.day14) WHEN 0 THEN '0000000' ELSE s.day14 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day14, (SELECT CASE COUNT(s.day15) WHEN 0 THEN '0000000' ELSE s.day15 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day15, (SELECT CASE COUNT(s.day16) WHEN 0 THEN '0000000' ELSE s.day16 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day16, (SELECT CASE COUNT(s.day17) WHEN 0 THEN '0000000' ELSE s.day17 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day17, (SELECT CASE COUNT(s.day18) WHEN 0 THEN '0000000' ELSE s.day18 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day18, (SELECT CASE COUNT(s.day19) WHEN 0 THEN '0000000' ELSE s.day19 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day19, (SELECT CASE COUNT(s.day20) WHEN 0 THEN '0000000' ELSE s.day20 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day20, (SELECT CASE COUNT(s.day21) WHEN 0 THEN '0000000' ELSE s.day21 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day21, (SELECT CASE COUNT(s.day22) WHEN 0 THEN '0000000' ELSE s.day22 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day22, (SELECT CASE COUNT(s.day23) WHEN 0 THEN '0000000' ELSE s.day23 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day23, (SELECT CASE COUNT(s.day24) WHEN 0 THEN '0000000' ELSE s.day24 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day24, (SELECT CASE COUNT(s.day25) WHEN 0 THEN '0000000' ELSE s.day25 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day25, (SELECT CASE COUNT(s.day26) WHEN 0 THEN '0000000' ELSE s.day26 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day26, (SELECT CASE COUNT(s.day27) WHEN 0 THEN '0000000' ELSE s.day27 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day27, (SELECT CASE COUNT(s.day28) WHEN 0 THEN '0000000' ELSE s.day28 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day28, (SELECT CASE COUNT(s.day29) WHEN 0 THEN '0000000' ELSE s.day29 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day29, (SELECT CASE COUNT(s.day30) WHEN 0 THEN '0000000' ELSE s.day30 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day30, (SELECT CASE COUNT(s.day31) WHEN 0 THEN '0000000' ELSE s.day31 END FROM schedule s WHERE s.user = a.user AND s.ym = :ym) AS day31 FROM (SELECT DISTINCT s.user FROM schedule s WHERE s.ym LIKE :year) a INNER JOIN user u ON u.id = a.user", nativeQuery = true)
	public List<ScheduleUserNameDto> selectScheduleUserNameForYear(String ym, String year);
}

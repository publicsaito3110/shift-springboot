package com.shift.domain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shift.domain.model.dto.ScheduleUserDto;

/**
 * @author saito
 *
 */
@Repository
public interface ScheduleUserRepository extends BaseRepository<ScheduleUserDto, Integer> {


	/**
	 * [DB]1日目の確定スケジュール取得処理
	 *
	 * <p>ym1日に該当する確定スケジュールをユーザごとに取得する<br>
	 * ただし、登録済みのスケジュールが1つもないときはEmptyとなる
	 * </p>
	 *
	 * @param schedule1 dayのスケジュール時間1に登録されている検索したい1文字目 (%1______%)
	 * @param schedule2 dayのスケジュール時間2に登録されている検索したい2文字目 (%_1_____%)
	 * @param schedule3 dayのスケジュール時間3に登録されている検索したい3文字目 (%__1____%)
	 * @param schedule4 dayのスケジュール時間4に登録されている検索したい4文字目 (%___1___%)
	 * @param schedule5 dayのスケジュール時間5に登録されている検索したい5文字目 (%____1__%)
	 * @param schedule6 dayのスケジュール時間6に登録されている検索したい6文字目 (%_____1_%)
	 * @param schedule7 dayのスケジュール時間7に登録されている検索したい7文字目 (%______1%)
	 * @param replaceValue 検索した際に、一致したとき格納する値
	 * @param ym 検索したい年月(YYYYMM)
	 * @return List<ScheduleUserDto><br>
	 * フィールド(&lt;ScheduleUserDto&gt;)<br>
	 * id, user, schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7
	 */
	@Query(value = "select s.id, s.user, case when s.day1 like :schedule1 then :replaceValue end as schedule1, case when s.day1 like :schedule2 then :replaceValue end as schedule2, case when s.day1 like :schedule3 then :replaceValue end as schedule3, case when s.day1 like :schedule4 then :replaceValue end as schedule4, case when s.day1 like :schedule5 then :replaceValue end as schedule5, case when s.day1 like :schedule6 then :replaceValue end as schedule6, case when s.day1 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay1BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);
}

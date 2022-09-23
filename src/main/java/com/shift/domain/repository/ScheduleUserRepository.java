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


	/**
	 * [DB]2日目の確定スケジュール取得処理
	 *
	 * <p>ym2日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day2 like :schedule1 then :replaceValue end as schedule1, case when s.day2 like :schedule2 then :replaceValue end as schedule2, case when s.day2 like :schedule3 then :replaceValue end as schedule3, case when s.day2 like :schedule4 then :replaceValue end as schedule4, case when s.day2 like :schedule5 then :replaceValue end as schedule5, case when s.day2 like :schedule6 then :replaceValue end as schedule6, case when s.day2 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay2BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]3日目の確定スケジュール取得処理
	 *
	 * <p>ym3日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day3 like :schedule1 then :replaceValue end as schedule1, case when s.day3 like :schedule2 then :replaceValue end as schedule2, case when s.day3 like :schedule3 then :replaceValue end as schedule3, case when s.day3 like :schedule4 then :replaceValue end as schedule4, case when s.day3 like :schedule5 then :replaceValue end as schedule5, case when s.day3 like :schedule6 then :replaceValue end as schedule6, case when s.day3 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay3BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]4日目の確定スケジュール取得処理
	 *
	 * <p>ym4日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day4 like :schedule1 then :replaceValue end as schedule1, case when s.day4 like :schedule2 then :replaceValue end as schedule2, case when s.day4 like :schedule3 then :replaceValue end as schedule3, case when s.day4 like :schedule4 then :replaceValue end as schedule4, case when s.day4 like :schedule5 then :replaceValue end as schedule5, case when s.day4 like :schedule6 then :replaceValue end as schedule6, case when s.day4 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay4BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]5日目の確定スケジュール取得処理
	 *
	 * <p>ym5日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day5 like :schedule1 then :replaceValue end as schedule1, case when s.day5 like :schedule2 then :replaceValue end as schedule2, case when s.day5 like :schedule3 then :replaceValue end as schedule3, case when s.day5 like :schedule4 then :replaceValue end as schedule4, case when s.day5 like :schedule5 then :replaceValue end as schedule5, case when s.day5 like :schedule6 then :replaceValue end as schedule6, case when s.day5 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay5BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]6日目の確定スケジュール取得処理
	 *
	 * <p>ym6日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day6 like :schedule1 then :replaceValue end as schedule1, case when s.day6 like :schedule2 then :replaceValue end as schedule2, case when s.day6 like :schedule3 then :replaceValue end as schedule3, case when s.day6 like :schedule4 then :replaceValue end as schedule4, case when s.day6 like :schedule5 then :replaceValue end as schedule5, case when s.day6 like :schedule6 then :replaceValue end as schedule6, case when s.day6 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay6BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]7日目の確定スケジュール取得処理
	 *
	 * <p>ym7日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day7 like :schedule1 then :replaceValue end as schedule1, case when s.day7 like :schedule2 then :replaceValue end as schedule2, case when s.day7 like :schedule3 then :replaceValue end as schedule3, case when s.day7 like :schedule4 then :replaceValue end as schedule4, case when s.day7 like :schedule5 then :replaceValue end as schedule5, case when s.day7 like :schedule6 then :replaceValue end as schedule6, case when s.day7 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay7BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]8日目の確定スケジュール取得処理
	 *
	 * <p>ym8日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day8 like :schedule1 then :replaceValue end as schedule1, case when s.day8 like :schedule2 then :replaceValue end as schedule2, case when s.day8 like :schedule3 then :replaceValue end as schedule3, case when s.day8 like :schedule4 then :replaceValue end as schedule4, case when s.day8 like :schedule5 then :replaceValue end as schedule5, case when s.day8 like :schedule6 then :replaceValue end as schedule6, case when s.day8 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay8BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]9日目の確定スケジュール取得処理
	 *
	 * <p>ym9日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day9 like :schedule1 then :replaceValue end as schedule1, case when s.day9 like :schedule2 then :replaceValue end as schedule2, case when s.day9 like :schedule3 then :replaceValue end as schedule3, case when s.day9 like :schedule4 then :replaceValue end as schedule4, case when s.day9 like :schedule5 then :replaceValue end as schedule5, case when s.day9 like :schedule6 then :replaceValue end as schedule6, case when s.day9 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay9BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]10日目の確定スケジュール取得処理
	 *
	 * <p>ym10日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day10 like :schedule1 then :replaceValue end as schedule1, case when s.day10 like :schedule2 then :replaceValue end as schedule2, case when s.day10 like :schedule3 then :replaceValue end as schedule3, case when s.day10 like :schedule4 then :replaceValue end as schedule4, case when s.day10 like :schedule5 then :replaceValue end as schedule5, case when s.day10 like :schedule6 then :replaceValue end as schedule6, case when s.day10 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay10BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]11日目の確定スケジュール取得処理
	 *
	 * <p>ym11日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day11 like :schedule1 then :replaceValue end as schedule1, case when s.day11 like :schedule2 then :replaceValue end as schedule2, case when s.day11 like :schedule3 then :replaceValue end as schedule3, case when s.day11 like :schedule4 then :replaceValue end as schedule4, case when s.day11 like :schedule5 then :replaceValue end as schedule5, case when s.day11 like :schedule6 then :replaceValue end as schedule6, case when s.day11 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay11BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]12日目の確定スケジュール取得処理
	 *
	 * <p>ym12日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day12 like :schedule1 then :replaceValue end as schedule1, case when s.day12 like :schedule2 then :replaceValue end as schedule2, case when s.day12 like :schedule3 then :replaceValue end as schedule3, case when s.day12 like :schedule4 then :replaceValue end as schedule4, case when s.day12 like :schedule5 then :replaceValue end as schedule5, case when s.day12 like :schedule6 then :replaceValue end as schedule6, case when s.day12 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay12BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]13日目の確定スケジュール取得処理
	 *
	 * <p>ym13日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day13 like :schedule1 then :replaceValue end as schedule1, case when s.day13 like :schedule2 then :replaceValue end as schedule2, case when s.day13 like :schedule3 then :replaceValue end as schedule3, case when s.day13 like :schedule4 then :replaceValue end as schedule4, case when s.day13 like :schedule5 then :replaceValue end as schedule5, case when s.day13 like :schedule6 then :replaceValue end as schedule6, case when s.day13 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay13BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]14日目の確定スケジュール取得処理
	 *
	 * <p>ym14日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day14 like :schedule1 then :replaceValue end as schedule1, case when s.day14 like :schedule2 then :replaceValue end as schedule2, case when s.day14 like :schedule3 then :replaceValue end as schedule3, case when s.day14 like :schedule4 then :replaceValue end as schedule4, case when s.day14 like :schedule5 then :replaceValue end as schedule5, case when s.day14 like :schedule6 then :replaceValue end as schedule6, case when s.day14 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay14BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]15日目の確定スケジュール取得処理
	 *
	 * <p>ym15日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day15 like :schedule1 then :replaceValue end as schedule1, case when s.day15 like :schedule2 then :replaceValue end as schedule2, case when s.day15 like :schedule3 then :replaceValue end as schedule3, case when s.day15 like :schedule4 then :replaceValue end as schedule4, case when s.day15 like :schedule5 then :replaceValue end as schedule5, case when s.day15 like :schedule6 then :replaceValue end as schedule6, case when s.day15 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay15BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]16日目の確定スケジュール取得処理
	 *
	 * <p>ym16日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day16 like :schedule1 then :replaceValue end as schedule1, case when s.day16 like :schedule2 then :replaceValue end as schedule2, case when s.day16 like :schedule3 then :replaceValue end as schedule3, case when s.day16 like :schedule4 then :replaceValue end as schedule4, case when s.day16 like :schedule5 then :replaceValue end as schedule5, case when s.day16 like :schedule6 then :replaceValue end as schedule6, case when s.day16 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay16BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]17日目の確定スケジュール取得処理
	 *
	 * <p>ym17日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day17 like :schedule1 then :replaceValue end as schedule1, case when s.day17 like :schedule2 then :replaceValue end as schedule2, case when s.day17 like :schedule3 then :replaceValue end as schedule3, case when s.day17 like :schedule4 then :replaceValue end as schedule4, case when s.day17 like :schedule5 then :replaceValue end as schedule5, case when s.day17 like :schedule6 then :replaceValue end as schedule6, case when s.day17 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay17BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]18日目の確定スケジュール取得処理
	 *
	 * <p>ym18日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day18 like :schedule1 then :replaceValue end as schedule1, case when s.day18 like :schedule2 then :replaceValue end as schedule2, case when s.day18 like :schedule3 then :replaceValue end as schedule3, case when s.day18 like :schedule4 then :replaceValue end as schedule4, case when s.day18 like :schedule5 then :replaceValue end as schedule5, case when s.day18 like :schedule6 then :replaceValue end as schedule6, case when s.day18 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay18BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]19日目の確定スケジュール取得処理
	 *
	 * <p>ym19日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day19 like :schedule1 then :replaceValue end as schedule1, case when s.day19 like :schedule2 then :replaceValue end as schedule2, case when s.day19 like :schedule3 then :replaceValue end as schedule3, case when s.day19 like :schedule4 then :replaceValue end as schedule4, case when s.day19 like :schedule5 then :replaceValue end as schedule5, case when s.day19 like :schedule6 then :replaceValue end as schedule6, case when s.day19 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay19BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]20日目の確定スケジュール取得処理
	 *
	 * <p>ym20日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day20 like :schedule1 then :replaceValue end as schedule1, case when s.day20 like :schedule2 then :replaceValue end as schedule2, case when s.day20 like :schedule3 then :replaceValue end as schedule3, case when s.day20 like :schedule4 then :replaceValue end as schedule4, case when s.day20 like :schedule5 then :replaceValue end as schedule5, case when s.day20 like :schedule6 then :replaceValue end as schedule6, case when s.day20 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay20BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]21日目の確定スケジュール取得処理
	 *
	 * <p>ym21日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day21 like :schedule1 then :replaceValue end as schedule1, case when s.day21 like :schedule2 then :replaceValue end as schedule2, case when s.day21 like :schedule3 then :replaceValue end as schedule3, case when s.day21 like :schedule4 then :replaceValue end as schedule4, case when s.day21 like :schedule5 then :replaceValue end as schedule5, case when s.day21 like :schedule6 then :replaceValue end as schedule6, case when s.day21 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay21BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]22日目の確定スケジュール取得処理
	 *
	 * <p>ym22日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day22 like :schedule1 then :replaceValue end as schedule1, case when s.day22 like :schedule2 then :replaceValue end as schedule2, case when s.day22 like :schedule3 then :replaceValue end as schedule3, case when s.day22 like :schedule4 then :replaceValue end as schedule4, case when s.day22 like :schedule5 then :replaceValue end as schedule5, case when s.day22 like :schedule6 then :replaceValue end as schedule6, case when s.day22 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay22BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]23日目の確定スケジュール取得処理
	 *
	 * <p>ym23日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day23 like :schedule1 then :replaceValue end as schedule1, case when s.day23 like :schedule2 then :replaceValue end as schedule2, case when s.day23 like :schedule3 then :replaceValue end as schedule3, case when s.day23 like :schedule4 then :replaceValue end as schedule4, case when s.day23 like :schedule5 then :replaceValue end as schedule5, case when s.day23 like :schedule6 then :replaceValue end as schedule6, case when s.day23 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay23BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]24日目の確定スケジュール取得処理
	 *
	 * <p>ym24日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day24 like :schedule1 then :replaceValue end as schedule1, case when s.day24 like :schedule2 then :replaceValue end as schedule2, case when s.day24 like :schedule3 then :replaceValue end as schedule3, case when s.day24 like :schedule4 then :replaceValue end as schedule4, case when s.day24 like :schedule5 then :replaceValue end as schedule5, case when s.day24 like :schedule6 then :replaceValue end as schedule6, case when s.day24 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay24BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]25日目の確定スケジュール取得処理
	 *
	 * <p>ym25日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day25 like :schedule1 then :replaceValue end as schedule1, case when s.day25 like :schedule2 then :replaceValue end as schedule2, case when s.day25 like :schedule3 then :replaceValue end as schedule3, case when s.day25 like :schedule4 then :replaceValue end as schedule4, case when s.day25 like :schedule5 then :replaceValue end as schedule5, case when s.day25 like :schedule6 then :replaceValue end as schedule6, case when s.day25 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay25BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]26日目の確定スケジュール取得処理
	 *
	 * <p>ym26日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day26 like :schedule1 then :replaceValue end as schedule1, case when s.day26 like :schedule2 then :replaceValue end as schedule2, case when s.day26 like :schedule3 then :replaceValue end as schedule3, case when s.day26 like :schedule4 then :replaceValue end as schedule4, case when s.day26 like :schedule5 then :replaceValue end as schedule5, case when s.day26 like :schedule6 then :replaceValue end as schedule6, case when s.day26 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay26BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]27日目の確定スケジュール取得処理
	 *
	 * <p>ym27日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day27 like :schedule1 then :replaceValue end as schedule1, case when s.day27 like :schedule2 then :replaceValue end as schedule2, case when s.day27 like :schedule3 then :replaceValue end as schedule3, case when s.day27 like :schedule4 then :replaceValue end as schedule4, case when s.day27 like :schedule5 then :replaceValue end as schedule5, case when s.day27 like :schedule6 then :replaceValue end as schedule6, case when s.day27 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay27BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]28日目の確定スケジュール取得処理
	 *
	 * <p>ym28日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day28 like :schedule1 then :replaceValue end as schedule1, case when s.day28 like :schedule2 then :replaceValue end as schedule2, case when s.day28 like :schedule3 then :replaceValue end as schedule3, case when s.day28 like :schedule4 then :replaceValue end as schedule4, case when s.day28 like :schedule5 then :replaceValue end as schedule5, case when s.day28 like :schedule6 then :replaceValue end as schedule6, case when s.day28 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay28BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]29日目の確定スケジュール取得処理
	 *
	 * <p>ym29日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day29 like :schedule1 then :replaceValue end as schedule1, case when s.day29 like :schedule2 then :replaceValue end as schedule2, case when s.day29 like :schedule3 then :replaceValue end as schedule3, case when s.day29 like :schedule4 then :replaceValue end as schedule4, case when s.day29 like :schedule5 then :replaceValue end as schedule5, case when s.day29 like :schedule6 then :replaceValue end as schedule6, case when s.day29 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay29BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]30日目の確定スケジュール取得処理
	 *
	 * <p>ym30日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day30 like :schedule1 then :replaceValue end as schedule1, case when s.day30 like :schedule2 then :replaceValue end as schedule2, case when s.day30 like :schedule3 then :replaceValue end as schedule3, case when s.day30 like :schedule4 then :replaceValue end as schedule4, case when s.day30 like :schedule5 then :replaceValue end as schedule5, case when s.day30 like :schedule6 then :replaceValue end as schedule6, case when s.day30 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay30BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);


	/**
	 * [DB]31日目の確定スケジュール取得処理
	 *
	 * <p>ym31日に該当する確定スケジュールをユーザごとに取得する<br>
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
	@Query(value = "select s.id, s.user, case when s.day31 like :schedule1 then :replaceValue end as schedule1, case when s.day31 like :schedule2 then :replaceValue end as schedule2, case when s.day31 like :schedule3 then :replaceValue end as schedule3, case when s.day31 like :schedule4 then :replaceValue end as schedule4, case when s.day31 like :schedule5 then :replaceValue end as schedule5, case when s.day31 like :schedule6 then :replaceValue end as schedule6, case when s.day31 like :schedule7 then :replaceValue end as schedule7 from schedule s where s.ym = :ym order by s.user", nativeQuery = true)
	public List<ScheduleUserDto> selectScheduleDay31BySchedulRepalceValueYm(String schedule1, String schedule2, String schedule3, String schedule4, String schedule5, String schedule6, String schedule7, String replaceValue, String ym);
}

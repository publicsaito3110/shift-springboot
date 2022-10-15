/* テストデータ1(MySQL) */

/* dm */
insert into dm(msg,send_user,receive_user,msg_date,read_flg) values
    ('こんにちは','A001','A100',TIMESTAMP '2022-10-12 21:26:06.000',null)
  , ('こんにちは','A001','A101',TIMESTAMP '2022-10-12 21:26:24.000',null)
  , ('こんにちは','A001','A102',TIMESTAMP '2022-10-12 21:26:33.000',null)
  , ('こんにちは','A001','A103',TIMESTAMP '2022-10-12 21:26:46.000',null)
  , ('こんにちは','A001','A104',TIMESTAMP '2022-10-12 21:27:04.000',null)
  , ('よろしく','A100','A001',TIMESTAMP '2022-10-12 21:27:44.000',null)
  , ('よろしく','A101','A001',TIMESTAMP '2022-10-12 21:28:13.000',null)
  , ('よろしく','A102','A001',TIMESTAMP '2022-10-12 21:28:32.000',null)
  , ('よろしく','A103','A001',TIMESTAMP '2022-10-12 21:28:50.000',null)
  , ('よろしく','A104','A001',TIMESTAMP '2022-10-12 21:29:06.000',null);


/* news */
insert into news(ymd,category,title,content) values
    ('20221001','1','お知らせ1','お知らせ1詳細')
  , ('20221002','2','お知らせ2','お知らせ2詳細')
  , ('20221003','3','お知らせ3','お知らせ3詳細')
  , ('20221004','1','お知らせ4','お知らせ4詳細')
  , ('20221005','2','お知らせ5','お知らせ5詳細')
  , ('20221006','3','お知らせ6','お知らせ6詳細')
  , ('20221007','1','お知らせ7','お知らせ7詳細')
  , ('20221008','2','お知らせ8','お知らせ8詳細')
  , ('20221009','3','お知らせ9','お知らせ9詳細')
  , ('20221010','1','お知らせ10','お知らせ10詳細')
  , ('20221011','2','お知らせ11','お知らせ11詳細')
  , ('20221012','3','お知らせ12','お知らせ12詳細')
  , ('20221013','1','お知らせ13','お知らせ13詳細')
  , ('20221014','2','お知らせ14','お知らせ14詳細')
  , ('20221015','3','お知らせ15','お知らせ15詳細')
  , ('20221016','1','お知らせ16','お知らせ16詳細')
  , ('20221017','2','お知らせ17','お知らせ17詳細')
  , ('20221018','3','お知らせ18','お知らせ18詳細')
  , ('20221019','1','お知らせ19','お知らせ19詳細')
  , ('20221020','2','お知らせ20','お知らせ20詳細')
  , ('20221021','3','お知らせ21','お知らせ21詳細')
  , ('20221022','1','お知らせ22','お知らせ22詳細')
  , ('20221023','2','お知らせ23','お知らせ23詳細')
  , ('20221024','3','お知らせ24','お知らせ24詳細')
  , ('20221025','1','お知らせ25','お知らせ25詳細')
  , ('20221026','2','お知らせ26','お知らせ26詳細')
  , ('20221027','3','お知らせ27','お知らせ27詳細')
  , ('20221028','1','お知らせ28','お知らせ28詳細')
  , ('20221029','2','お知らせ29','お知らせ29詳細')
  , ('20221030','3','お知らせ30','お知らせ30詳細')
  , ('20221101','1','お知らせ31','お知らせ31詳細')
  , ('20221102','2','お知らせ32','お知らせ32詳細')
  , ('20221103','3','お知らせ33','お知らせ33詳細')
  , ('20221104','1','お知らせ34','お知らせ34詳細')
  , ('20221105','2','お知らせ35','お知らせ35詳細')
  , ('20221106','3','お知らせ36','お知らせ36詳細')
  , ('20221107','1','お知らせ37','お知らせ37詳細')
  , ('20221108','2','お知らせ38','お知らせ38詳細')
  , ('20221109','3','お知らせ39','お知らせ39詳細')
  , ('20221110','1','お知らせ40','お知らせ40詳細')
  , ('20221111','2','お知らせ41','お知らせ41詳細')
  , ('20221112','3','お知らせ42','お知らせ42詳細')
  , ('20221113','1','お知らせ43','お知らせ43詳細')
  , ('20221114','2','お知らせ44','お知らせ44詳細')
  , ('20221115','3','お知らせ45','お知らせ45詳細')
  , ('20221116','1','お知らせ46','お知らせ46詳細')
  , ('20221117','2','お知らせ47','お知らせ47詳細')
  , ('20221118','3','お知らせ48','お知らせ48詳細')
  , ('20221119','1','お知らせ49','お知らせ49詳細')
  , ('20221120','2','お知らせ50','お知らせ50詳細');



/* schedule */
insert into schedule(ym,`user`,day1,day2,day3,day4,day5,day6,day7,day8,day9,day10,day11,day12,day13,day14,day15,day16,day17,day18,day19,day20,day21,day22,day23,day24,day25,day26,day27,day28,day29,day30,day31) values
    ('202210','A100','1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000')
  , ('202210','A101','0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000')
  , ('202210','A102','0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000')
  , ('202210','A103','1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000')
  , ('202210','A104','0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null)
  , ('202210','A105','1100000',null,null,null,'1100000',null,null,null,'1100000',null,null,null,'1100000',null,null,null,'1100000',null,null,null,'1100000',null,null,null,'1100000',null,null,'1100000',null,null,null)
  , ('202211','A100','1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000')
  , ('202211','A101','0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000')
  , ('202211','A102','0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000')
  , ('202211','A103','1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000')
  , ('202211','A104','0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null)
  , ('202211','A105','1100000',null,null,null,'1100000',null,null,null,'1100000',null,null,null,'1100000',null,null,null,'1100000',null,null,null,'1100000',null,null,null,'1100000',null,null,'1100000',null,null,null)
  , ('202209','A100','1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000',null,'1000000')
  , ('202209','A101','0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000','0100000',null,'0100000')
  , ('202209','A102','0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000',null,'0010000','0010000','0010000')
  , ('202209','A103','1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000',null,null,'1010000')
  , ('202209','A104','0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null,null,'0110000','0110000',null)
  , ('202209','A105','1100000',null,null,null,'1100000',null,null,null,'1100000',null,null,null,'1100000',null,null,null,'1100000',null,null,null,'1100000',null,null,null,'1100000',null,null,'1100000',null,null,null);


/* schedule_pre  */
insert into schedule_pre(ym,`user`,day1,day2,day3,day4,day5,day6,day7,day8,day9,day10,day11,day12,day13,day14,day15,day16,day17,day18,day19,day20,day21,day22,day23,day24,day25,day26,day27,day28,day29,day30,day31) values
    ('202210','A100','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000')
  , ('202209','A100','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','0000000')
  , ('202211','A100','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','1000000','0000000')
  , ('202210','A101','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000')
  , ('202209','A101','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0000000')
  , ('202211','A101','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0100000','0000000')
  , ('202210','A102','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000')
  , ('202209','A102','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0000000')
  , ('202211','A102','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0010000','0000000')
  , ('202210','A103','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000')
  , ('202211','A103','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','0000000')
  , ('202209','A103','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','1010000','0000000')
  , ('202210','A104','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000')
  , ('202209','A104','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0000000')
  , ('202211','A104','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0110000','0000000')
  , ('202210','A105','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000')
  , ('202209','A105','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','0000000')
  , ('202211','A105','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','1100000','0000000');


/* schedule_time */
insert into schedule_time(end_ymd,name1,start_hm1,end_hm1,rest_hm1,name2,start_hm2,end_hm2,rest_hm2,name3,start_hm3,end_hm3,rest_hm3,name4,start_hm4,end_hm4,rest_hm4,name5,start_hm5,end_hm5,rest_hm5,name6,start_hm6,end_hm6,rest_hm6,name7,start_hm7,end_hm7,rest_hm7) values
    ('20220430','朝番1','0900','1100','0000','昼番1','1100','1300','0000','夜番1','1300','1500','0000',null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
  , ('20220630','朝番2','0900','1200','0000','昼番2','1200','1400','0000','夜番2','1400','1600','0000',null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
  , ('20221031','朝番3','0900','1200','0000','昼番3','1200','1400','0000','夜番3','1400','1600','0000',null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
  , ('20221130','朝番4','0900','1200','0000','昼番4','1200','1400','0000','夜番4','1400','1600','0000',null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
  , ('99999999','朝番5','0900','1200','0000','昼番5','1200','1400','0000','夜番5','1400','1600','0000',null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);


/* user */
/* パスワードは全て1234 */
/* emailにメールアドレスを登録している場合、実際にメールが届くので注意(emailはメールアドレス重複禁止) */
insert into `user`(id,name,name_kana,gender,password,address,tel,email,note,icon_kbn,admin_flg,del_flg) values
    ('A001','管理者','カンリシャ','1','$2a$10$pQPVrr/TEy95RvfI8BVG6.uNTbTSdbCT7ozzI5fmPvSkFYKbwrS/2',null,null,null,'管理者',null,'1',null)
  , ('A100','新井','アライ','1','$2a$10$pQPVrr/TEy95RvfI8BVG6.uNTbTSdbCT7ozzI5fmPvSkFYKbwrS/2',null,null,null,null,null,null,null)
  , ('A101','今田','イマダ','2','$2a$10$pQPVrr/TEy95RvfI8BVG6.uNTbTSdbCT7ozzI5fmPvSkFYKbwrS/2',null,null,null,null,null,null,null)
  , ('A102','上田','ウエダ','1','$2a$10$pQPVrr/TEy95RvfI8BVG6.uNTbTSdbCT7ozzI5fmPvSkFYKbwrS/2',null,null,null,null,null,null,null)
  , ('A103','江口','エグチ','1','$2a$10$pQPVrr/TEy95RvfI8BVG6.uNTbTSdbCT7ozzI5fmPvSkFYKbwrS/2',null,null,null,null,null,null,null)
  , ('A104','太田','オオタ','2','$2a$10$pQPVrr/TEy95RvfI8BVG6.uNTbTSdbCT7ozzI5fmPvSkFYKbwrS/2',null,null,null,null,null,null,null)
  , ('A105','加藤','カトウ','1','$2a$10$pQPVrr/TEy95RvfI8BVG6.uNTbTSdbCT7ozzI5fmPvSkFYKbwrS/2',null,null,null,null,null,null,null)
  , ('A106','岸田','キシダ','1','$2a$10$pQPVrr/TEy95RvfI8BVG6.uNTbTSdbCT7ozzI5fmPvSkFYKbwrS/2',null,null,null,null,null,null,null)
  , ('A107','工藤','クドウ','2','$2a$10$pQPVrr/TEy95RvfI8BVG6.uNTbTSdbCT7ozzI5fmPvSkFYKbwrS/2',null,null,null,null,null,null,null)
  , ('A108','芥子川','ケシガワ','1','$2a$10$pQPVrr/TEy95RvfI8BVG6.uNTbTSdbCT7ozzI5fmPvSkFYKbwrS/2',null,null,null,null,null,null,null)
  , ('A109','小池','コイケ','1','$2a$10$pQPVrr/TEy95RvfI8BVG6.uNTbTSdbCT7ozzI5fmPvSkFYKbwrS/2',null,null,null,null,null,null,null)
  , ('A110','佐藤','サトウ','2','$2a$10$pQPVrr/TEy95RvfI8BVG6.uNTbTSdbCT7ozzI5fmPvSkFYKbwrS/2',null,null,null,null,null,null,null)
  , ('A111','清水','シミズ','1','$2a$10$pQPVrr/TEy95RvfI8BVG6.uNTbTSdbCT7ozzI5fmPvSkFYKbwrS/2',null,null,null,null,null,null,null)
  , ('A112','須藤','ストウ','1','$2a$10$pQPVrr/TEy95RvfI8BVG6.uNTbTSdbCT7ozzI5fmPvSkFYKbwrS/2',null,null,null,null,null,null,null)
  , ('A113','関口','セキグチ','2','$2a$10$pQPVrr/TEy95RvfI8BVG6.uNTbTSdbCT7ozzI5fmPvSkFYKbwrS/2',null,null,null,null,null,null,null)
  , ('A114','園田','ソノダ','1','$2a$10$pQPVrr/TEy95RvfI8BVG6.uNTbTSdbCT7ozzI5fmPvSkFYKbwrS/2',null,null,null,null,null,null,null);

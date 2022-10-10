/* MySQL(ver8.0.27) */

/* dm */
CREATE TABLE `dm` (
  `id` int NOT NULL AUTO_INCREMENT,
  `msg` varchar(200) DEFAULT NULL,
  `send_user` varchar(4) DEFAULT NULL,
  `receive_user` varchar(4) DEFAULT NULL,
  `msg_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);


/* news */
CREATE TABLE `news` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ymd` varchar(8) NOT NULL,
  `category` varchar(1) NOT NULL,
  `title` varchar(20) NOT NULL,
  `content` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
);


/* schedule */
CREATE TABLE `schedule` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ym` varchar(6) DEFAULT NULL,
  `user` varchar(4) DEFAULT NULL,
  `day1` varchar(7) DEFAULT NULL,
  `day2` varchar(7) DEFAULT NULL,
  `day3` varchar(7) DEFAULT NULL,
  `day4` varchar(7) DEFAULT NULL,
  `day5` varchar(7) DEFAULT NULL,
  `day6` varchar(7) DEFAULT NULL,
  `day7` varchar(7) DEFAULT NULL,
  `day8` varchar(7) DEFAULT NULL,
  `day9` varchar(7) DEFAULT NULL,
  `day10` varchar(7) DEFAULT NULL,
  `day11` varchar(7) DEFAULT NULL,
  `day12` varchar(7) DEFAULT NULL,
  `day13` varchar(7) DEFAULT NULL,
  `day14` varchar(7) DEFAULT NULL,
  `day15` varchar(7) DEFAULT NULL,
  `day16` varchar(7) DEFAULT NULL,
  `day17` varchar(7) DEFAULT NULL,
  `day18` varchar(7) DEFAULT NULL,
  `day19` varchar(7) DEFAULT NULL,
  `day20` varchar(7) DEFAULT NULL,
  `day21` varchar(7) DEFAULT NULL,
  `day22` varchar(7) DEFAULT NULL,
  `day23` varchar(7) DEFAULT NULL,
  `day24` varchar(7) DEFAULT NULL,
  `day25` varchar(7) DEFAULT NULL,
  `day26` varchar(7) DEFAULT NULL,
  `day27` varchar(7) DEFAULT NULL,
  `day28` varchar(7) DEFAULT NULL,
  `day29` varchar(7) DEFAULT NULL,
  `day30` varchar(7) DEFAULT NULL,
  `day31` varchar(7) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


/* schedule_pre */
CREATE TABLE `schedule_pre` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ym` varchar(6) DEFAULT NULL,
  `user` varchar(4) DEFAULT NULL,
  `day1` varchar(7) DEFAULT NULL,
  `day2` varchar(7) DEFAULT NULL,
  `day3` varchar(7) DEFAULT NULL,
  `day4` varchar(7) DEFAULT NULL,
  `day5` varchar(7) DEFAULT NULL,
  `day6` varchar(7) DEFAULT NULL,
  `day7` varchar(7) DEFAULT NULL,
  `day8` varchar(7) DEFAULT NULL,
  `day9` varchar(7) DEFAULT NULL,
  `day10` varchar(7) DEFAULT NULL,
  `day11` varchar(7) DEFAULT NULL,
  `day12` varchar(7) DEFAULT NULL,
  `day13` varchar(7) DEFAULT NULL,
  `day14` varchar(7) DEFAULT NULL,
  `day15` varchar(7) DEFAULT NULL,
  `day16` varchar(7) DEFAULT NULL,
  `day17` varchar(7) DEFAULT NULL,
  `day18` varchar(7) DEFAULT NULL,
  `day19` varchar(7) DEFAULT NULL,
  `day20` varchar(7) DEFAULT NULL,
  `day21` varchar(7) DEFAULT NULL,
  `day22` varchar(7) DEFAULT NULL,
  `day23` varchar(7) DEFAULT NULL,
  `day24` varchar(7) DEFAULT NULL,
  `day25` varchar(7) DEFAULT NULL,
  `day26` varchar(7) DEFAULT NULL,
  `day27` varchar(7) DEFAULT NULL,
  `day28` varchar(7) DEFAULT NULL,
  `day29` varchar(7) DEFAULT NULL,
  `day30` varchar(7) DEFAULT NULL,
  `day31` varchar(7) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


/* schedule_time */
CREATE TABLE `schedule_time` (
  `id` int NOT NULL AUTO_INCREMENT,
  `end_ymd` varchar(8) NOT NULL,
  `name1` varchar(20) DEFAULT NULL,
  `start_hm1` varchar(4) DEFAULT NULL,
  `end_hm1` varchar(4) DEFAULT NULL,
  `name2` varchar(20) DEFAULT NULL,
  `start_hm2` varchar(4) DEFAULT NULL,
  `end_hm2` varchar(4) DEFAULT NULL,
  `rest_hm2` varchar(4) DEFAULT NULL,
  `name3` varchar(20) DEFAULT NULL,
  `start_hm3` varchar(4) DEFAULT NULL,
  `end_hm3` varchar(4) DEFAULT NULL,
  `rest_hm3` varchar(4) DEFAULT NULL,
  `name4` varchar(20) DEFAULT NULL,
  `start_hm4` varchar(4) DEFAULT NULL,
  `end_hm4` varchar(4) DEFAULT NULL,
  `rest_hm4` varchar(4) DEFAULT NULL,
  `name5` varchar(20) DEFAULT NULL,
  `start_hm5` varchar(4) DEFAULT NULL,
  `end_hm5` varchar(4) DEFAULT NULL,
  `rest_hm5` varchar(4) DEFAULT NULL,
  `name6` varchar(20) DEFAULT NULL,
  `start_hm6` varchar(4) DEFAULT NULL,
  `end_hm6` varchar(4) DEFAULT NULL,
  `rest_hm6` varchar(4) DEFAULT NULL,
  `name7` varchar(20) DEFAULT NULL,
  `start_hm7` varchar(4) DEFAULT NULL,
  `end_hm7` varchar(4) DEFAULT NULL,
  `rest_hm7` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


/* user */
CREATE TABLE `user` (
  `id` varchar(4) NOT NULL,
  `name` varchar(20) NOT NULL,
  `name_kana` varchar(40) NOT NULL,
  `gender` varchar(1) NOT NULL,
  `password` text NOT NULL,
  `address` varchar(100) DEFAULT NULL,
  `tel` varchar(15) DEFAULT NULL,
  `email` varchar(254) DEFAULT NULL,
  `note` varchar(400) DEFAULT NULL,
  `icon_kbn` varchar(1) DEFAULT NULL,
  `admin_flg` varchar(1) DEFAULT NULL,
  `del_flg` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

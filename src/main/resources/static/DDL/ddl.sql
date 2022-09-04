/* MySQL(ver8.0.27) */

/* dm */
CREATE TABLE `dm` (
  `id` int NOT NULL AUTO_INCREMENT,
  `msg` varchar(200) DEFAULT NULL,
  `send_user` varchar(4) DEFAULT NULL,
  `receive_user` varchar(4) DEFAULT NULL,
  `msg_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`);
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
  `ymd` varchar(8) NOT NULL,
  `user1` varchar(4) DEFAULT NULL,
  `memo1` varchar(100) DEFAULT NULL,
  `user2` varchar(4) DEFAULT NULL,
  `memo2` varchar(100) DEFAULT NULL,
  `user3` varchar(4) DEFAULT NULL,
  `memo3` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ymd`);
);


/* schedule_pre (仮) */
CREATE TABLE `schedule_pre` (
`id` int NOT NULL AUTO_INCREMENT,
`ym` varchar(6) DEFAULT NULL,
`user` varchar(4) DEFAULT NULL,
`1` varchar(3) DEFAULT NULL,
`2` varchar(3) DEFAULT NULL,
`3` varchar(3) DEFAULT NULL,
`4` varchar(3) DEFAULT NULL,
`5` varchar(3) DEFAULT NULL,
`6` varchar(3) DEFAULT NULL,
`7` varchar(3) DEFAULT NULL,
`8` varchar(3) DEFAULT NULL,
`9` varchar(3) DEFAULT NULL,
`10` varchar(3) DEFAULT NULL,
`11` varchar(3) DEFAULT NULL,
`12` varchar(3) DEFAULT NULL,
`13` varchar(3) DEFAULT NULL,
`14` varchar(3) DEFAULT NULL,
`15` varchar(3) DEFAULT NULL,
`16` varchar(3) DEFAULT NULL,
`17` varchar(3) DEFAULT NULL,
`18` varchar(3) DEFAULT NULL,
`19` varchar(3) DEFAULT NULL,
`20` varchar(3) DEFAULT NULL,
`21` varchar(3) DEFAULT NULL,
`22` varchar(3) DEFAULT NULL,
`23` varchar(3) DEFAULT NULL,
`24` varchar(3) DEFAULT NULL,
`25` varchar(3) DEFAULT NULL,
`26` varchar(3) DEFAULT NULL,
`27` varchar(3) DEFAULT NULL,
`28` varchar(3) DEFAULT NULL,
`29` varchar(3) DEFAULT NULL,
`30` varchar(3) DEFAULT NULL,
`31` varchar(3) DEFAULT NULL,
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
  `email` varchar(20) DEFAULT NULL,
  `note` varchar(400) DEFAULT NULL,
  `admin_flg` varchar(1) DEFAULT NULL,
  `del_flg` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`);
);

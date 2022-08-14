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


/* user */
CREATE TABLE `user` (
  `id` varchar(4) NOT NULL,
  `name` varchar(20) NOT NULL,
  `name_kana` varchar(40) DEFAULT NULL,
  `gender` varchar(1) DEFAULT NULL,
  `password` text,
  `address` varchar(100) DEFAULT NULL,
  `tel` varchar(15) DEFAULT NULL,
  `email` varchar(20) DEFAULT NULL,
  `note` varchar(400) DEFAULT NULL,
  `admin_flg` varchar(1) DEFAULT NULL,
  `del_flg` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`);
);

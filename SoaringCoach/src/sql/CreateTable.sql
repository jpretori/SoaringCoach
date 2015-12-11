DROP TABLE IF EXISTS `igc_b_record`;

CREATE TABLE `igc_b_record` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `file_id` varchar(30) DEFAULT NULL,
  `timestamp` varchar(6) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `altitude_ok` char(1) DEFAULT NULL,
  `pressure_altitude` int(11) DEFAULT NULL,
  `gnss_altitude` int(11) DEFAULT NULL,
  `other` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `file_id` (`file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
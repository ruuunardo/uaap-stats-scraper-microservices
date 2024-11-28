SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE IF NOT EXISTS `users` (
  `username` varchar(256) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `active` boolean DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

CREATE TABLE IF NOT EXISTS `roles` (
  `username` varchar(256) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  UNIQUE KEY `username` (`username`,`role`),
  CONSTRAINT `fk_user` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

SET FOREIGN_KEY_CHECKS=1;
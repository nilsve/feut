CREATE TABLE `adres` (
  `adres_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `woonplaats` varchar(250) DEFAULT NULL,
  `straat` varchar(250) DEFAULT NULL,
  `huisnummer` int(11) NOT NULL,
  `toevoeging` varchar(20) DEFAULT NULL,
  `postcode` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`adres_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `huis` (
  `huis_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `adres_id` int(11) unsigned NOT NULL,
  `naam` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`huis_id`),
  CONSTRAINT FOREIGN KEY (`adres_id`) REFERENCES `adres` (`adres_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `gebruiker` (
  `gebruiker_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(250) DEFAULT NULL,
  `voornaam` varchar(250) DEFAULT NULL,
  `achternaam` varchar(250) DEFAULT NULL,
  `password` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`gebruiker_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `huis_gebruiker` (
  `huis_id` int(11) unsigned NOT NULL,
  `gebruiker_id` int(11) unsigned NOT NULL,
  `beheerder` TINYINT(1) unsigned NOT NULL DEFAULT 0,
  `aanwezig` TINYINT(1) unsigned NOT NULL DEFAULT 0,
  `online` TINYINT(1) unsigned NOT NULL DEFAULT 0,
  PRIMARY KEY (`huis_id`, `gebruiker_id`),
  CONSTRAINT FOREIGN KEY (`huis_id`) REFERENCES `huis` (`huis_id`),
  CONSTRAINT FOREIGN KEY (`gebruiker_id`) REFERENCES `gebruiker` (`gebruiker_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `chat_message` (
  `chat_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `huis_id` int(11) unsigned NOT NULL,
  `gebruiker_id` int(11) unsigned NOT NULL,
  `bericht` varchar(250) DEFAULT NULL,
  `datum` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`chat_id`),
  CONSTRAINT FOREIGN KEY (`huis_id`) REFERENCES `huis` (`huis_id`),
  CONSTRAINT FOREIGN KEY (`gebruiker_id`) REFERENCES `gebruiker` (`gebruiker_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `product` (
  `product_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `huis_id` int(11) unsigned NOT NULL,
  `gekocht_door_id` int(11) NOT NULL,
  `aantal` int(11) NOT NULL,
  `gekocht` int(11) NOT NULL,
  `datum_aanvraag` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `datum_gekocht` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `beschrijving` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  CONSTRAINT FOREIGN KEY (`huis_id`) REFERENCES `huis` (`huis_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
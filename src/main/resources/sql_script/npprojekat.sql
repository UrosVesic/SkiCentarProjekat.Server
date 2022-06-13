/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.18 : Database - npprojekat
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`npprojekat` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `npprojekat`;

/*Table structure for table `korisnik` */

DROP TABLE IF EXISTS `korisnik`;

CREATE TABLE `korisnik` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `sifra` varchar(255) DEFAULT NULL,
  `ime` varchar(255) DEFAULT NULL,
  `prezime` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `korisnik` */

insert  into `korisnik`(`id`,`email`,`sifra`,`ime`,`prezime`) values 
(1,'a@a','aaa','aaaa','aaa'),
(2,'mika@mika','mika123','Mika','Mikic'),
(3,'peraperic@gmail.com','peraperic','Pera','Perić'),
(4,'urosvesic@gmail.com','urosvesic','Uroš','Vesić'),
(5,'zika@zika.com','123','zika','zikic');

/*Table structure for table `kupac` */

DROP TABLE IF EXISTS `kupac`;

CREATE TABLE `kupac` (
  `idKupca` bigint(20) NOT NULL AUTO_INCREMENT,
  `brojLK` varchar(10) DEFAULT NULL,
  `Ime` varchar(30) DEFAULT NULL,
  `Prezime` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`idKupca`),
  UNIQUE KEY `brojLK` (`brojLK`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `kupac` */

insert  into `kupac`(`idKupca`,`brojLK`,`Ime`,`Prezime`) values 
(1,'123456','Uros','Vesic'),
(2,'123457','Pera','Peric'),
(3,'12333','Uros','Vesic'),
(4,'123333','Zika','Mikic');

/*Table structure for table `skicentar` */

DROP TABLE IF EXISTS `skicentar`;

CREATE TABLE `skicentar` (
  `SifraSkiCentra` bigint(20) NOT NULL AUTO_INCREMENT,
  `NazivSkiCentra` varchar(50) DEFAULT NULL,
  `NazivPlanine` varchar(50) DEFAULT NULL,
  `RadnoVreme` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`SifraSkiCentra`),
  UNIQUE KEY `NazivSkiCentra` (`NazivSkiCentra`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `skicentar` */

insert  into `skicentar`(`SifraSkiCentra`,`NazivSkiCentra`,`NazivPlanine`,`RadnoVreme`) values 
(1,'SC Kopaonik','Kopaonik','09-17:30'),
(2,'Tornik','Zlatibor','09-16:30'),
(3,'OC Jahorina','Jahorina','09-16'),
(4,'Divcibare','Divcibare','09-16'),
(5,'Stara Planina','Stara Planina','09-16'),
(6,'SC1','PL1','00-24'),
(7,'SC2','PL2','09:30-16:30'),
(9,'SC3','PL3','12-17'),
(12,'SCNovi2','Nova','09-16');

/*Table structure for table `skikarta` */

DROP TABLE IF EXISTS `skikarta`;

CREATE TABLE `skikarta` (
  `SifraSkiKarte` bigint(20) NOT NULL AUTO_INCREMENT,
  `VrstaSkiKarte` enum('JEDNODNEVNA','DVODNEVNA','TRODNEVNA','CETVORODNEVNA','PETODNEVNA','SESTODNEVNA','SEDMODNEVNA') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `CenaSkiKarte` decimal(10,0) DEFAULT NULL,
  `SifraSkiCentra` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`SifraSkiKarte`),
  UNIQUE KEY `vrstaSkiKarte-skiCentar` (`VrstaSkiKarte`,`SifraSkiCentra`),
  KEY `SifraSkiCentra` (`SifraSkiCentra`),
  CONSTRAINT `skikarta_ibfk_1` FOREIGN KEY (`SifraSkiCentra`) REFERENCES `skicentar` (`SifraSkiCentra`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `skikarta` */

insert  into `skikarta`(`SifraSkiKarte`,`VrstaSkiKarte`,`CenaSkiKarte`,`SifraSkiCentra`) values 
(3,'JEDNODNEVNA',2600,1),
(4,'DVODNEVNA',4900,1),
(5,'TRODNEVNA',3400,1),
(7,'SEDMODNEVNA',4000,4),
(9,'TRODNEVNA',4000,4),
(11,'TRODNEVNA',5000,9),
(12,'DVODNEVNA',1200,9),
(14,'JEDNODNEVNA',1234,5);

/*Table structure for table `skipas` */

DROP TABLE IF EXISTS `skipas`;

CREATE TABLE `skipas` (
  `SifraSkiPasa` bigint(20) NOT NULL AUTO_INCREMENT,
  `UkupnaCena` decimal(20,0) DEFAULT NULL,
  `idKupca` bigint(50) DEFAULT NULL,
  `DatumIzdavanja` date DEFAULT NULL,
  `sezona` varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`SifraSkiPasa`),
  UNIQUE KEY `kupac-sezona` (`idKupca`,`sezona`),
  CONSTRAINT `skipas_ibfk_1` FOREIGN KEY (`idKupca`) REFERENCES `kupac` (`idKupca`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `skipas` */

insert  into `skipas`(`SifraSkiPasa`,`UkupnaCena`,`idKupca`,`DatumIzdavanja`,`sezona`) values 
(3,2600,1,'2015-05-03','2014/2015'),
(4,2600,2,'2022-05-07','2021/2022'),
(5,11000,1,'2022-05-11','2021/2022'),
(13,5200,4,'2018-06-09','2017/2018'),
(14,5200,4,'2021-06-05','2020/2021');

/*Table structure for table `stavkaskipasa` */

DROP TABLE IF EXISTS `stavkaskipasa`;

CREATE TABLE `stavkaskipasa` (
  `SifraSkiPasa` bigint(20) NOT NULL,
  `RB` bigint(20) NOT NULL,
  `VrednostStavke` decimal(10,0) DEFAULT NULL,
  `PocetakVazenja` date DEFAULT NULL,
  `ZavrsetakVazenja` date DEFAULT NULL,
  `SifraSkiKarte` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`SifraSkiPasa`,`RB`),
  KEY `SifraSkiKarte` (`SifraSkiKarte`),
  KEY `RB` (`RB`),
  CONSTRAINT `stavkaskipasa_ibfk_1` FOREIGN KEY (`SifraSkiKarte`) REFERENCES `skikarta` (`SifraSkiKarte`),
  CONSTRAINT `stavkaskipasa_ibfk_2` FOREIGN KEY (`SifraSkiPasa`) REFERENCES `skipas` (`SifraSkiPasa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `stavkaskipasa` */

insert  into `stavkaskipasa`(`SifraSkiPasa`,`RB`,`VrednostStavke`,`PocetakVazenja`,`ZavrsetakVazenja`,`SifraSkiKarte`) values 
(3,1,2600,'2022-05-06','2022-05-07',3),
(4,2,2600,'2022-05-07','2022-05-08',3),
(5,3,4900,'2022-05-12','2022-05-14',4),
(5,4,4900,'2022-06-15','2022-06-17',4),
(5,5,1200,'2022-06-25','2022-06-27',12),
(13,1,2600,'2018-06-09','2018-06-10',3),
(13,2,2600,'2018-06-16','2018-06-17',3),
(14,1,2600,'2021-06-05','2021-06-06',3),
(14,2,2600,'2021-06-19','2021-06-20',3);

/*Table structure for table `staza` */

DROP TABLE IF EXISTS `staza`;

CREATE TABLE `staza` (
  `idStaze` bigint(20) NOT NULL AUTO_INCREMENT,
  `BrojStaze` varchar(5) DEFAULT NULL,
  `NazivStaze` varchar(50) DEFAULT NULL,
  `TipStaze` varchar(50) DEFAULT NULL,
  `SifraSkiCentra` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idStaze`),
  UNIQUE KEY `BrojStaze` (`BrojStaze`,`SifraSkiCentra`),
  KEY `SifraSkiCentra` (`SifraSkiCentra`),
  CONSTRAINT `staza_ibfk_1` FOREIGN KEY (`SifraSkiCentra`) REFERENCES `skicentar` (`SifraSkiCentra`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `staza` */

insert  into `staza`(`idStaze`,`BrojStaze`,`NazivStaze`,`TipStaze`,`SifraSkiCentra`) values 
(15,'3a','Krst','Laka',1),
(16,'3b','Krst','Laka',1),
(17,'3c','Krst','Laka',1),
(18,'4','Pancicev vrh','Srednja',1),
(19,'6','Crvena duboka','Srednja',1),
(20,'2a','Tornik','Srednja',2),
(21,'2','Tornik','Srednja',2);

/*Table structure for table `zicara` */

DROP TABLE IF EXISTS `zicara`;

CREATE TABLE `zicara` (
  `SifraZicare` bigint(20) NOT NULL AUTO_INCREMENT,
  `NazivZicare` varchar(50) DEFAULT NULL,
  `RadnoVreme` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Kapacitet` int(11) DEFAULT NULL,
  `UFunkciji` tinyint(1) DEFAULT NULL,
  `SifraSkiCentra` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`SifraZicare`),
  KEY `SifraSkiCentra` (`SifraSkiCentra`),
  CONSTRAINT `zicara_ibfk_1` FOREIGN KEY (`SifraSkiCentra`) REFERENCES `skicentar` (`SifraSkiCentra`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `zicara` */

insert  into `zicara`(`SifraZicare`,`NazivZicare`,`RadnoVreme`,`Kapacitet`,`UFunkciji`,`SifraSkiCentra`) values 
(7,'Tornik','10-16',1400,1,2),
(8,'Krst','9-16',1500,1,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

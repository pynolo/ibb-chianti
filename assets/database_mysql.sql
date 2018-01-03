CREATE DATABASE  IF NOT EXISTS `ibb_2018` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ibb_2018`;
-- MySQL dump 10.13  Distrib 5.5.34, for debian-linux-gnu (x86_64)
--
-- ------------------------------------------------------
-- Server version	5.1.49-3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `config`
--

DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config` (
	`id` varchar(32) NOT NULL,
	`val` varchar(256) NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
insert into config (id,val) values ('basePassword','x');
insert into config (id,val) values ('adminPassword','x');
insert into config (id,val) values ('stripeSecretKey','x');
insert into config (id,val) values ('stripePublicKey','x');

insert into config (id,val) values ('paypalThankyouUrl','https://join.burningboots.it/greeter#thankyou');
insert into config (id,val) values ('paypalLogoImgUrl','https://join.burningboots.it/images/ibb_title_purple_189.png');
insert into config (id,val) values ('paypalIpnUrl','https://join.burningboots.it/greeter/ipn');
insert into config (id,val) values ('paypalPaymentUrl','x');
insert into config (id,val) values ('paypalAccount','x');
insert into config (id,val) values ('paypalPaymentUrl_prod','https://www.paypal.com/cgi-bin/webscr');
insert into config (id,val) values ('paypalAccount_prod','itburningboots@gmail.com');
insert into config (id,val) values ('paypalPaymentUrl_dev','https://www.sandbox.paypal.com/cgi-bin/webscr');
insert into config (id,val) values ('paypalAccount_dev','itburningboots-facilitator@gmail.com');

--
-- Table structure for table `level`
--

DROP TABLE IF EXISTS `level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `level` (
	`id` int(11) NOT NULL,
	`name` varchar(64) DEFAULT NULL,
	`price` decimal(9,2) NOT NULL DEFAULT '0.00',
	`last_date` date NOT NULL,
	`last_count` int(11) NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `level` (`id`, `name`, `price`, `last_date`, `last_count`) VALUES ('1', 'Low income', '30', '2018-01-05', '3');
INSERT INTO `level` (`id`, `name`, `price`, `last_date`, `last_count`) VALUES ('2', 'Level 1', '50', '2018-01-10', '8');
INSERT INTO `level` (`id`, `name`, `price`, `last_date`, `last_count`) VALUES ('3', 'Level 2', '60', '2018-01-15', '15');
INSERT INTO `level` (`id`, `name`, `price`, `last_date`, `last_count`) VALUES ('4', 'Level 3', '70', '2018-02-01', '20');

--
-- Table structure for table `participant`
--

DROP TABLE IF EXISTS `participant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participant` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`creation_dt` datetime NOT NULL,
	`update_dt` datetime NOT NULL,
	`item_number` varchar(64) NOT NULL,
	`email` varchar(64) DEFAULT NULL,
	`first_name` varchar(64) DEFAULT NULL,
	`last_name` varchar(64) DEFAULT NULL,
	`birth_city` varchar(128) DEFAULT NULL,
	`birth_dt` varchar(64) DEFAULT NULL,
	`payment_amount` varchar(64) DEFAULT NULL,
	`payment_details` text DEFAULT NULL,
	`payment_dt` datetime DEFAULT NULL,
	`replaced_by_id` int(11) DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `web_session`
--

DROP TABLE IF EXISTS `web_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `web_session` (
	`id` varchar(64) NOT NULL,
	`creation_dt` datetime NOT NULL,
	`heartbeat_dt` datetime NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `ipn_response`
--

DROP TABLE IF EXISTS `ipn_response`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ipn_response` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`id_participant` int(11) DEFAULT NULL,
	`item_number` varchar(64) DEFAULT NULL,
	`payment_status` varchar(64) DEFAULT NULL,
	`payer_email` varchar(64) DEFAULT NULL,
	`mc_gross` varchar(64) DEFAULT NULL,
	`mc_currency` varchar(64) DEFAULT NULL,
	`payment_date` varchar(128) DEFAULT NULL,
	`pending_reason` varchar(64) DEFAULT NULL,
	`payment_type` varchar(64) DEFAULT NULL,
	PRIMARY KEY (`id`),
	KEY `unique_item_number` (`item_number`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


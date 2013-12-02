-- phpMyAdmin SQL Dump
-- version 4.0.4.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Dec 02, 2013 at 05:57 PM
-- Server version: 5.6.11
-- PHP Version: 5.5.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `wifil`
--
CREATE DATABASE IF NOT EXISTS `wifil` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `wifil`;

-- --------------------------------------------------------

--
-- Table structure for table `editlist`
--

CREATE TABLE IF NOT EXISTS `editlist` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `UserGUID` varchar(50) NOT NULL,
  `HotspotID` int(11) NOT NULL,
  `lat` decimal(10,4) NOT NULL,
  `lon` decimal(10,4) NOT NULL,
  `ChangeData` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `HotspotID` (`HotspotID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=55 ;

--
-- Dumping data for table `editlist`
--

INSERT INTO `editlist` (`ID`, `UserGUID`, `HotspotID`, `lat`, `lon`, `ChangeData`) VALUES
(34, '1234', 10, '40.4000', '-86.9000', NULL),
(35, '1234', 11, '40.4250', '-86.9238', NULL),
(36, '1234', 12, '40.4251', '-86.9239', NULL),
(37, '1234', 10, '40.4000', '-86.9000', NULL),
(38, '1234', 11, '40.4250', '-86.9238', NULL),
(39, '1234', 12, '40.4251', '-86.9239', NULL),
(40, '1234', 10, '40.4000', '-86.9000', NULL),
(41, '1234', 11, '40.4250', '-86.9238', NULL),
(42, '1234', 12, '40.4251', '-86.9239', NULL),
(43, '1234', 10, '40.4003', '-86.9000', NULL),
(44, '1234', 11, '40.4250', '-86.9238', NULL),
(45, '1234', 12, '40.4251', '-86.9239', NULL),
(46, '1234', 10, '40.4003', '-86.9000', NULL),
(47, '1234', 11, '40.4250', '-86.9238', NULL),
(48, '1234', 12, '40.4251', '-86.9239', NULL),
(49, '1234', 10, '40.4003', '-86.9000', NULL),
(50, '1234', 11, '40.4250', '-86.9238', NULL),
(51, '1234', 12, '40.4251', '-86.9239', NULL),
(52, '1234', 10, '40.4001', '-86.9000', NULL),
(53, '1234', 11, '40.4250', '-86.9238', NULL),
(54, '1234', 12, '40.4251', '-86.9239', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `hotspotlist`
--

CREATE TABLE IF NOT EXISTS `hotspotlist` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SSID` varchar(32) NOT NULL,
  `MAC` varchar(17) NOT NULL,
  `lat` decimal(10,4) NOT NULL,
  `lon` decimal(10,4) NOT NULL,
  `radius` int(11) NOT NULL,
  `count` int(10) NOT NULL DEFAULT '1',
  `meta` varchar(1000) DEFAULT NULL,
  `geohash` int(11) DEFAULT NULL,
  PRIMARY KEY (`MAC`),
  UNIQUE KEY `ID` (`ID`),
  KEY `lat` (`lat`),
  KEY `lon` (`lon`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=13 ;

--
-- Dumping data for table `hotspotlist`
--

INSERT INTO `hotspotlist` (`ID`, `SSID`, `MAC`, `lat`, `lon`, `radius`, `count`, `meta`, `geohash`) VALUES
(10, 'New Hotspot', 'AB:CD:EF:AB:CD:EF', '40.4001', '-86.9000', 22, 8, NULL, 31739462),
(11, 'PAL3.0', 'AA:AA:AA:AA:AA', '40.4250', '-86.9238', 20, 8, NULL, 31739461),
(12, 'King', '00:1E:2A:6E:FB:A5', '40.4251', '-86.9239', 20, 8, NULL, 31739461),
(6, 'PANERA', '00:24:a8:b3:b5:00', '39.2207', '-85.8877', 30, 1, NULL, 31566682);

--
-- Triggers `hotspotlist`
--
DROP TRIGGER IF EXISTS `set geohash`;
DELIMITER //
CREATE TRIGGER `set geohash` BEFORE INSERT ON `hotspotlist`
 FOR EACH ROW SET NEW.geohash = (FLOOR((NEW.lat+180)/.05))*7200+FLOOR((NEW.lon+180)/.05)
//
DELIMITER ;
DROP TRIGGER IF EXISTS `update geohash`;
DELIMITER //
CREATE TRIGGER `update geohash` BEFORE UPDATE ON `hotspotlist`
 FOR EACH ROW SET NEW.geohash = (FLOOR((NEW.lat+180)/.05))*7200+FLOOR((NEW.lon+180)/.05)
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `userlist`
--

CREATE TABLE IF NOT EXISTS `userlist` (
  `GUID` varchar(50) NOT NULL,
  `Permissions` int(11) NOT NULL,
  PRIMARY KEY (`GUID`),
  UNIQUE KEY `GUID` (`GUID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `userlist`
--

INSERT INTO `userlist` (`GUID`, `Permissions`) VALUES
('0', 111);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

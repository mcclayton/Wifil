-- phpMyAdmin SQL Dump
-- version 4.0.4.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Oct 28, 2013 at 04:41 AM
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
  `UserGUID` varchar(50) NOT NULL,
  `HotspotID` int(11) NOT NULL,
  `ChangeData` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  `meta` varchar(1000) NOT NULL,
  `geohash` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `hotspotlist`
--

INSERT INTO `hotspotlist` (`ID`, `SSID`, `MAC`, `lat`, `lon`, `radius`, `meta`, `geohash`) VALUES
(1, 'King', '00:1E:2A:6E:FB:A5', '40.4251', '-86.9239', 30, 'desc:"The Best Hotspot"\r\nrat:5', 31739461);

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

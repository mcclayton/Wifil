-- phpMyAdmin SQL Dump
-- version 4.0.4.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Dec 04, 2013 at 02:15 PM
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
  `lat` decimal(10,6) NOT NULL,
  `lon` decimal(10,6) NOT NULL,
  `ChangeData` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `HotspotID` (`HotspotID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=206 ;

-- --------------------------------------------------------

--
-- Table structure for table `hotspotlist`
--

CREATE TABLE IF NOT EXISTS `hotspotlist` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SSID` varchar(32) NOT NULL,
  `MAC` varchar(17) NOT NULL,
  `isPublic` tinyint(1) NOT NULL DEFAULT '1',
  `lat` decimal(10,6) NOT NULL,
  `lon` decimal(10,6) NOT NULL,
  `radius` int(11) NOT NULL,
  `count` int(10) NOT NULL DEFAULT '1',
  `meta` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`MAC`),
  UNIQUE KEY `ID` (`ID`),
  KEY `lat` (`lat`),
  KEY `lon` (`lon`),
  KEY `coords` (`lat`,`lon`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=151 ;

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

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- phpMyAdmin SQL Dump
-- version 4.2.12deb2+deb8u2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 30. Jul 2017 um 11:37
-- Server Version: 5.5.55-0+deb8u1
-- PHP-Version: 5.6.30-0+deb8u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `KAPPA`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Benutzer`
--
-- Erstellt am: 15. Jul 2017 um 18:49
--

CREATE TABLE IF NOT EXISTS `Benutzer` (
  `EMail` varchar(100) NOT NULL COMMENT 'Gleichzeitig Datenbankname!!!',
  `Passwort` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `Benutzer`
--

INSERT INTO `Benutzer` (`EMail`, `Passwort`) VALUES
('admin@worldofjarcraft.ddns.net', '1234'),
('eric', 'abc'),
('eric.ackermann.99@googlemail.com', '12'),
('eric.ackermann.alles@googlemail.com', 'Essen'),
('KerstinAckermannMail@web.de', 'ACKI');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Einkauf`
--
-- Erstellt am: 15. Jul 2017 um 18:49
--

CREATE TABLE IF NOT EXISTS `Einkauf` (
`Num` int(11) NOT NULL,
  `Lebensmittel` text NOT NULL,
  `Benutzer` varchar(100) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=latin1;

--
-- RELATIONEN DER TABELLE `Einkauf`:
--   `Benutzer`
--       `Benutzer` -> `EMail`
--

--
-- Daten für Tabelle `Einkauf`
--

INSERT INTO `Einkauf` (`Num`, `Lebensmittel`, `Benutzer`) VALUES
(7, 'verdammt viel kaltes, totes FLEISCH IHR PANSEN', 'eric.ackermann.99@googlemail.com'),
(48, 'Soja Sauce', 'admin@worldofjarcraft.ddns.net'),
(51, 'Eier', 'admin@worldofjarcraft.ddns.net'),
(52, 'Milch', 'admin@worldofjarcraft.ddns.net'),
(53, 'Butter', 'admin@worldofjarcraft.ddns.net'),
(54, 'Kuerbiscreme', 'admin@worldofjarcraft.ddns.net'),
(57, 'Suesssskartoffeln', 'admin@worldofjarcraft.ddns.net'),
(58, 'Essen', 'eric'),
(60, 'noch mehr Essen', 'eric');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Fach`
--
-- Erstellt am: 15. Jul 2017 um 18:49
--

CREATE TABLE IF NOT EXISTS `Fach` (
`lNummer` int(11) NOT NULL,
  `Kuehlschrank` int(11) DEFAULT NULL,
  `Name` varchar(100) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;

--
-- RELATIONEN DER TABELLE `Fach`:
--   `Kuehlschrank`
--       `Kuehlschrank` -> `laufNummer`
--

--
-- Daten für Tabelle `Fach`
--

INSERT INTO `Fach` (`lNummer`, `Kuehlschrank`, `Name`) VALUES
(8, 18, '7'),
(9, 18, '1'),
(12, 25, 'Fach 1'),
(13, 18, '2'),
(16, 28, 'Fach 1'),
(17, 28, 'Fach 2'),
(18, 25, 'Fach 2'),
(19, 25, 'Fach 3'),
(20, 30, 'Fach 1'),
(21, 31, 'Fach 1'),
(22, 31, 'Fach 2');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Kuehlschrank`
--
-- Erstellt am: 15. Jul 2017 um 18:49
--

CREATE TABLE IF NOT EXISTS `Kuehlschrank` (
`laufNummer` int(11) NOT NULL,
  `Name` text NOT NULL,
  `Zahl_Faecher` int(11) NOT NULL,
  `Besitzer` varchar(100) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1;

--
-- RELATIONEN DER TABELLE `Kuehlschrank`:
--   `Besitzer`
--       `Benutzer` -> `EMail`
--

--
-- Daten für Tabelle `Kuehlschrank`
--

INSERT INTO `Kuehlschrank` (`laufNummer`, `Name`, `Zahl_Faecher`, `Besitzer`) VALUES
(18, 'Kueche', 7, 'admin@worldofjarcraft.ddns.net'),
(19, 'Kueche', 7, 'eric.ackermann.99@googlemail.com'),
(25, 'Kuehltruhe', 0, 'admin@worldofjarcraft.ddns.net'),
(28, 'Scheck', 0, 'eric.ackermann.alles@googlemail.com'),
(29, 'Ecken', 0, 'eric.ackermann.alles@googlemail.com'),
(30, 'Schrank', 0, 'eric'),
(31, 'Kuehltruhe', 0, 'KerstinAckermannMail@web.de');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Lebensmittel`
--
-- Erstellt am: 30. Jul 2017 um 08:47
--

CREATE TABLE IF NOT EXISTS `Lebensmittel` (
`Nummer` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Anzahl` varchar(100) NOT NULL COMMENT 'Wie oft das Lebensmittel da ist.',
  `Haltbarkeitsdatum` bigint(20) NOT NULL COMMENT 'Angabe als Java-Timestamp.',
  `Fach` int(11) DEFAULT NULL,
  `Besitzer` varchar(100) DEFAULT NULL,
  `eingelagert` bigint(20) unsigned NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;

--
-- RELATIONEN DER TABELLE `Lebensmittel`:
--   `Fach`
--       `Fach` -> `lNummer`
--   `Besitzer`
--       `Benutzer` -> `EMail`
--

--
-- Daten für Tabelle `Lebensmittel`
--

INSERT INTO `Lebensmittel` (`Nummer`, `Name`, `Anzahl`, `Haltbarkeitsdatum`, `Fach`, `Besitzer`, `eingelagert`) VALUES
(6, 'massig viel Fleisch', '999', 123466789987654321, 8, 'admin@worldofjarcraft.ddns.net', 0),
(7, 'noch mehr Fleisch', '999', 123466789987654321, 8, 'admin@worldofjarcraft.ddns.net', 0),
(8, 'EkelgemÃ¼se', '999', 123466789987654321, 8, 'admin@worldofjarcraft.ddns.net', 0),
(13, 'RANZ', '999', 123466789987654321, 8, 'admin@worldofjarcraft.ddns.net', 0),
(15, 'RANZ', '999', 123466789987654321, 8, 'admin@worldofjarcraft.ddns.net', 0),
(16, 'Fleisch', '5555', 1501601367493, 8, 'admin@worldofjarcraft.ddns.net', 0),
(18, 'Essen', '5', 0, 8, 'admin@worldofjarcraft.ddns.net', 0),
(23, 'Lecker Gemüsepfanne', '5', 1500829000957, 8, 'admin@worldofjarcraft.ddns.net', 0),
(24, 'Essen', '9', 0, 8, 'admin@worldofjarcraft.ddns.net', 0),
(25, 'Futter', '6', 0, 12, 'admin@worldofjarcraft.ddns.net', 0),
(26, 'Fett was zu frasen', '9', 1500821697731, 8, 'admin@worldofjarcraft.ddns.net', 0),
(28, 'Fett Was Zu Frasen', '9', 1501081974912, 13, 'admin@worldofjarcraft.ddns.net', 0),
(29, 'Fett Was Zu Frasen', '9', 1501082020564, 13, 'admin@worldofjarcraft.ddns.net', 0),
(30, 'Für', '6', 1501082217779, 13, 'admin@worldofjarcraft.ddns.net', 0),
(34, 'Essen', '0', 0, 17, 'eric.ackermann.alles@googlemail.com', 0),
(35, 'Essen', '9', 1501000353298, 17, 'eric.ackermann.alles@googlemail.com', 0),
(36, 'Beff Steck von Oma', '5', 1501352137535, 8, 'admin@worldofjarcraft.ddns.net', 0),
(37, 'Ross Beff', '8', 1503252982153, 8, 'admin@worldofjarcraft.ddns.net', 0),
(39, 'Essen', '0', 0, 13, 'admin@worldofjarcraft.ddns.net', 0),
(40, 'Essen', '7', 1499783495265, 12, 'admin@worldofjarcraft.ddns.net', 0),
(41, 'Gulasch Von Oma', '1', 0, 8, 'admin@worldofjarcraft.ddns.net', 0),
(43, 'Jameson', '1', 1501232052259, 12, 'admin@worldofjarcraft.ddns.net', 0),
(44, 'Gemuesesuppe', '1', 1543751825504, 8, 'admin@worldofjarcraft.ddns.net', 0),
(45, 'Schokoeis', '12', 1503745176058, 18, 'admin@worldofjarcraft.ddns.net', 0),
(46, 'Eierspeise', '7', 1501767810864, 8, 'admin@worldofjarcraft.ddns.net', 0),
(47, 'Sahne', '6', 1556977523487, 8, 'admin@worldofjarcraft.ddns.net', 0),
(48, 'Kopfsalat', '1', 0, 9, 'admin@worldofjarcraft.ddns.net', 0),
(49, 'fettarme Butter', '2', 1501595197938, 9, 'admin@worldofjarcraft.ddns.net', 0),
(51, 'Broetchen', '8', 1501854825194, 9, 'admin@worldofjarcraft.ddns.net', 0),
(52, 'Minestrone', '1', 1502118490101, 8, 'admin@worldofjarcraft.ddns.net', 0),
(53, 'Essen', '0', 1500738580015, 8, 'admin@worldofjarcraft.ddns.net', 0),
(54, 'Sahneeis', '2', 1509214655612, 19, 'admin@worldofjarcraft.ddns.net', 0),
(55, 'selbstgemachte Sahnesosssse', '5', 1504210681518, 8, 'admin@worldofjarcraft.ddns.net', 0),
(56, 'selbstgemafhtes Gulasch', '5', 0, 8, 'admin@worldofjarcraft.ddns.net', 1501345577090),
(57, 'Essen', '1', 1501620761345, 20, 'eric', 1501361561346),
(58, 'Mehr Essen', '0', 0, 20, 'eric', 1501361571394),
(59, 'Erbsen', '1', 1532954643609, 12, 'KerstinAckermannMail@web.de', 1501418643619),
(60, 'Roster gebraten', '2', 0, 12, 'KerstinAckermannMail@web.de', 1501418754745),
(61, 'Roster gebraten', '2', 0, 12, 'KerstinAckermannMail@web.de', 1501418793207),
(63, 'Erbsen', '1 grosssse Tuete', 1532955609769, 12, 'KerstinAckermannMail@web.de', 1501419609774),
(64, 'Erbsen', '1 grosssse Tuete', 0, 12, 'KerstinAckermannMail@web.de', 1501419629228),
(65, 'Erbsen', '1 grosssse Tuete', 1501506089389, 12, 'KerstinAckermannMail@web.de', 1501419689391),
(71, 'Erbsen', '1 grosssse Tuete', 1501853649458, 22, 'KerstinAckermannMail@web.de', 1501421649461),
(72, 'Erbsen', '1 Tuete', 1532957671209, 21, 'KerstinAckermannMail@web.de', 1501421671210);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `resetPW`
--
-- Erstellt am: 15. Jul 2017 um 18:49
--

CREATE TABLE IF NOT EXISTS `resetPW` (
`OrderID` int(11) NOT NULL,
  `Benutzer` varchar(100) DEFAULT NULL,
  `Code` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- RELATIONEN DER TABELLE `resetPW`:
--   `Benutzer`
--       `Benutzer` -> `EMail`
--

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `Benutzer`
--
ALTER TABLE `Benutzer`
 ADD PRIMARY KEY (`EMail`), ADD KEY `E-Mail` (`EMail`);

--
-- Indizes für die Tabelle `Einkauf`
--
ALTER TABLE `Einkauf`
 ADD PRIMARY KEY (`Num`), ADD KEY `Einkauf_ibfk_1` (`Benutzer`);

--
-- Indizes für die Tabelle `Fach`
--
ALTER TABLE `Fach`
 ADD PRIMARY KEY (`lNummer`), ADD KEY `Fach_ibfk_1` (`Kuehlschrank`);

--
-- Indizes für die Tabelle `Kuehlschrank`
--
ALTER TABLE `Kuehlschrank`
 ADD PRIMARY KEY (`laufNummer`), ADD KEY `Besitzer` (`Besitzer`);

--
-- Indizes für die Tabelle `Lebensmittel`
--
ALTER TABLE `Lebensmittel`
 ADD PRIMARY KEY (`Nummer`), ADD KEY `Lebensmittel_ibfk_1` (`Fach`), ADD KEY `neuer-Fremdschluessel` (`Besitzer`);

--
-- Indizes für die Tabelle `resetPW`
--
ALTER TABLE `resetPW`
 ADD PRIMARY KEY (`OrderID`), ADD KEY `resetPW_ibfk_1` (`Benutzer`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `Einkauf`
--
ALTER TABLE `Einkauf`
MODIFY `Num` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=61;
--
-- AUTO_INCREMENT für Tabelle `Fach`
--
ALTER TABLE `Fach`
MODIFY `lNummer` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=23;
--
-- AUTO_INCREMENT für Tabelle `Kuehlschrank`
--
ALTER TABLE `Kuehlschrank`
MODIFY `laufNummer` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=32;
--
-- AUTO_INCREMENT für Tabelle `Lebensmittel`
--
ALTER TABLE `Lebensmittel`
MODIFY `Nummer` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=73;
--
-- AUTO_INCREMENT für Tabelle `resetPW`
--
ALTER TABLE `resetPW`
MODIFY `OrderID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `Einkauf`
--
ALTER TABLE `Einkauf`
ADD CONSTRAINT `Einkauf_ibfk_1` FOREIGN KEY (`Benutzer`) REFERENCES `Benutzer` (`EMail`) ON DELETE SET NULL ON UPDATE NO ACTION;

--
-- Constraints der Tabelle `Fach`
--
ALTER TABLE `Fach`
ADD CONSTRAINT `Fach_ibfk_1` FOREIGN KEY (`Kuehlschrank`) REFERENCES `Kuehlschrank` (`laufNummer`) ON DELETE SET NULL ON UPDATE NO ACTION;

--
-- Constraints der Tabelle `Kuehlschrank`
--
ALTER TABLE `Kuehlschrank`
ADD CONSTRAINT `Kuehlschrank_ibfk_1` FOREIGN KEY (`Besitzer`) REFERENCES `Benutzer` (`EMail`) ON DELETE SET NULL ON UPDATE NO ACTION;

--
-- Constraints der Tabelle `Lebensmittel`
--
ALTER TABLE `Lebensmittel`
ADD CONSTRAINT `Lebensmittel_ibfk_1` FOREIGN KEY (`Fach`) REFERENCES `Fach` (`lNummer`) ON DELETE SET NULL ON UPDATE NO ACTION,
ADD CONSTRAINT `neuer-Fremdschluessel` FOREIGN KEY (`Besitzer`) REFERENCES `Benutzer` (`EMail`);

--
-- Constraints der Tabelle `resetPW`
--
ALTER TABLE `resetPW`
ADD CONSTRAINT `resetPW_ibfk_1` FOREIGN KEY (`Benutzer`) REFERENCES `Benutzer` (`EMail`) ON DELETE SET NULL ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

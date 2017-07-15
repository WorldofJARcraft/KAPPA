-- phpMyAdmin SQL Dump
-- version 4.2.12deb2+deb8u2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 15. Jul 2017 um 20:22
-- Server Version: 5.5.54-0+deb8u1
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
-- Erstellt am: 30. Jun 2017 um 14:50
--

CREATE TABLE IF NOT EXISTS `Benutzer` (
  `EMail` varchar(100) NOT NULL COMMENT 'Gleichzeitig Datenbankname!!!',
  `Passwort` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `Benutzer`
--

INSERT INTO `Benutzer` (`EMail`, `Passwort`) VALUES
('admin@worldofjarcraft.ddns.net', '1234');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Einkauf`
--
-- Erstellt am: 01. Jul 2017 um 15:01
--

CREATE TABLE IF NOT EXISTS `Einkauf` (
`Num` int(11) NOT NULL,
  `Lebensmittel` text NOT NULL,
  `Benutzer` varchar(100) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- RELATIONEN DER TABELLE `Einkauf`:
--   `Benutzer`
--       `Benutzer` -> `EMail`
--

--
-- Daten für Tabelle `Einkauf`
--

INSERT INTO `Einkauf` (`Num`, `Lebensmittel`, `Benutzer`) VALUES
(6, 'FLEISCH!!!', 'admin@worldofjarcraft.ddns.net');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Fach`
--
-- Erstellt am: 01. Jul 2017 um 14:59
--

CREATE TABLE IF NOT EXISTS `Fach` (
`lNummer` int(11) NOT NULL,
  `Kuehlschrank` int(11) DEFAULT NULL,
  `Name` varchar(100) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- RELATIONEN DER TABELLE `Fach`:
--   `Kuehlschrank`
--       `Kuehlschrank` -> `laufNummer`
--

--
-- Daten für Tabelle `Fach`
--

INSERT INTO `Fach` (`lNummer`, `Kuehlschrank`, `Name`) VALUES
(8, 18, '7');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Kuehlschrank`
--
-- Erstellt am: 15. Jul 2017 um 16:48
--

CREATE TABLE IF NOT EXISTS `Kuehlschrank` (
`laufNummer` int(11) NOT NULL,
  `Name` text NOT NULL,
  `Zahl_Faecher` int(11) NOT NULL,
  `Besitzer` varchar(100) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

--
-- RELATIONEN DER TABELLE `Kuehlschrank`:
--   `Besitzer`
--       `Benutzer` -> `EMail`
--

--
-- Daten für Tabelle `Kuehlschrank`
--

INSERT INTO `Kuehlschrank` (`laufNummer`, `Name`, `Zahl_Faecher`, `Besitzer`) VALUES
(18, 'Kueche', 7, 'admin@worldofjarcraft.ddns.net');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Lebensmittel`
--
-- Erstellt am: 15. Jul 2017 um 16:55
--

CREATE TABLE IF NOT EXISTS `Lebensmittel` (
`Nummer` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Anzahl` int(11) NOT NULL COMMENT 'Wie oft das Lebensmittel da ist.',
  `Haltbarkeitsdatum` bigint(20) NOT NULL COMMENT 'Angabe als Java-Timestamp.',
  `Fach` int(11) DEFAULT NULL,
  `Besitzer` varchar(100) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

--
-- RELATIONEN DER TABELLE `Lebensmittel`:
--   `Besitzer`
--       `Benutzer` -> `EMail`
--   `Fach`
--       `Fach` -> `lNummer`
--

--
-- Daten für Tabelle `Lebensmittel`
--

INSERT INTO `Lebensmittel` (`Nummer`, `Name`, `Anzahl`, `Haltbarkeitsdatum`, `Fach`, `Besitzer`) VALUES
(6, 'massig viel Fleisch', 999, 123466789987654321, 8, 'admin@worldofjarcraft.ddns.net'),
(7, 'noch mehr Fleisch', 999, 123466789987654321, 8, 'admin@worldofjarcraft.ddns.net'),
(8, 'EkelgemÃ¼se', 999, 123466789987654321, 8, 'admin@worldofjarcraft.ddns.net'),
(9, 'RANZ', 999, 123466789987654321, 8, NULL),
(10, 'verdammt viel FLEISCH vom Rind', 2, 1234567890, 8, NULL),
(11, 'dst', 11, 1234567786543, 8, 'admin@worldofjarcraft.ddns.net'),
(12, 'RANZ', 999, 123466789987654321, 8, 'admin@worldofjarcraft.ddns.net'),
(13, 'RANZ', 999, 123466789987654321, 8, 'admin@worldofjarcraft.ddns.net'),
(14, 'RANZ', 999, 123466789987654321, 8, 'admin@worldofjarcraft.ddns.net'),
(15, 'RANZ', 999, 123466789987654321, 8, 'admin@worldofjarcraft.ddns.net');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `resetPW`
--
-- Erstellt am: 01. Jul 2017 um 15:08
--

CREATE TABLE IF NOT EXISTS `resetPW` (
`OrderID` int(11) NOT NULL,
  `Benutzer` varchar(100) DEFAULT NULL,
  `Code` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

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
MODIFY `Num` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT für Tabelle `Fach`
--
ALTER TABLE `Fach`
MODIFY `lNummer` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT für Tabelle `Kuehlschrank`
--
ALTER TABLE `Kuehlschrank`
MODIFY `laufNummer` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=19;
--
-- AUTO_INCREMENT für Tabelle `Lebensmittel`
--
ALTER TABLE `Lebensmittel`
MODIFY `Nummer` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=16;
--
-- AUTO_INCREMENT für Tabelle `resetPW`
--
ALTER TABLE `resetPW`
MODIFY `OrderID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=17;
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
ADD CONSTRAINT `neuer-Fremdschluessel` FOREIGN KEY (`Besitzer`) REFERENCES `Benutzer` (`EMail`),
ADD CONSTRAINT `Lebensmittel_ibfk_1` FOREIGN KEY (`Fach`) REFERENCES `Fach` (`lNummer`) ON DELETE SET NULL ON UPDATE NO ACTION;

--
-- Constraints der Tabelle `resetPW`
--
ALTER TABLE `resetPW`
ADD CONSTRAINT `resetPW_ibfk_1` FOREIGN KEY (`Benutzer`) REFERENCES `Benutzer` (`EMail`) ON DELETE SET NULL ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

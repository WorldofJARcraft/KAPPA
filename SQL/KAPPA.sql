-- phpMyAdmin SQL Dump
-- version 4.2.12deb2+deb8u2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 30. Jun 2017 um 18:13
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
('eric.ackermann.99@googlemail.com', '1234'),
('eric.ackermann.alles@googlemail.com', '123456');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Einkauf`
--
-- Erstellt am: 30. Jun 2017 um 14:50
--

CREATE TABLE IF NOT EXISTS `Einkauf` (
`Num` int(11) NOT NULL,
  `Lebensmittel` text NOT NULL,
  `Benutzer` varchar(100) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `Einkauf`
--

INSERT INTO `Einkauf` (`Num`, `Lebensmittel`, `Benutzer`) VALUES
(1, 'FLEISCH', 'eric.ackermann.99@googlemail.com'),
(2, 'asfgjklpöofyadbnmtgstrz', 'eric.ackermann.alles@googlemail.com'),
(3, 'viel Fleisch', 'eric.ackermann.99@googlemail.com');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Fach`
--
-- Erstellt am: 30. Jun 2017 um 15:41
--

CREATE TABLE IF NOT EXISTS `Fach` (
`lNummer` int(11) NOT NULL,
  `Kuehlschrank` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `Fach`
--

INSERT INTO `Fach` (`lNummer`, `Kuehlschrank`, `Name`) VALUES
(1, 2, ''),
(2, 14, '1'),
(3, 14, ''),
(5, 14, '2');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Kuehlschrank`
--
-- Erstellt am: 30. Jun 2017 um 14:50
--

CREATE TABLE IF NOT EXISTS `Kuehlschrank` (
`laufNummer` int(11) NOT NULL,
  `Name` text NOT NULL,
  `Zahl_Faecher` int(11) NOT NULL,
  `Besitzer` varchar(100) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `Kuehlschrank`
--

INSERT INTO `Kuehlschrank` (`laufNummer`, `Name`, `Zahl_Faecher`, `Besitzer`) VALUES
(2, 'Küche rechts', 7, 'eric.ackermann.99@googlemail.com'),
(3, 'wz', 2, 'eric.ackermann.alles@googlemail.com'),
(14, 'Kuechelinks', 7, 'eric.ackermann.99@googlemail.com');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Lebensmittel`
--
-- Erstellt am: 30. Jun 2017 um 16:07
--

CREATE TABLE IF NOT EXISTS `Lebensmittel` (
`Nummer` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Anzahl` int(11) NOT NULL COMMENT 'Wie oft das Lebensmittel da ist.',
  `Haltbarkeitsdatum` bigint(20) NOT NULL COMMENT 'Angabe als Java-Timestamp.',
  `Fach` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `Lebensmittel`
--

INSERT INTO `Lebensmittel` (`Nummer`, `Name`, `Anzahl`, `Haltbarkeitsdatum`, `Fach`) VALUES
(1, 'Garnelen', 1, 201734678989098, 1),
(2, 'FLEISCH', 1000, 245789063, 5),
(3, 'FLEISCH', 5, 123456789, 2);

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
 ADD PRIMARY KEY (`Num`), ADD KEY `Benutzer` (`Benutzer`);

--
-- Indizes für die Tabelle `Fach`
--
ALTER TABLE `Fach`
 ADD PRIMARY KEY (`lNummer`), ADD KEY `Kuehlschrank` (`Kuehlschrank`);

--
-- Indizes für die Tabelle `Kuehlschrank`
--
ALTER TABLE `Kuehlschrank`
 ADD PRIMARY KEY (`laufNummer`), ADD KEY `Kuehlschrank_ibfk_1` (`Besitzer`);

--
-- Indizes für die Tabelle `Lebensmittel`
--
ALTER TABLE `Lebensmittel`
 ADD PRIMARY KEY (`Nummer`), ADD KEY `Fach` (`Fach`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `Einkauf`
--
ALTER TABLE `Einkauf`
MODIFY `Num` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT für Tabelle `Fach`
--
ALTER TABLE `Fach`
MODIFY `lNummer` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT für Tabelle `Kuehlschrank`
--
ALTER TABLE `Kuehlschrank`
MODIFY `laufNummer` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=16;
--
-- AUTO_INCREMENT für Tabelle `Lebensmittel`
--
ALTER TABLE `Lebensmittel`
MODIFY `Nummer` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `Einkauf`
--
ALTER TABLE `Einkauf`
ADD CONSTRAINT `Einkauf_ibfk_1` FOREIGN KEY (`Benutzer`) REFERENCES `Benutzer` (`EMail`);

--
-- Constraints der Tabelle `Fach`
--
ALTER TABLE `Fach`
ADD CONSTRAINT `Fach_ibfk_1` FOREIGN KEY (`Kuehlschrank`) REFERENCES `Kuehlschrank` (`laufNummer`);

--
-- Constraints der Tabelle `Kuehlschrank`
--
ALTER TABLE `Kuehlschrank`
ADD CONSTRAINT `Kuehlschrank_ibfk_1` FOREIGN KEY (`Besitzer`) REFERENCES `Benutzer` (`EMail`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `Lebensmittel`
--
ALTER TABLE `Lebensmittel`
ADD CONSTRAINT `Lebensmittel_ibfk_1` FOREIGN KEY (`Fach`) REFERENCES `Fach` (`lNummer`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

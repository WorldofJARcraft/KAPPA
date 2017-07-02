<?php
//Quelle: http://stefan-draeger-software.de/blog/android-app-mit-mysql-datenbank-verbinden/

//Konfiguration einlesen
require_once("Conf.php");
//Datenbankverbindung aufbauen
$connection = mysqli_connect($host,$user,$passwort,$datenbank);
//auf Fehler prüfen
if (mysqli_connect_errno()) {
    echo mysql_errno($connection) . ": " . mysql_error($connection). "\n";
    die();
}
neuerUser($connection);
//trägt die aktuelle Zeit für die übergebene Startnummer in die gewählte Station ein.
function neuerUser ($connection) {
	//zuerst prüfen, ob Zeit schon vorhanden
	
	//Abfrage formulieren...
	//genaue Tabelle und einzutragende Startnummer werden per GET in der Adresse übergeben und hier eingesetzt
	//auslesen, ob für Startnummer schon Zeit eingetragen ist
	$sqlStmt = "DELETE FROM `Einkauf` WHERE Benutzer IS NULL;";
	$result =  mysqli_query($connection,$sqlStmt);
	$sqlStmt = "DELETE FROM `Kuehlschrank` WHERE Besitzer IS NULL;";
	$result =  mysqli_query($connection,$sqlStmt);
	$sqlStmt = "DELETE FROM `Fach` WHERE Kuehlschrank IS NULL;";
	$result =  mysqli_query($connection,$sqlStmt);  
	$sqlStmt = "DELETE FROM `Lebensmittel` WHERE Fach IS NULL;";
	$result =  mysqli_query($connection,$sqlStmt);  
  //Abfrage vorbereiten
	
	closeConnection($connection);
}
  

//Verbindung schließen.
function closeConnection($connection){
  mysqli_close($connection);
}
?>
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
//übergebene Zeit eintragen
neuerUser($connection);

//trägt die aktuelle Zeit für die übergebene Startnummer in die gewählte Station ein.
function neuerUser ($connection) {
	//zuerst prüfen, ob Zeit schon vorhanden
	
	//Abfrage formulieren...
	//genaue Tabelle und einzutragende Startnummer werden per GET in der Adresse übergeben und hier eingesetzt
	//auslesen, ob für Startnummer schon Zeit eingetragen ist
	$sqlStmt = "SELECT `EMail` FROM `Benutzer` WHERE `EMail` = '".$_GET["mail"]."'";
	$result = mysqli_query($connection,$sqlStmt);
	$vorhanden = 0;
	if ($result = $connection->query($sqlStmt)){
		$row_cnt = $result->num_rows;
	if($row_cnt>0){ $vorhanden=1;}
	else{$vorhanden=0;}
	}
	if($vorhanden===0){
	$sqlStmt = "INSERT INTO `KAPPA`.`Benutzer` (`EMail`, `Passwort`) VALUES ('".$_GET["mail"]."', '".$_GET["pw"]."');";
	$result =  mysqli_query($connection,$sqlStmt);
	if($result===true)
		echo "Erfolg";
	}
	else {
		echo "User schon vorhanden.";	
	}
	//keine Ergebnisse, die zu betrachten wären

	//Verbindung schließen
	closeConnection($connection);
  
}
  

//Verbindung schließen.
function closeConnection($connection){
  mysqli_close($connection);
}
?>
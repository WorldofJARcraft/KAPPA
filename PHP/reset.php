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
	$sqlStmt = "SELECT Code FROM `resetPW` WHERE Benutzer='".$_GET["mail"]."';";
  //Abfrage vorbereiten
  $result =  mysqli_query($connection,$sqlStmt);
	$match = "false";  
  //wenn Ergebnisse...
  while ($zeile = mysqli_fetch_array( $result, MYSQL_ASSOC)){
  		//... dann die Zahl dr Messstationen ausgeben (Zahl in Spalte "Wert" der ersten und einzigen gefundenen Zeile)
      if($zeile["Code"]===$_GET["code"])
      $match = "true";
      }
      
  if($match==="true"){
  // Das Objekt wieder freigeben.
   //Ergebnisse leeren
	$result->free();
	//Abfrage formulieren...
	//genaue Tabelle und einzutragende Startnummer werden per GET in der Adresse übergeben und hier eingesetzt
	//auslesen, ob für Startnummer schon Zeit eingetragen ist
	$sqlStmt = "UPDATE `KAPPA`.`Benutzer` SET `Passwort` = '".$_GET["newPW"]."' WHERE `Benutzer`.`EMail` = '".$_GET["mail"]."';";
	$result =  mysqli_query($connection,$sqlStmt);
	if($result==true)
		echo "Erfolg";
	$sqlStmt = "DELETE FROM `resetPW` WHERE Benutzer='".$_GET["mail"]."'";
	$result =  mysqli_query($connection,$sqlStmt);
	$empfaenger = $_GET["mail"];
	$betreff = "Rücksetzen Ihres KAPPA-Passwortes";
	$from = "From: KAPPA-Administrator <kappa@worldofjarcraft.ddns.net>";
	$text = "Ihr KAPPA-Passwort wurde erfolgreich zurückgesetzt.";
mail($empfaenger, $betreff, $text, $from);	
	//keine Ergebnisse, die zu betrachten wären

	//Verbindung schließen
}
else echo "Falscher Code."; 
	closeConnection($connection);
  
}
  

//Verbindung schließen.
function closeConnection($connection){
  mysqli_close($connection);
}
?>
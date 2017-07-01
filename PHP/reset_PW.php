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
	$sqlStmt = "SELECT EMail FROM `Benutzer` WHERE EMail='".$_GET["mail"]."';";
  //Abfrage vorbereiten
  $result =  mysqli_query($connection,$sqlStmt);
	$exists = "false";  
  //wenn Ergebnisse...
  if ($result = $connection->query($sqlStmt)) {
  		//... dann die Zahl dr Messstationen ausgeben (Zahl in Spalte "Wert" der ersten und einzigen gefundenen Zeile)
      if(empty($result->fetch_assoc()["EMail"])){}
      else $exists = "true";
      }
      
  if($exists==="true"){
  // Das Objekt wieder freigeben.
   //Ergebnisse leeren
	$result->free();
	$rand = rand(100, 999);
	//Abfrage formulieren...
	//genaue Tabelle und einzutragende Startnummer werden per GET in der Adresse übergeben und hier eingesetzt
	//auslesen, ob für Startnummer schon Zeit eingetragen ist
	$sqlStmt = "INSERT INTO `KAPPA`.`resetPW` (`OrderID`, `Benutzer`, `Code`) VALUES (NULL, '".$_GET["mail"]."', '".$rand."');";
	$result =  mysqli_query($connection,$sqlStmt);
	if($result==true)
		echo "Erfolg";
	$empfaenger = $_GET["mail"];
	$betreff = "Rücksetzen Ihres KAPPA-Passwortes";
	$from = "From: KAPPA-Administrator <kappa@worldofjarcraft.ddns.net>";
	$text = "Bitte öffnen Sie folgenden Link, um Ihr Passwort zurückzusetzen: \"worldofjarcraft.ddns.net/kappa/reset.php?code=".$rand."&mail=".$empfaenger."&newPW=".$_GET["pw"]."\".";
mail($empfaenger, $betreff, $text, $from);	
	//keine Ergebnisse, die zu betrachten wären

	//Verbindung schließen
}
else echo "User existiert nicht."; 
	closeConnection($connection);
  
}
  

//Verbindung schließen.
function closeConnection($connection){
  mysqli_close($connection);
}
?>
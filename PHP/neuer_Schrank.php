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
$auth = verify($connection);
if($auth === "true")
neuerUser($connection);
else 
echo "unauthorised!";

function verify ($connection) {
	//zuerst prüfen, ob Zeit schon vorhanden
	//Abfrage formulieren...
	//genaue Tabelle und einzutragende Startnummer werden per GET in der Adresse übergeben und hier eingesetzt
	//auslesen, ob für Startnummer schon Zeit eingetragen ist
	$sqlStmt = "SELECT Passwort FROM `Benutzer` WHERE EMail='".$_GET["mail"]."'";
  //Abfrage vorbereiten
  $result =  mysqli_query($connection,$sqlStmt);
  $pw=null;
  //wenn Ergebnisse...
  if ($result = $connection->query($sqlStmt)) {
  		//... dann die Zahl dr Messstationen ausgeben (Zahl in Spalte "Wert" der ersten und einzigen gefundenen Zeile)
      $pw=$result->fetch_assoc()["Passwort"];
      }   
	if($pw!=null&$pw===$_GET["pw"]){
		return "true";
			//echo "pascht.";		
		}
	else {
		//echo "MÜLL!!!";	
	}
	//echo "hallo";
	return "false";
	closeConnection($connection);
  
}

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
  // Das Objekt wieder freigeben.
   //Ergebnisse leeren
	$result->free();
	$sqlStmt = "SELECT * FROM `Kuehlschrank` WHERE Name='".$_GET["name"]."' AND Besitzer='".$_GET["mail"]."';";
  //Abfrage vorbereiten
  $result =  mysqli_query($connection,$sqlStmt);
	$vorhanden = "false";  
  //wenn Ergebnisse...
  if ($result = $connection->query($sqlStmt)) {
  		//... dann die Zahl dr Messstationen ausgeben (Zahl in Spalte "Wert" der ersten und einzigen gefundenen Zeile)
      if(empty($result->fetch_assoc()["Besitzer"])){}
      else {
			$vorhanden = "true";       
      }
      }
  // Das Objekt wieder freigeben.
   //Ergebnisse leeren
	$result->free();
	if($exists==="true"&&$vorhanden==="false"){
	$sqlStmt = "INSERT INTO `KAPPA`.`Kuehlschrank` (`laufNummer`, `Name`, `Zahl_Faecher`, `Besitzer`) VALUES (NULL, '".$_GET["name"]."', '".$_GET["faecher"]."', '".$_GET["mail"]."');";
	$result =  mysqli_query($connection,$sqlStmt);
	if($result==true)
		echo "Erfolg";
	//keine Ergebnisse, die zu betrachten wären
	}
	else if($exists==false){
		echo "User nicht vorhanden: Operation nicht erlaubt.";
	}
	else if($vorhanden==false)
		echo"Kühlschrank schon vorhanden: Operation nicht erlaubt.";
	//Verbindung schließen
	closeConnection($connection);
  
}
  

//Verbindung schließen.
function closeConnection($connection){
  mysqli_close($connection);
}
?>
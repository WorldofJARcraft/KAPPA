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
	
	$sqlStmt = "SELECT * FROM `Lebensmittel` WHERE Name LIKE '%".$_GET["sterm"]."%' AND Besitzer = '".$_GET["mail"]."'";
	$result =  mysqli_query($connection,$sqlStmt);
	while ($zeile = mysqli_fetch_array( $result, MYSQL_ASSOC)){
  		//... dann die Zahl dr Messstationen ausgeben (Zahl in Spalte "Wert" der ersten und einzigen gefundenen Zeile)
      echo $zeile["Nummer"].";".$zeile["Name"].";".$zeile["Anzahl"].";".$zeile["Haltbarkeitsdatum"]."|"; 
  }    
	//Verbindung schließen
	closeConnection($connection);
}
  

//Verbindung schließen.
function closeConnection($connection){
  mysqli_close($connection);
}
?>
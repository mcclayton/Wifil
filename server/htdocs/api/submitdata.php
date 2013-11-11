<?php
if(!(isset($_POST['guid'])&&isset($_POST['secret'])&&isset($_FILES['data']))) {
	echo "E:Invalid Parameters";
	exit;
}

if($_POST['guid']!="1234" || $_POST['secret']!="5678") {
	echo "E:Invalid GUID/Secret";
	exit;
}

$dataStr = file_get_contents($_FILES['data']['tmp_name']);
$data = json_decode($dataStr);


$host = 'localhost';
$user = 'WifilManager';
$pw = '75xb9wbf543MTuJa';
$db = 'wifil';

$con = mysqli_connect($host,$user,$pw,$db);

if(mysqli_connect_errno($con)) {
	echo "Failed to connect to MySQL: ".mysqli_connect_error();
	exit;
}

foreach($data as $ent) {
	$mac = mysqli_real_escape_string($con,$ent->MAC);
	$query = "SELECT * FROM hotspotlist WHERE MAC = \"$mac\"";
	$result = mysqli_query($con,$query);
	$hslist = array();
	while($row = mysqli_fetch_array($result)) {
		$hslist[] = $row;
	}
	if(count($hslist) == 0) {
		addHotspot($con,$ent);
	} else if(count($hslist)==1) {
		updateHotspot($con,$ent);
	} else {
		echo "E:Multiple matches. Contact admin.";
		exit;
	}
}

if(isset($_POST['debug'])) {
	echo "Full table:<br>";
	$result = mysqli_query($con,"SELECT * FROM hotspotlist");
	while($row = mysqli_fetch_array($result)) {
		print_r($row);
		echo "<br>";
	}
	echo "---END OF RESULTS---<br>";
}

echo 1;
exit;

function addHotspot($con,$ent) {
	if(isset($_POST['debug']))
		echo "Adding hotspot<br>";
	$query = "INSERT INTO hotspotlist (SSID,MAC,lat,lon,radius,meta) VALUES (\""
		.mysqli_real_escape_string($con,$ent->SSID)."\",\""
		.mysqli_real_escape_string($con,$ent->MAC)."\","
		.$ent->lat.",".$ent->lon.",30,null)";
	mysqli_query($con,$query);
}

function updateHotspot($con,$ent) {
	if(isset($_POST['debug']))
		echo "Updating hotspot<br>";
	$query = "UPDATE hotspotlist SET lat=".$ent->lat.",lon=".$ent->lon." WHERE MAC=\"".$ent->MAC."\"";
	if(isset($_POST['debug'])) {
		echo $query."<br>";
	}
	mysqli_query($con,$query);
}


?>
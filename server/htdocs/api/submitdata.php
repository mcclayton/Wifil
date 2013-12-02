<?php
if(!(isset($_POST['guid'])&&isset($_POST['secret'])&&isset($_POST['data']))) {
	echo "E:Invalid Parameters";
	exit;
}

if($_POST['guid']!="1234" || $_POST['secret']!="5678") {
	echo "E:Invalid GUID/Secret";
	exit;
}

$data = json_decode($_POST['data']);


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
	$query = "INSERT INTO editlist (UserGUID,HotspotID,lat,lon) VALUES (\""
		.mysqli_real_escape_string($con,$_POST['guid'])."\",LAST_INSERT_ID(),".$ent->lat.",".$ent->lon.")";
	mysqli_query($con,$query);
}

function updateHotspot($con,$ent) {
	if(isset($_POST['debug']))
		echo "Updating hotspot<br>";
	$query = "SELECT lat,lon,count FROM hotspotlist WHERE MAC=\"".$ent->MAC."\"";
	if(isset($_POST['debug'])) {
		echo $query."<br>";
	}
	$row = mysqli_fetch_array(mysqli_query($con,$query));
	
	$oldLat = floatval($row['lat']);
	$oldLon = floatval($row['lon']);
	$count = $row['count'];
	
	$newLat = ($count*$oldLat + floatval($ent->lat))/($count+1);
	$newLon = ($count*$oldLon + floatval($ent->lon))/($count+1);
	
	$query = "UPDATE hotspotlist SET lat=".$newLat.",lon=".$newLon.",count = count + 1 WHERE MAC=\"".$ent->MAC."\"";
	if(isset($_POST['debug'])) {
		echo $query."<br>";
	}
	mysqli_query($con,$query);
	
	$query = "SELECT ID,lat,lon FROM hotspotlist WHERE MAC=\"".$ent->MAC."\"";
	if(isset($_POST['debug'])) {
		echo $query."<br>";
	}
	$val = mysqli_fetch_array(mysqli_query($con,$query));
	$hsid = $val['ID'];
	$hslat = $val['lat'];
	$hslon = $val['lon'];
	if(isset($_POST['debug'])) {
		echo "ID: ".$hsid."<br>";
	}
	
	$query = "INSERT INTO editlist (UserGUID,HotspotID,lat,lon) VALUES (\""
		.mysqli_real_escape_string($con,$_POST['guid'])."\",".$hsid.",".$ent->lat.",".$ent->lon.")";
	if(isset($_POST['debug'])) {
		echo $query."<br>";
	}
	mysqli_query($con,$query);
	
	$query = "SELECT lat,lon FROM editlist WHERE HotspotID=".$hsid;
	if(isset($_POST['debug'])) {
		echo $query."<br>";
	}
	$res = mysqli_query($con,$query);
	$maxRadius = 20;
	while($row = mysqli_fetch_array($res)) {
		$testRadius = haversine($hslat,$hslon,$row['lat'],$row['lon']);
		if(isset($_POST['debug']))
			echo "test radius: ".$testRadius."<br>";
		if($testRadius > $maxRadius)
			$maxRadius = $testRadius;
	}
	
	$query = "UPDATE hotspotlist SET radius=".floor($maxRadius)." WHERE ID=".$hsid;
	if(isset($_POST['debug'])) {
		echo $query."<br>";
	}
	mysqli_query($con,$query);
}

function haversine(
  $latitudeFrom, $longitudeFrom, $latitudeTo, $longitudeTo, $earthRadius = 6378100)
{
  // convert from degrees to radians
  $latFrom = deg2rad($latitudeFrom);
  $lonFrom = deg2rad($longitudeFrom);
  $latTo = deg2rad($latitudeTo);
  $lonTo = deg2rad($longitudeTo);

  $latDelta = $latTo - $latFrom;
  $lonDelta = $lonTo - $lonFrom;

  $angle = 2 * asin(sqrt(pow(sin($latDelta / 2), 2) +
    cos($latFrom) * cos($latTo) * pow(sin($lonDelta / 2), 2)));
  $val = $angle * $earthRadius;
  if(isset($_POST['debug']))
	echo "haversine ($latitudeFrom,$longitudeFrom) to ($latitudeTo,$longitudeTo) = $val<br>";
  return $val;
}


?>
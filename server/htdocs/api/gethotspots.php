<?php
if(!(isset($_POST['lat'])&&isset($_POST['lon'])&&isset($_POST['r'])&&isset($_POST['guid'])&&isset($_POST['secret']))) {
	echo "E:Invalid Parameters (lat,lon,r)"; //todo: do not include parameter names in release version
	exit;
}

$latitude = floatval($_POST['lat']);
$longitude = floatval($_POST['lon']);
$radius = floatval($_POST['r']);

if(!( $latitude && $longitude && $radius )) {
	echo "E:Invalid lat/lon/r";
	exit;
}

if($_POST['guid']!="1234" || $_POST['secret']!="5678") {
	echo "E:Invalid GUID/Secret";
	exit;
}

$maxradius = 10;
if($_POST['r']>$maxradius) {
	echo "E:Radius exceeds maximum allowance.";
	exit;
}

function haversine(
  $latitudeFrom, $longitudeFrom, $latitudeTo, $longitudeTo, $earthRadius = 6378.1)
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
  return $angle * $earthRadius;
}

$host = 'localhost';
$user = 'WifilManager';
$pw = '75xb9wbf543MTuJa';
$db = 'wifil';

$con = mysqli_connect($host,$user,$pw,$db);

if(mysqli_connect_errno($con)) {
	echo "Failed to connect to MySQL: ".mysqli_connect_error();
	exit;
}

$latUnit = haversine($_POST['lat'],$_POST['lon'],$_POST['lat']+1,$_POST['lon']);
$lonUnit = haversine($_POST['lat'],$_POST['lon'],$_POST['lat'],$_POST['lon']+1);

$latRange = ( $_POST['r'] / $latUnit );
$lonRange = ( $_POST['r'] / $lonUnit );

$latMin = $_POST['lat'] - $latRange;
$latMax = $_POST['lat'] + $latRange;

$lonMin = $_POST['lon'] - $lonRange;
$lonMax = $_POST['lon'] + $lonRange;

if(isset($_POST['debug']))
	echo "Lat: $latUnit ($latMin - $latMax)<br>Lon: $lonUnit ($lonMin - $lonMax)<br>";

$result = mysqli_query($con,"SELECT SSID,MAC,lat,lon,radius,meta FROM hotspotlist WHERE lat BETWEEN $latMin AND $latMax AND lon BETWEEN $lonMin and $lonMax"); //todo: add WHERE clause to check lat/lon/radius

$hslist = array();
while($row = mysqli_fetch_array($result))  {
	$hslist[] = $row;
}

echo json_encode($hslist);

mysqli_close($con);

?>
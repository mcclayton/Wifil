<?php
if(!(isset($_POST['lat'])&&isset($_POST['lon'])&&isset($_POST['r'])&&isset($_POST['guid'])&&isset($_POST['secret']))) {
	echo "E:Invalid Parameters (lat,lon,r)"; //todo: do not include parameter names in release version
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

$host = 'localhost';
$user = 'WifilManager';
$pw = '75xb9wbf543MTuJa';
$db = 'wifil';

$con = mysqli_connect($host,$user,$pw,$db);

if(mysqli_connect_errno($con)) {
	echo "Failed to connect to MySQL: ".mysqli_connect_error();
	exit;
}

$result = mysqli_query($con,"SELECT * FROM hotspotlist"); //todo: add WHERE clause to check lat/lon/radius

$hslist = array();
while($row = mysqli_fetch_array($result))  {
	$hslist[] = $row;
}

echo json_encode($hslist);

mysqli_close($con);

?>
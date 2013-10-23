<?php /*

$host = 'localhost';
$user = 'WifilManager';
$pw = '75xb9wbf543MTuJa';
$db = 'wifil';

$con = mysqli_connect($host,$user,$pw,$db);

if(mysqli_connect_errno($con))
	echo "Failed to connect to MySQL: ".mysqli_connect_error();

$latmin = 40.41;
$latmax = 40.45;
$lonmin = -86.95;
$lonmax = -86.90;

$n=0;
for($lat = $latmin;$lat<$latmax;$lat+=.005) {
	for($lon = $lonmin;$lon<$lonmax;$lon+=.005) {
		mysqli_query($con,"INSERT INTO regionlist (lat,lon,regionID) VALUES ('$lat','$lon',$n)");
		$n++;
	}
}

mysqli_close($con);

*/ ?>
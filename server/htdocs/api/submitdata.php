<?php
if(!isset($_GET['guid'])) {
	echo "E:Invalid GUID";
	exit;
}
if(!isset($_GET['data'])) {
	echo "E:No data provided";
	exit;
}
echo 1;
?>
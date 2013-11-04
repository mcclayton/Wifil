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

//todo: validate data and add to database


echo 1;
?>
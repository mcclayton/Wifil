<?php
if(!(isset($_POST["guid"]) && isset($_POST["secret"]))) {
	echo "E:Invalid Parameters";
	exit;
}
if(!($_POST["guid"]=="1234" && $_POST["secret"]=="5678")) {
	echo "E:Invalid GUID/Secret";
	exit;
}
echo 1;
?>
<?php
if(!isset($_POST["guid"])) {
	echo "E:Invalid Parameters";
	exit;
}
if($_POST["guid"]=="1234")
	echo 5678;
?>
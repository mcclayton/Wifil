<?php
foreach($_GET as $key => $val) {
	echo $key." => ".$val."\n";
}
foreach($_POST as $key => $val) {
   echo $key." => ".$val."\n";
}
?>
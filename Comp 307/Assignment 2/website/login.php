<?php
require 'application.php';

$json = json_decode(stripslashes($_POST["message"]));
$session = login($json->username, $json->password);
if ($session == false) {
	echo -1;
} else {
	echo $session;
}
?>

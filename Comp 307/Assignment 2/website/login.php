<?php
require 'application.php';

$json = json_decode($_POST["message"]);
$session = login($json->username, $json->password);
if ($session == false) {
	echo -1;
} else {
	echo $session;
}
?>





<?php

$connection = null;

function encrypt($str, $shift) {
	$newstr = $str;
	for ($i=0; $i < strlen($str); $i ++) {
		$c = ord($str[$i]);
		if ($c >= 65 && $c <=  90) {
			$newstr[$i] = chr(mod(($c + $shift - 65),26) + 65);	
		} else if ($c >= 97 && $c <= 122) {
			$newstr[$i] = chr(mod(($c + $shift - 97),26) + 97);	
		}
	}
	return $newstr;
}

function mod($i, $m) {
	return ($i % $m + $m ) % $m;
}


$servername = "localhost:3306";
$username = "root";
$password = "madremia350";

try {
    $connection = new PDO("mysql:host=$servername;dbname=Q2DB", $username, $password);
} catch(PDOException $e) {
    echo $e->getMessage();
}

function login($username, $encrypted_password) {
	global $connection;
	$statement = $connection->prepare("SELECT * FROM members WHERE username = :username");
	$statement->execute(array(':username' => $username));
	$user = $statement->fetch();
	if($user == null) {
		return false;
	}
	$shared_key = $user['shared_key'];
	$password = encrypt($encrypted_password, - $shared_key);
	if($password == $user['password']) {
		return new_session($user['id']); //Create a new session for the user
	} else {
		return false;
	}
}

function get_user($username) {
	global $connection;
	$statement = $connection->prepare("SELECT * FROM members WHERE username = :username");
	$statement->execute(array(':username' => $username));
	return $statement->fetch();
}

function get_shared_key($username) {
	global $connection;
	$statement = $connection->prepare("SELECT shared_key FROM members WHERE username = :username");
	$statement->execute(array(':username' => $username));
	$result = $statement->fetch();
	if($result == null) {
		return null;
	} else {
		return $result['shared_key'];
	}
}

function get_session($user_id) {
	global $connection;
	$statement = $connection->prepare("SELECT * FROM sessions WHERE member_id = :user_id");
	$statement->execute(array(':user_id' => $user_id));
	return $statement->fetch();

}

function destroy_session($session_id) {
	global $connection;
	$statement = $connection->prepare("DELETE FROM sessions WHERE id = :id");
	$statement->execute(array(':id' => $session_id));
}

//Create a new session for the given user if not exist. If it does return the existing session id.
function new_session($user_id) {
	global $connection;
	$statement = $connection->prepare("SELECT * FROM sessions WHERE member_id = :user_id");
	$statement->execute(array(':user_id' => $user_id));
	$session = $statement->fetch();
	if( $session == null) {
		$statement = $connection->prepare("INSERT INTO sessions(member_id) VALUES (:user_id)");
		$statement->execute(array(':user_id' => $user_id));
		return $connection->lastInsertId();
	} else {
		return $session['id'];
	}
}


?>
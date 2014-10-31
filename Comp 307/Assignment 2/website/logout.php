<?php 
	require 'application.php';
	if(!isset($_GET['user_id']) or !isset($_GET['session_id'])) {
		header('Location: index.php');
		die();
	} 

	$user_id = $_GET['user_id'];
	$session = get_session($user_id);
	$session_id = $session['id'];
	if($session == null or $session['id'] != $_GET['session_id']) {
		header('Location: index.php');
		die();
	}
	
	destroy_session($session_id);
	header('Location: index.php?message=Logout sucessfully!');
?>
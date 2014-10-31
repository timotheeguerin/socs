<?php 
	require 'application.php';
	if(!isset($_GET['username']) or !isset($_GET['session_id'])) {
		header('Location: index.php');
		die();
	} 

	$user = get_user($_GET['username']);
	$session = get_session($user['id']);
	$session_id = $session['id'];
	if($session == null or $session['id'] != $_GET['session_id']) {
		header('Location: index.php');
		die();
	}
?>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Title of the document</title>
	<link rel="stylesheet" type="text/css" href="css/style.css?time=@Environment.TickCount">
	<link rel="stylesheet" type="text/css" href="css/font-awesome.min.css?time=@Environment.TickCount">
	<script type="text/javascript" src='javascript/script.js'></script>
	<div id='session-id' class='hidden'><?= $session_id ?></div>
</head>

<body>
	<div class='content'>
		<div>
			<div>
				<h1> Congratulations, you signed in succesfully <?= $user['username'] ?>.</h1>
			</div>
			<hr>
			<br>
			<div class='logout-container'>
				<a class='link' href='logout.php?user_id=<?= $user_id ?>&session_id=<?= $session_id ?>' title='Logout'>
					<div>
						<i class='fa fa-sign-out'></i>
					</div>
					<div>
						Logout
					</div>
				</a>
			</div>
		</div>
	</div>
</body>

</html>


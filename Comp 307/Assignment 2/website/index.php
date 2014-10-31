<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Title of the document</title>
	<link rel="stylesheet" type="text/css" href="css/style.css?time=@Environment.TickCount">
	<link rel="stylesheet" type="text/css" href="css/font-awesome.min.css?time=@Environment.TickCount">
	<script type="text/javascript" src='javascript/script.js'></script>
</head>

<body>
	<div class='content'>
		<div class='form' >
			<?php
				if(isset($_GET['message'])) {
					?>
						<div class='info'>
							<?= $_GET['message'] ?>
						</div>
					<?php
				}
			?>
			<h1> Log in </h1>

			<form action='login.php' method='post' onsubmit="return loginFormSubmitted(this)">
				<div id='error-container' class='hidden'>
					Error, login information are wrong.
				</div>
				<input name='username' placeholder='Username'/>
				<input name='password' placeholder='Password'/>
				<input type='submit' value='Log in'/>
			</form>
		</div>
	</div>
</body>

</html>
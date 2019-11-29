<?php
	require_once('.config.inc.php');
	if (!$_SESSION['valid']){
		echo '<html><head><title>'.$SITE_NAME.'</title></head><body>You haven\'t login, please <a href="/login.php">Login</a> first.</body></html>';
		die();
	}
	if (time() - $_SESSION["timeout"] > $LOGIN_TIMEOUT) {
		unset($_SESSION['valid']);
		header("Location: /login.php", true, 301);
	}
	// Update time after refresh this page
	$_SESSION["timeout"] = time();
?>
<!doctype html>
<html>
	<head>
		<title><?php echo $SITE_NAME; ?></title>
		<link rel="stylesheet" type="text/css" href="panel.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
		<script src="management.js?<?php echo time();?>"></script>
		<script>
			$(document).ready(() => {
				init_management_page();
			});
		</script>
	</head>
	<body>
		<h1 id="header_title">User mangent</h1>
		
		<div id="choose_panel">
			<label>
				<input type="radio" name="top_section_select_group" id="radio_user_management_panel" checked="checked">
				User management
			</label>
			<label>
				<input type="radio" name="top_section_select_group" id="radio_firebase_management_panel">
				Firebase™ Cloud Messaging management
			</label>
		</div>

		<hr>
		<div>
			<div id="div_user_management">
				<button id="btn_add_user">Add user</button>
				<button id="btn_refresh_user">Refresh</button>
				<label>
					Search user:
					<input type="text" id="txt_search_user_field">
				</label>
				<br />
				<div id="users_area"><!-- renender by javascript --></div>
			</div> <!-- div_user_management -->

			<div id="div_firebase_management" style="display: none;">
				
			</div>
		</div>
		<hr>
		Click <a href="/login.php?action=logout">here</a> to logout Session.
	</body>
</html>
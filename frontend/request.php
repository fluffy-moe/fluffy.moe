<?php
	require_once('.config.inc.php');

	// https://stackoverflow.com/a/13640164
	header("Cache-Control: no-store, no-cache, must-revalidate, max-age=0");
	header("Cache-Control: post-check=0, pre-check=0", false);
	header("Pragma: no-cache");
	header('Content-Type: application/json');

	if (!$_SESSION['valid']){
		//header('Location: /login.php', true, 301);
		http_response_code(400);
		die('{"result": "ERROR: Session timeout"}');
	}
	else {
		$_SESSION['timeout'] = time();
	}

	function get_pet_info($conn, $pet_id, $result) {
		$vac_info = array();
		$dei_info = array();
		$ocr_info = array();
		$inhost_info = array();
		$ht_info = array();
		$kt_info = array();
		$r = mysqli_query($conn, "SELECT * FROM `vaccination_record` WHERE `belong` = $pet_id ORDER BY `date` ASC");
		while ($_result = mysqli_fetch_assoc($r))
			array_push($vac_info, $_result);
		$r = mysqli_query($conn, "SELECT * FROM `deinsectzation_record` WHERE `belong` = $pet_id ORDER BY `date` ASC");
		while ($_result = mysqli_fetch_assoc($r))
			array_push($dei_info, $_result);
		$r = mysqli_query($conn, "SELECT * FROM `outpatient_clinic_record` WHERE `belong` = $pet_id ORDER BY `date` ASC");
		while ($_result = mysqli_fetch_assoc($r))
			array_push($ocr_info, $_result);
		$r = mysqli_query($conn, "SELECT * FROM `hospital_admission_record` WHERE `belong` = $pet_id ORDER BY `end_date` ASC");
		while ($_result = mysqli_fetch_assoc($r))
			array_push($inhost_info, $_result);
		$r = mysqli_query($conn, "SELECT * FROM `hematology_test_record` WHERE `belong` = $pet_id ORDER BY `date` ASC");
		while ($_result = mysqli_fetch_assoc($r))
			array_push($ht_info, $_result);
		$r = mysqli_query($conn, "SELECT * FROM `kidney_test_record` WHERE `belong` = $pet_id ORDER BY `date` ASC");
		while ($_result = mysqli_fetch_assoc($r))
			array_push($kt_info, $_result);
		return array("info" => $result, "vaccination" => $vac_info, "deinsectzation" => $dei_info,
				"outpatient_clinic" => $ocr_info, "hospital_admission" => $inhost_info,
				"hematology_test" => $ht_info, "kidney_test" => $kt_info);
	}

	if ($_SERVER['REQUEST_METHOD'] === 'POST') {
		if (isset($_POST['t'])){
			//if (!(($_POST['t'] === 'firebase_post' || $_POST['t'] === 'notification_manage')
			if (!($_POST['t'] === 'update_person' || $_POST['t'] === 'update_pet') && isset($_POST['payload'])) {
				http_response_code(400);
				die('{"result": "Bad request"}');
			}
			$payload = json_decode($_POST['payload'], true);
			/*if ($_POST['t'] === 'firebase_post')
				if (strlen($payload['title']) > 25 || strlen($payload['body']) > 300) {
					http_response_code(413);
					die('Payload too large');
				}*/
			$payload['t'] = $_POST['t'];
			$payload = json_encode($payload);
			$ch = curl_init($BACKEND_SERVER_ADMIN_PAGE);
			curl_setopt_array($ch, array(CURLOPT_POST => 1, CURLOPT_POSTFIELDS => $payload, CURLOPT_HEADER => 0, CURLOPT_RETURNTRANSFER => TRUE,
				CURLOPT_HTTPHEADER => array('Content-Type: application/json', 'Content-Length: ' . strlen($payload)),
				CURLOPT_TCP_FASTOPEN => TRUE));
			$response = curl_exec($ch);
			if (curl_errno($ch)) {
				http_response_code(502);
				die('Couldn\'t send request: ' . curl_error($ch));
			}
			$response = json_decode($response, true);
			if ($response['status'] == 200) {
				if (count($response['options']) > 0) {
					http_response_code(200);
					die(json_encode(array("options" => $response['options'])));
				}
				else
					http_response_code(204);
			}
			else {
				http_response_code(400);
				die(json_encode(array("status" => 400, "error_info" => $response['error']['info'])));
			}
			die();
		}
	}
	elseif ($_SERVER['REQUEST_METHOD'] === 'GET') {
		if (isset($_GET['t'])) {
			if ($_GET['t'] === '204') {
				http_response_code(204);
				die();
			}
			$j = array(
				"result" => null, // reversed
				"data" => array()
			);
			$conn = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD, $DB_NAME, $DB_PORT);
			if ($_GET['t'] === 'firebase_clients') {
				/*if ($_GET['c'] == 'device') { // Deprecated
					$r = mysqli_query($conn, "SELECT `token` FROM `firebasetoken` WHERE `register_date` > DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL 2 MONTH)");
					while ($result = mysqli_fetch_assoc($r))
						array_push($j["data"], $result["token"]);
				}*/
				$r = mysqli_query($conn, "SELECT `user_id` FROM `firebasetoken` WHERE `register_date` > DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL 2 MONTH)");
				$users = array();
				while ($result = mysqli_fetch_assoc($r))
					//array_push($j["data"], $result["user_id"]);
					array_push($users, $result["user_id"]);
				array_unique($users);

				foreach ($users as $value) {
					$r = mysqli_query($conn, "SELECT `username` FROM `accounts` WHERE `id` = $value");
					$result = mysqli_fetch_assoc($r);
					$j["data"][$value] = $result["username"];
					//array_push($j["data"], $result["username"]);
				}
				//$j['result'] = !empty($j["data"])? "success": "error";
			} elseif ($_GET['t'] === 'past_notifications') {
				$r = mysqli_query($conn, "SELECT `id`, `title`, `body`, `timestamp`, `available` FROM `notifications` ORDER BY `id` DESC");
				while ($result = mysqli_fetch_assoc($r))
					array_push($j["data"], $result);
			} elseif ($_GET['t'] === 'user') {
				$r = mysqli_query($conn, "SELECT `id`, `realname`, `nickname`, `phone`, `address` FROM `feeder_information`");
				while ($result = mysqli_fetch_assoc($r))
					array_push($j["data"], $result);
			} elseif ($_GET['t'] === 'user_detail' && isset($_GET['user_id'])) {
				$user_id = mysqli_escape_string($conn, $_GET['user_id']);
				$r = mysqli_query($conn, "SELECT * FROM `feeder_information` WHERE `id` = $user_id");
				$result = mysqli_fetch_assoc($r);
				$j["data"] = array("user" => $result);
			} elseif ($_GET['t'] === 'pet_detail' && isset($_GET['user_id'])) {
				$user_id = mysqli_escape_string($conn, $_GET['user_id']);
				$r = mysqli_query($conn, "SELECT * FROM `pet_information` WHERE `belong` = $user_id");
				while ($result = mysqli_fetch_assoc($r)){
					$pet_id = $result['id'];
					array_push($j['data'], get_pet_info($conn, $pet_id, $result));
				}
			} else {
				http_response_code(400);
				$j['result'] = "ERROR";
			}
			echo json_encode($j, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);
			mysqli_close($conn);
		}
		else {
			http_response_code(400);
			die('Bad request');
		}
	}
?>
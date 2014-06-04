<?php
	// Includes
	include('functions.php');
	
	// Receive POST data
	$username = $_POST["username"];
	$password = $_POST["password"];
	
	// POST JSON
	$data = array(
		'user'      => $username,
		'passwd'    => $password
	);

	$content = json_encode($data);
	$url = "http://cloud.cesga.es:8080/hadoop/v1/users";
	
	$response = postJSON($content,$url);
	
	if($response['message']=='OK'){
		echo 'User registered succesfully';
	}
	else{
		echo 'Registration failed';
	}
	
?>
<?php
	// Includes
	include('functions.php');
	
	// Receive POST
	$receivedJson = file_get_contents("php://input");

	$url = "http://cloud.cesga.es:8080/hadoop/v1/ip";
	
	$response = requestDeleteJSON($receivedJson,$url);
	
	// Response
	echo json_encode($response);
?>
<?php
	$receivedJson = file_get_contents("php://input");
	$arrayJson = json_decode($receivedJson, true);

	$ch = curl_init('http://cloud.cesga.es:8080/hadoop/v1/clusters/' . $arrayJson["id"]);                                                                      
	curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "GET");                                                                     
	curl_setopt($ch, CURLOPT_POSTFIELDS, $data_string);                                                                  
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);                                                                      
	curl_setopt($ch, CURLOPT_HTTPHEADER, array(                                                                          
		'Content-Type: application/json')                                                                       
	);                                                                                                                   
	
	$result = curl_exec($ch);
	
	echo json_encode($result);
	
 	//echo json_encode(json_decode($arrayJson["id"]));
 	//$myprint = 'http://cloud.cesga.es:8080/hadoop/v1/clusters/' . json_decode($arrayJson["id"]);
	//echo json_encode($myprint);
 ?>
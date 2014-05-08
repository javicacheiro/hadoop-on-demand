<?php                                                                                                                                             
	$ch = curl_init('http://cloud.cesga.es:8080/hadoop/v1/clusters');                                                                      
	curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "GET");                                                                     
	curl_setopt($ch, CURLOPT_POSTFIELDS, $data_string);                                                                  
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);                                                                      
	curl_setopt($ch, CURLOPT_HTTPHEADER, array(                                                                          
		'Content-Type: application/json')                                                                       
	);                                                                                                                   
	
	$result = curl_exec($ch);
	
	echo json_encode($result);
?>
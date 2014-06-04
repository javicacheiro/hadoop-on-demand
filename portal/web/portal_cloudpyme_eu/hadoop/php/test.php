<?php 
	// -- RECEIVE POST -- //
	echo '<b>Received post:</b><br/>';
	echo $_POST;



	
	echo '<br/><br/>';
	
	// -- SEND POST -- //
	echo '<b>Send post:</b><br/>';
	$data = array("message" => "foo");                                                                   
	$data_string = json_encode($data);                                                                                   

	$ch = curl_init('http://127.0.0.1:8080/hadoop/v1/test');                                                                      
	curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");                                                                     
	curl_setopt($ch, CURLOPT_POSTFIELDS, $data_string);                                                                  
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);                                                                      
	curl_setopt($ch, CURLOPT_HTTPHEADER, array(                                                                          
		'Content-Type: application/json',                                                                                
		'Content-Length: ' . strlen($data_string))                                                                       
	);                                                                                                                   
	
	$result = curl_exec($ch);
	echo $result;
?>
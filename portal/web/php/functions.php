<?php
	function postJSON($content , $url){
		$curl = curl_init($url);
		curl_setopt($curl, CURLOPT_HEADER, false);
		curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($curl, CURLOPT_HTTPHEADER,
				array("Content-type: application/json"));
		curl_setopt($curl, CURLOPT_POST, true);
		curl_setopt($curl, CURLOPT_POSTFIELDS, $content);

		$json_response = curl_exec($curl);

		$status = curl_getinfo($curl, CURLINFO_HTTP_CODE);

		curl_close($curl);

		$response = json_decode($json_response, true);
		
		return $response;
	}
	
	function requestDeleteJSON($content , $url){
		$ch = curl_init($url);
		curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "DELETE");
		curl_setopt($ch, CURLOPT_POSTFIELDS, $content);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_HTTPHEADER, array(
				'Content-Type: application/json',
				'Accept: application/json',
				'Content-Length: ' . strlen($content))
		);
		
		$result = curl_exec($ch);

		$response = json_decode($result, true);
		
		return $response;
	}
?>
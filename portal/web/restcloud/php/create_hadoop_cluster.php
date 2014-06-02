<?php
	// Includes
	include('functions.php');
	
	// Receive POST
	$receivedJson = file_get_contents("php://input");
	$arrayJson = json_decode($receivedJson, true);

	// Send JSON POST to REST
	$data = array(
		'size'      		=> $arrayJson["size"],
		'dfsReplicas'		=> $arrayJson["replicas"],
		'dfsBlockSize'		=> $arrayJson["blocksize"],
		'reduceTasksNumber'	=> $arrayJson["reduce"],
		'user'				=> $arrayJson["user"]
	);

	$content = json_encode($data);
	$url = "http://cloud.cesga.es:8080/hadoop/v1/clusters";
	
	$response = postJSON($content,$url);
	
	// Response
	$resp = '<span class="spanResponseTitle">Hadoop start request description</span><br/>'
		. '<span class="spanResponseLabel">Number of nodes : </span>'
			. '<span class="spanResponseValue">' . $arrayJson["size"] . '</span><br/>'
		. '<span class="spanResponseLabel">DFS Replicas : </span>'
			. '<span class="spanResponseValue">' . $arrayJson["replicas"] . '</span><br/>'
		. '<span class="spanResponseLabel">DFS Block Size : </span>'
			. '<span class="spanResponseValue">' . $arrayJson["blocksize"] . '</span><br/>'
		. '<span class="spanResponseLabel">Reduce Tasks Number : </span>'
			. '<span class="spanResponseValue">' . $arrayJson["reduce"] . '</span><br/>'
		. '<span class="spanResponseLabel">User : </span>'
			. '<span class="spanResponseValue">' . $arrayJson["user"] . '</span><br/>'
		. '<span class="spanResponseTitle">Hadoop start request response</span><br/>';
		
	if (substr($response['message'],0,3) == 'id:'){
		$resp .= '<span class="spanResponseLabel">id : </span>'
			. '<span class="spanResponseValue">' . substr($response["message"],3) . '</span><br/>';
		$resp .= '<br/><br/><span id="watchProgress" class="clickable_span" onclick="doStartProgress(' . substr($response["message"],3) . ')">WATCH PROGRESS</span>';
	}else{
		$resp .= '<span class="spanResponseLabel">message : </span>'
			. '<span class="spanResponseValue">' . $response["message"] . '</span><br/>';
	}
		
	echo json_encode($resp);
?>
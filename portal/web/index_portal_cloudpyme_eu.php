<?php

        header("Expires: Mon, 26 Jul 1997 05:00:00 GMT"); // Date in the past
        header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT"); // always modified
        header("Cache-Control: no-cache, must-revalidate"); // HTTP/1.1
        header("Pragma: no-cache");

	//require_once "config_lang.inc";
        require_once('/var/www/html/lib/config_lang.inc');
	require_once "grab_globals.inc.php";
	require_once "config_auth.inc";
	require_once "functions.inc";
      	require_once "mrbs_auth.inc";

	// Check the user is authorised for this page
	checkAuthorised("Lista de M&aacute;quinas Virtuales");
	$user = getUserName();
	$passwd = getUserPassword();	

        require("config_var.inc");
        require("funciones.php");
?>

<!DOCTYPE html>
<html>
<head>
    <title>Hadoop || Portal</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="robots" content="noindex">
    <link rel="shortcut icon" href="http://portal.cloudpyme.eu/iconos/favicon.ico" />
    <!-- Google Fonts -->    
    <link href='http://fonts.googleapis.com/css?family=Open+Sans&subset=latin,cyrillic-ext' rel='stylesheet' type='text/css'>
    <!-- Bootstrap: CSS, JS, iconos -->
    <!-- <script src="http://portal.cloudpyme.eu/js/bootstrap.min.js"></script> -->
    <!-- Estilos genéricos -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	<script type="text/javascript" src="js/main.js"></script>
	<script type="text/javascript" src="js/starthadoop.js"></script>
	<script type="text/javascript" src="js/stophadoop.js"></script>
	<script type="text/javascript" src="js/listhadoop.js"></script>
	<script type="text/javascript" src="js/clusterinfo.js"></script>
	<script type="text/javascript" src="js/startprogress.js"></script>
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/json2.js"></script>    
</head>

<body>
<?php
    	// SEND login POST to restcloud REST API /hadoop/v1/users    
   	 $array = array(
		"user"		=> $user,
		"passwd"	=> $passwd
   	 );


	$content = json_encode($array);
	$url = "http://cloud.cesga.es:8080/hadoop/v1/users";
	
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

	if($response['message']=='OK'){
		echo '<script type="text/javascript"> '
			. 'var user = "' . $user .'";'
			. 'doIndex(); '
			. '</script>';
	}
	else{
		echo 'Login failed';
	}    


    //Actualiza cada 15 sec.
    if ($pending) {
	echo "<meta http-equiv=\"Refresh\" content=\"15\">\n";
    }
?>

</body>
</html>

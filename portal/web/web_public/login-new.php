<?php
        header("Cache-Control: no-cache, must-revalidate"); // HTTP/1.1
        header("Pragma: no-cache");
	
	require_once('lib/config_lang.inc');
	require_once "grab_globals.inc.php";
	require_once "config_auth.inc";
	require_once "functions.inc";
      	require_once "mrbs_auth.inc";

	// Check the user is authorised for this page
	checkAuthorised("Hadoop on demand");
	$user = getUserName();
	$passwd = getUserPassword();	

        require("config_var.inc");
        require("funciones.php");
?>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
  <title>Index</title>
  <link href="css/bootstrap.min.css" rel="stylesheet">
  <link href="css/font-awesome.min.css" rel="stylesheet">
  <link rel="stylesheet" type="text/css" href="css/sticky-footer-navbar.css">
  <?php include('list_clusters.php'); ?>


   <!--ESTILO PARA CLASE HEADER-->
  <style type="text/css">
    .navbar-nav.navbar-right:last-child {
      margin-right: -10px;
    }
  </style>

  <!--ESTILO PARA CLASE HEADER-->
  <style type="text/css">
    .navbar-brand {
      float: left;
      height: 50px;
      padding: 18px 15px;
      font-size: 18px;
      line-height: 20px;
    }
  </style>

  <!--ESTILOS PARA BOTONES OPEN Y WARNING-->
  <style type="text/css">
    .label-warning,
    .badge-important {
        background-color: #ff9d00;
    }
  </style>

  <style type="text/css">
    .label-important,
    .badge-important {
        background-color: #0cf410;
    }
    
  </style>

  <!--ESTILO PARA FIJAR EL FOOTER EN EL FONDO DE LA VENTANA-->

  <style type="text/css">

  #footer {
    position:absolute;
    bottom:0;
    width: 1280px;
  }
  </style>
      
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
			. 'window.localStorage.setItem("user" , "' . $user .'"); '
			. 'window.location="wizard.htm"'
			. '</script>';
	}
	else{
		echo 'Access to REST service failed';
	}    


    //Actualiza cada 15 sec.
    if ($pending) {
	echo "<meta http-equiv=\"Refresh\" content=\"15\">\n";
    }
?>
  <script type="text/javascript">
  </script>
  <div id="prueba">
    <h1>HOLA MUNDO</h1>
  </div>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.js"></script>
  <script src="js/bootstrap.min.js"></script>
  <script type="text/javascript">
    function fillClustersDiv(){
      var requestData = {
      user : user
      }
  
      var request = $.ajax({
        type: "POST",
        url: "php/list_clusters.php",
        data: JSON.stringify(requestData),
        dataType: "json",
        success: function(data){
        //Create jQuery object from the response HTML.
        receivedJsonData = jQuery.parseJSON(data);
        var printing = ""; // Stuff going to be put inside contentDiv
      
        clusters = receivedJsonData.clusters; // Array
      
        for(var i = 0 ; i < clusters.length ; i++){
          if(clusters[i].user == user){
            printing += '';
          }
           
        }
        document.getElementById("clustersDiv").innerHTML = '<h1>AJAX</h1>';
      }
      });
    }
          
  </script>
  <script type="text/javascript">
    function sleep(milliseconds) {
      var start = new Date().getTime();
      for (var i = 0; i < 1e7; i++) {
        if ((new Date().getTime() - start) > milliseconds){
          break;
        }
      }
    }
  </script>
  <script type="text/javascript">
    var cambio=1;
    function cambiar(){
      if(cambio==1){
        sleep(2000);
        document.getElementById("prueba").innerHTML = "<h1>Cambiado</h1>";
        cambio=2;  
      }
      else{
        document.getElementById("prueba").innerHTML = "<h1>Original</h1>";
        cambio=1;
      //document.getElementById("prueba").innerHTML ="<h1>Original</h1>";
      
      //document.getElementById("prueba").innerHTML ="<span class='glyphicon glyphicon-refresh glyphicon-refresh-animate'></span>";
      }
    }
    cambiar();
  </script>
  <nav class="navbar navbar-default navbar fixed-top">
    <div class="container">
      <div id="header" class="text-center">
        <div class="navbar-header">
            <a href="#" class="navbar-brand">Hadoop</a>
        </div>
        <nav role="navigation" class="nav navbar-nav navbar-right">
          <li class="dropdown">
            <a data-toggle="dropdown" class="dropdown-toggle" href="#"><i class="fa fa-bars fa-2x"></i></a>
              <ul role="menu" class="dropdown-menu">
                <li><a href="#">Clusters</a></li>
                <li><a href="#">Firewall</a></li>
                <li><a href="#">Login Credentials</a></li>
                <li><a href="#">Help</a></li>
                <li><a href="#">Close</a></li>
                <li><a href="#">Exit(logout)</a></li>
            </ul>
          </li>
        </nav>
      </div>
    </div>
  </nav>
  <!--<div class="container">
  <table class="table table-striped table-bordered">
    <caption>Cluster Information</caption>
    <thead>
      <tr>
        <th>Number of nodes</th>
        <th>DFS Replicas</th>
        <th>DFS Block Size</th>
        <th>Reduce Task Number</th>
        <th>User</th>
      </tr>
   </thead>
   <tbody>
      <tr>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>

   </tbody>
  </table>
  </div> 
  <div class="panel-footer" id="footer">
        <p>AQUI HAY QUE COLOCAR TODO LO REFERENTE ACERCA DE INFORMACIÓN ADICIONAL</p>
  </div>
if(clusters[i].user == user){
            printing +='<div class="container">'
                      +'<table class="table table-striped table-bordered">'
                      +'<caption>Cluster Information</caption>'
                      +'<thead>'
                      +'<tr>'
                      +'<th>Number of nodes</th>'
                      +'<th>DFS Replicas</th>'
                      +'<th>DFS Block Size</th>'
                      +'<th>Reduce Task Number</th>'
                      +'<th>User</th>'
                      +'</tr>'
                      +'</thead>'
                      +'<tbody>'
                      +'<tr>'
                      +'<td></td>'
                      +'<td></td>'
                      +'<td></td>'
                      +'<td></td>'
                      +'<td></td>'
                      +'</tr>'
                      +'<tr>'
                      +'<td></td>'
                      +'<td></td>'
                      +'<td></td>'
                      +'<td></td>'
                      +'<td></td>'
                      +'</tr>'
                      +'<tr>'
                      +'<td></td>'
                      +'<td></td>'
                      +'<td></td>'
                      +'<td></td>'
                      +'<td></td>'
                      +'</tr>'
                      +'</tbody>'
                      +'</table>'
                      +'</div>'
                      +'<div class="panel-footer" id="footer">'
                      +'<p>AQUI HAY QUE COLOCAR TODO LO REFERENTE ACERCA DE INFORMACIÓN ADICIONAL</p>'
                      +'</div>';
-->
</body>
</html>

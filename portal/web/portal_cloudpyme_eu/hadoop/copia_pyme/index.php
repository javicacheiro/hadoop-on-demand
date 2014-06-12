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
    <title>CloudPyme || Portal</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="robots" content="noindex">
    <link rel="shortcut icon" href="http://portal.cloudpyme.eu/iconos/favicon.ico" />
    <!-- Google Fonts -->    
    <link href='http://fonts.googleapis.com/css?family=Open+Sans&subset=latin,cyrillic-ext' rel='stylesheet' type='text/css'>
    <!-- Bootstrap: CSS, JS, iconos -->
    <link rel="stylesheet" href="http://portal.cloudpyme.eu/css/bootstrap.css" >
    <link rel="stylesheet" href="http://portal.cloudpyme.eu/css/bootstrap-theme.css">
    <link rel="stylesheet" href="http://portal.cloudpyme.eu/iconos/font-awesome/css/font-awesome.css">
    <!-- <script src="http://portal.cloudpyme.eu/js/bootstrap.min.js"></script> -->
    <!-- Estilos genéricos -->
    <link rel="stylesheet" href="http://portal.cloudpyme.eu/css/style.css"> 
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script language="JavaScript">
     function KillMV(id)
     {
            var answer = confirm ("Esta seguro de querer eliminar la maquina con ID " + id);
            if (answer)
            {
                    document.datos.idmv.value=id;
                    //document.datos.action = "kill_mv.php";
                    document.datos.submit();
            }
     }
    </script>
              
</header>

//<?php
//        require_once "header.inc";
//?>





<?php

    print "<p>Hola $user : $passwd</p>";

    // FOOTER   
    require_once "footer.inc";

    //Actualiza cada 15 sec.
    if ($pending) {
	echo "<meta http-equiv=\"Refresh\" content=\"15\">\n";
    }
?>

</body>
</html>

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
	checkAuthorised("Eliminar MV");
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
    <link rel="stylesheet" href="bootstrap/bootstrap.css" >
    <link rel="stylesheet" href="bootstrap/bootstrap-theme.css">
    <link rel="stylesheet" href="http://portal.cloudpyme.eu/iconos/font-awesome/css/font-awesome.css">
   <!-- <script src="http://portal.cloudpyme.eu/js/bootstrap.min.js"></script>-->
    <!-- Estilos genéricos -->
    <link rel="stylesheet" href="bootstrap/style.css">
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

<script language="JavaScript">
function RegresarAuto()
{
   // regresa a la pagina anterior despues de 3 segundos.
        window.setTimeout("window.location.href = 'lista_mv.php';",4000);
}
</script>

</head>

<?php
	require_once "header.inc";
?>

<!-- CONTENT -->
<div id="container">
    
    
    <!-- COL LEFT -->
    <div id="col_left">  

         <!-- Panel Control -->       
        <div class="panel panel-info">
            <div class="panel-heading"> <i class="fa fa-cog fa-1x"></i> <?=get_vocab("panel_control");?></div>
                <div class="panel-body">
                   
                   <ul class="nav nav-pills nav-stacked">
                       <li><a href="lanzar_mv.php<?= '?'.$QUERY_STRING ;?>"><i class="fa fa-desktop fa-fw"></i> <?=get_vocab("lanzar_m_v");?></a></li>
                       <li class="active"><a href="lista_mv.php<?= '?'.$QUERY_STRING ;?>"><i class="fa fa-cogs fa-fw"></i> <?=get_vocab("listar_m_v");?></a></li>
                       <li><a href="#1"><i class="fa fa-cogs fa-fw"></i> <?=get_vocab("m_v_ejecucion");?></a></li>
                       <li><a href="#2"><i class="fa fa-bookmark fa-fw"></i> <?=get_vocab("m_v_ultimos_dias");?></a></li>
                       <li><a href="#3"><i class="fa fa-list fa-fw"></i> <?=get_vocab("listado_trabajos_cola");?></a></li>
                       <li><a href="#4"><i class="fa fa-barcode fa-fw"></i> <?=get_vocab("informacion_consumo");?></a></li>
                   </ul>

                </div> <!-- End Panel Body -->
        </div>
        <!-- End Panel Control -->
    </div> 
    <!-- END COL LEFT -->   
    
    
      <!-- COL WIDE -->
    <div id="col_wide">
        
        
        <div class="panel panel-info">
            <div class="panel-heading"><?=get_vocab("panel_eliminar_m_v");?></div>
               <div class="panel-body">
    
         
<?php
// Comprobamos que el usuario pertenezca a un proyecto
        require("xml2array.php");
        $contenido = file_get_contents($funciones->FILE_XML);
        if( $contenido == false){
                print "<div class=\"alert alert-warning\"><strong>ERROR:</strong>No es posible leer el fichero XML con la información de las solicitudes de CloudPyme dadas de alta.</div>";
                exit();
        }

        $solicitudes = xml2array($contenido,1,'attributes');

        if ( is_array($solicitudes['proyectos']['proyecto'][0]) ) {
                $proyectos = $solicitudes['proyectos']['proyecto'];
        } else {
                $proyectos[] = $solicitudes['proyectos']['proyecto'];
        }

	$solicitud = NULL;
        foreach ($proyectos as $proyecto) {
                unset($tmp);
                if ( is_array($proyecto['usuarios']['login']) ) {
                        $tmp=$proyecto['usuarios']['login'];
                } else {
                        $tmp[] = $proyecto['usuarios']['login'];
                }

		if (in_array($user, $tmp)) {
			$solicitud = $proyecto['attr']['nombre'];
			break;
		}

        }

	if ( !isset($solicitud) ) {
		
                print "<div class=\"alert alert-warning\"><strong>ERROR:</strong>No está asociado a ninguna solicitud de CloudPyme.</div>";
		exit;
	} else {
?>



<?php
		if (!empty($_POST)) {
			$maquina = $_POST['idmv'];

			// Obtenemos los datos de Consumo
			$chequeo = $funciones->Check_MV_User($maquina, $user, $solicitud);

			if ($chequeo) {
				$out = array();
				$salida = exec("/usr/bin/expect -c \"spawn su - ".$user." -c \\\"$KILL_MV $maquina; echo EXIT_STATUS=\\\$?\\\"; expect ssword; send ".$passwd."\\r; expect close;\"", $out, $return_var);

				#Eliminamos las 2 primeras lineas de la salida pues son los comandos ejecutados
				array_shift($out);
				array_shift($out);

				if(stristr($salida, 'exit') === FALSE) {
					$salida=implode('<br>', $out);
				}
			
				$return_var = 0;
				$temp = stristr($salida, 'exit_status');
				if(!$temp === FALSE) {
					$return_var = array_pop(explode('=',$temp));
				}
				//echo "<p>return_var = $return_var";

				if ($return_var >0 ) {
					print "<div class=\"alert alert-warning\"><strong>ERROR: </strong>No ha sido posible eliminar la m&aacute;quina virtual con ID $maquina.Error:<br>$salida.</div>";
					exit;
				} else {
                                        print "<div class=\"alert alert-success\"><strong>CORRECTO: </strong>".get_vocab("msg_exito_eliminar_maquina").$maquina."</div>";
				}
			} else {
				print "<div class=\"alert alert-warning\"><strong>ERROR: </strong>La maquina con ID $maquina que quiere eliminar no es suya y por lo tanto no la puede eliminar o bien no existe.</div>";
			}
		} else {
			print "<div class=\"alert alert-warning\"><strong>ERROR: </strong>Es necesario especificar la maquina que se quiere eliminar</div>";
		}
	}
?>

                    </div> <!-- End Panel Body -->
                 </div> <!-- End Panel Info -->
     
     </div> <!-- END COL-WIDE -->

</div><!-- END CONTENT -->


<script language="Javascript">
	RegresarAuto();    
</script>


<?php
        require_once "footer.inc";
?>

</body>
</html>

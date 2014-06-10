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
        checkAuthorised("Lanzar M&aacute;quina Virtual");
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
    <link href="http://portal.cloudpyme.eu/iconos/favicon.ico" rel="shortcut icon"type="image/x-icon" />
    <!-- Google Fonts -->    
    <link href='http://fonts.googleapis.com/css?family=Open+Sans&subset=latin,cyrillic-ext' rel='stylesheet' type='text/css'>
    <!-- Bootstrap: CSS, JS, iconos -->
    <link rel="stylesheet" href="bootstrap/bootstrap.css" >
    <link rel="stylesheet" href="bootstrap/bootstrap-theme.css">
    <link rel="stylesheet" href="http://portal.cloudpyme.eu/iconos/font-awesome/css/font-awesome.css">
    <!-- <script src="http://portal.cloudpyme.eu/js/bootstrap.min.js"></script> -->
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

<?
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
                       <li class="active"><a href="lanzar_mv.php<?= '?'.$QUERY_STRING ;?>"><i class="fa fa-desktop fa-fw"></i> <?=get_vocab("lanzar_m_v");?></a></li>
                       <li><a href="lista_mv.php<?= '?'.$QUERY_STRING ;?>"><i class="fa fa-cogs fa-fw"></i> <?=get_vocab("listar_m_v");?></a></li>
                       <li><a href="lista_mv.php#1"><i class="fa fa-cogs fa-fw"></i> <?=get_vocab("m_v_ejecucion");?></a></li>
                       <li><a href="lista_mv.php#2"><i class="fa fa-bookmark fa-fw"></i> <?=get_vocab("m_v_ultimos_dias");?></a></li>
                       <li><a href="lista_mv.php#3"><i class="fa fa-list fa-fw"></i> <?=get_vocab("listado_trabajos_cola");?></a></li>
                       <li><a href="lista_mv.php#4"><i class="fa fa-barcode fa-fw"></i> <?=get_vocab("informacion_consumo");?></a></li>
                   </ul>

                </div> <!-- End Panel Body -->
        </div>
        <!-- End Panel Control -->
    </div> 
    <!-- END COL LEFT -->       
        
    
    
     <!-- COL WIDE -->
    <div id="col_wide">


<!-- Lanzar Máquina Virtual -->  
      <div class="panel panel-info">
         <div class="panel-heading"><?=get_vocab("panel_lanzar_m_v");?></div>
             <div class="panel-body">
       

<?php
// Comprobamos que el usuario pertenezca a un proyecto
        require("xml2array.php");
        $contenido = file_get_contents($funciones->FILE_XML);
        if( $contenido == false){
                print "<div class=\"alert alert-warning\"><strong>ERROR:</strong>No es posible leer el fichero XML con la informaci&oacute;n de las solicitudes de CloudPyme dadas de alta</div>";
               // echo "<p class='error'>No es posible leer el fichero XML con la informaci&oacute;n de las solicitudes de CloudPyme dadas de alta</p>";
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
			$horas_pri=$proyecto['nhoras_pri'];
			$horas_npri=$proyecto['nhoras_npri'];
			break;
		}
        }

	if ( !isset($solicitud) ) {
                print "<div class=\"alert alert-warning\"><strong>ERROR:</strong>No está asociado a ninguna solicitud de CloudPyme</div>";
		exit;
	} else {
?>
      


<?php
		// Obtenemos los datos de Consumo
        	$ahora = time();
	        $accounting = $funciones->getConsumo($ahora, $proyectos, $solicitud);

		$consumo = 0;
		if($accounting[$solicitud]['total'])
			$consumo = $accounting[$solicitud]['total'];

		if ($consumo >= ($horas_pri + $horas_npri)*3600) {
			print "<div class=\"alert alert-warning\"><strong>ALERTA:</strong>Ha consumido todas las horas concedidas por lo que no se pueden lanzar nuevos trabajos.</div>";
                        exit;
		}


		// Los usuarios DEMO solo pueden tener una maquina en ejecucion
		if (!stristr($user, 'cpdem') === FALSE) {
			$nmaq = 0;
			$data = $funciones->MV_List($user, $solicitud);
			if (is_array($data)) {
				$nmaq = count($data);
			}

			if ($nmaq > 0) {
				print "<div class=\"alert alert-warning\"><strong>ERROR:</strong>Los usuarios DEMO solo pueden tener una m&aacute;quina en ejecuci&oacute;n</div>";
        			require_once "footer.inc";
				exit;
			}
		}

                // Procesamos datos recibidos del formulario Lanzar Máquina
		if (!empty($_POST)) {
			$maquina = $_POST['template'];
			$tiempo = $_POST['maxtime'];

			$tjob = $templates[$maquina]['proc']*$tiempo*3600;
			if ($consumo + $tjob >= ($horas_pri + $horas_npri)*3600) {
                            print "<div class=\"alert alert-warning\"><strong>ALERTA:</strong>En estos momentos no puede enviar una m&aacute;quina con los recursos requeridos porque entre las m&aacute;quinas que ya han finalizado y las m&aacute;quinas en ejecuci&oacute;n asociadas a la solicitud $solicitud suman un tiempo estimado de ".$funciones->convSeg2($consumo)." por lo que sumado al tiempo (tiempo * procesadores) que solicita en esta m&aacute;quina (".$funciones->convSeg2($tjob)."), se sobrepasarian las horas que le fueron asignadas (".$funciones->convSeg2(($horas_pri + $horas_npri)*3600).").\n\nPor lo tanto, debera solicitar menos tiempo excepto que elimine alguna m&aacute;quina de las que tiene en ejecuci&oacute;n.</div>";
                            exit;
			}

			$out = array();

			$salida = exec("/usr/bin/expect -c \"spawn su - ".$user." -c \\\"$LANZA_MV $maquina $passwd $tiempo; echo EXIT_STATUS=\\\$?\\\"; expect ssword; send ".$passwd."\\r; expect close;\"", $out, $return_var);
			#print_r($out);

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
				print "<div class=\"alert alert-danger\"><strong>ERROR: </strong>".get_vocab("msg_error_lanzar_maquina").$salida."</div>";
				exit;
			} else {
				print "<div class=\"alert alert-success\"><strong>CORRECTO: </strong>".get_vocab("msg_exito_lanzar_maquina")."</div>";
			}
?>
	<script language="Javascript">
	<!--
	        RegresarAuto();    
	// -->
	</script>

<?php
		} else {
?>
	<form class="form-horizontal" role="form" method="post" action="<?php echo htmlentities($_SERVER['PHP_SELF']); ?>">
            
                 <div class="form-group">
                      <label class="col-sm-2 control-label"><?=get_vocab("tipo_maquina");?></label>
                            <div class="col-sm-4">
                                <select class="form-control" name="template">
                                              
            		
<?php
			// Los usuarios DEMO solo pueden enviar maquinas de 1proc y 2Gb
			if (stristr($user, 'cpdem') === FALSE) {
				foreach ($templates as $key => $value) {
					echo "  <option value='$key'>".$value['proc']." proc - ".$value['mem']."Gb</option>\n";
				}
			} else {
				$temp=array_keys($templates);
				$key=$temp[0];
				$temp=array_values($templates);
				$value=$temp[0];
				echo "  <option value='$key'>".$value['proc']." proc - ".$value['mem']."Gb</option>\n";
			}
 
//			<option value='single_1P_2G'>1 proc - 2Gb</option>
//			<option value='single_2P_4G'>2 proc - 4Gb</option>
//			<option value='single_4P_4G'>4 proc - 4Gb</option>
?>

                                        
                               </select>                             
                        </div>
                 </div>
                                        
	  	  
                <div class="form-group">
                    <label class="col-sm-2 control-label"><?=get_vocab("tiempo_max_vida");?></label>
                       <div class="col-sm-4">
                        <select class="form-control" name="maxtime">
			<option value='4' selected>4 <?=get_vocab("horas");?></option>
			<option value='8'>8 <?=get_vocab("horas");?></option>
			<option value='12'>12 <?=get_vocab("horas");?></option>
			<option value='24'>24 <?=get_vocab("horas");?></option>
			<option value='36'>36 <?=get_vocab("horas");?></option>
			<option value='48'>48 <?=get_vocab("horas");?></option>
			<option value='72'>72 <?=get_vocab("horas");?></option>
			<option value='100'>100 <?=get_vocab("horas");?></option>
<?php			if (stristr($user, 'cpdem') === FALSE) {	?>
				<option value='200'>200 <?=get_vocab("horas");?></option>
<?php			}		?>
			</select>
                           
		          <br/>
                         <button type="submit" class="btn btn-primary" name="submit" value="Lanzar Maquina"><?=get_vocab("btn_lanzar_maquina");?></button>
                        </div>
                   </div>
	</form>
<?php
		}
	}

?>


             </div> <!-- Cierro Panel -->
        
       </div> <!-- END COL-WIDE -->

</div><!-- END CONTENT -->
        
        

<?php
     require_once "footer.inc";
?>

</body>
</html>

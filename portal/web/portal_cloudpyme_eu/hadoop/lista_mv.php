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
    <!-- <script src="http://portal.cloudpyme.eu/js/bootstrap.min.js"></script> -->
    <!-- Estilos genéricos -->
    <link rel="stylesheet" href="bootstrap/style.css"> 
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

<?php
        require_once "header.inc";
?>


<!-- CONTENT -->
<div id="container">
     
 
<?php
// Comprobamos que el usuario pertenezca a un proyecto
        require("xml2array.php");
        $contenido = file_get_contents($funciones->FILE_XML);
        if( $contenido == false){
                echo "No es posible leer el fichero XML con la informaci&oacute;n de las solicitudes de CloudPyme dadas de alta";
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

        
	if (!isset($solicitud)) {
                print "<h2 class='error'>NO esta asociado a ninguna solicitud de CloudPyme</h2>";
		exit;
	} else {
?>   

<?php
	}

	// IDs de las maquinas confirmadas en ejecucion
	$mv_run = array();
	if ($handle = opendir($RUN_MV_DIR)) {
		while (false !== ($entry = readdir($handle))) {
			if ($entry != "." && $entry != "..") { // && is_file($entry)) {
				//echo "$entry\n";
				list($idmv, $login) = explode('@', $entry);
				if ($login == $user) 
					$mv_run[] = $idmv;
			}
		}
		closedir($handle);
	}
  ?>  
        
        
        
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
            <div class="panel-heading"><?=get_vocab("panel_listar_m_v");?></div>
        </div>
    
                
        <?php
	//Hora dependiendo del timezone correspondiente al idioma
        global $idioma;
        $ahora=time();
	$temp=date_default_timezone_get();
	if (in_array($idioma, $lang_support)) {
		date_default_timezone_set($lang_timezone[$idioma]);
	} else {
		date_default_timezone_set($lang_timezone[$default_language_tokens]);
	}	
        
        if ($idioma == 'en'){
           $format_now = date('m/d/Y H:i:s T', $ahora);
        } else {
            $format_now = date('d/m/Y H:i:s T', $ahora);
        }
	date_default_timezone_set($temp);
        
        ?>
        <!-- Última Actualización -->
        <ol class="breadcrumb" style="text-align:right">
             <li><span class="label label-primary"><?=get_vocab("ultima_actualizacion");?></span> : <?=$format_now;?></li>
        </ol>
        <!-- -->
        

        
        
        <!-- Lista de Máquinas Virtuales -->
        <h2><i class="fa fa-cogs fa-2x"></i><a name="1"> <?=get_vocab("m_v_ejecucion");?></a></h2>
 <?php       
// Lista de MV en ejecucion
	$pending = 0;
	$data = $funciones->MV_List($user, $solicitud);
        if (!is_array($data)) {
              //echo "<h3>&nbsp;".$data."</h3>";
               print "<div class=\"well well-sm\">".$data."</div>";
//            exit;
        } else {
                print "<div class=\"panel panel-default\" style=\"margin-bottom:3em\">";
        	print "<table class='table'>\n";
	        print "<tr><th>".get_vocab('th_id')."</th><th>".get_vocab('th_host')."</th><th>".get_vocab('th_inicio')."</th><th>".get_vocab('th_tipo')."</th><th>".get_vocab('th_tiempo')."</th><th>".get_vocab('th_estado')."</th><th>".get_vocab('th_acceder')."</th><th>".get_vocab('th_acciones')."</th></tr>\n";

        	foreach ($data as $mv) {
			$temp=explode('.',$mv['IP']);
			$hostname = 'node'.$temp[3];
			//$estado = in_array($mv['id_vm'], $mv_run) ? 'RUN' : 'PEND';
			if (in_array($mv['id_vm'], $mv_run)) {
				$estado = 'RUN';

				print " <tr>
					<td>".$mv['id_vm']."</td>
					<td>".$hostname."</td>
					<td>".date('d/m/Y H:i:s O',$mv['ini'])."</td>
					<td>".$mv['tipo']."</td>
					<td>".$mv['tiempo']." horas</td>
					<td>".$estado."</td>
					<td align=\"center\"><a target='_blank' href='http://".$mv['IP'].":5801/novnc.html?token=".$mv['token']."'><i class=\"fa fa-desktop fa-2x\"></i></a></td>
					<td align=\"center\"><a href='javascript:KillMV(\"".$mv['id_vm']."\");'><i class=\"fa fa-times-circle fa-2x\"></i> ".get_vocab('eliminar')."</a></td>
					</tr>\n";
			} else {
				$estado = 'PEND';
				$pending += 1;

				print " <tr>
					<td>".$mv['id_vm']."</td>
					<td>".$hostname."</td>
					<td>".date('d/m/Y H:i:s O',$mv['ini'])."</td>
					<td>".$mv['tipo']."</td>
					<td>".$mv['tiempo']." horas</td>
					<td>".$estado."</td>
					<td align=\"center\">--</td>
					<td align=\"center\"><a href='javascript:KillMV(\"".$mv['id_vm']."\");'><i class=\"fa fa-times-circle fa-2x\"></i> Eliminar</a></td>
					</tr>\n";
			}
        	}

	        print "</table>\n";
                print "</div>";
         }
  ?>  
          

   
            
        <?php $DIAS=7; ?>          
        <!-- Máquinas Virtuales finalizadas últimos 7 días -->    
        <h2><i class="fa fa-bookmark fa-2x"></i><a name="2"> <?=get_vocab("m_v_ultimos_dias");?></a></h2>
  <?php
// Lista de MV finalizadas en los ultimos N dias
        $data = $funciones->MV_List($user, $solicitud, $DIAS);
        if (!is_array($data)) {
            
                // echo "<h3>&nbsp;".$data."</h3>";
                print "<div class=\"well well-sm\">".$data."</div>";
       //        exit;
                
        } else {
                print "<div class=\"panel panel-default\" style=\"margin-bottom:3em\">";
	        print "<table class='table'>\n";
        	print "  <tr><th>".get_vocab('th_id')."</th><th>".get_vocab('th_host')."</th><th>".get_vocab('th_inicio')."</th><th>".get_vocab('th_fin')."</th><th>".get_vocab('th_tipo')."</th></tr>\n";

	        foreach ($data as $mv) {
			$temp=explode('.',$mv['IP']);
			$hostname = 'node'.$temp[3];
        	        print " <tr>
	        	        <td>".$mv['id_vm']."</td>
				<td>".$hostname."</td>
        	        	<td>".date('d/m/Y H:i:s O',$mv['ini'])."</td>
				<td>".date('d/m/Y H:i:s O',$mv['fin'])."</td>
		                <td>".$mv['tipo']."</td>
        		        </tr>\n";
	        }
        	print "</table>\n";
                print "</div>";
	}
?>
            
            
               
        <!-- Lista Trabajos encolados -->
        <h2><i class="fa fa-list fa-2x"></i><a name="3"> <?=get_vocab("listado_trabajos_cola");?></a></h2>
<?php
// Obtenemos la informacion de accounting de los trabajos en cola
	$out = array();
	$salida = exec(". /etc/profile;". $GET_JOBS." $solicitud 2>&1; echo EXIT_STATUS=\$?", $out, $return_var);

	if(stristr($salida, 'exit') === FALSE) {
		$salida=implode('<br>', $out);
	}

	$return_var = 0;
	$temp = stristr($salida, 'exit_status');
	if(!$temp === FALSE) {
		$return_var = array_pop(explode('=',$temp));
	}
	#echo "<p>return_var = $return_var<p>";

      
	if ($return_var == 0 ) {
		array_pop($out);
		$jobs = array();
		for ($i=0; $i < count($out); $i++) {
			if (stristr($out[$i], $user)) {
				$jobs[]=explode('|',$out[$i]);
			}
		}

		if (count($jobs) == 0) {
			//echo "<h3> ".get_vocab('no_trabajos_cola')."</h3>";
                        print "<div class=\"well well-sm\">".get_vocab('no_trabajos_cola')."</div>";
                     
		} else{
                        print "<div class=\"panel panel-default\" style=\"margin-bottom:3em\">";
			print "<table class='table'>\n";
			print "  <tr><th>".get_vocab('th_jobid')."</th><th>".get_vocab('th_procesadores_solicitados')."</th><th>".get_vocab('th_tiempo_solicitado')."</th><th>".get_vocab('th_memoria_total')."</th><th>".get_vocab('th_tareas')."</th><th>".get_vocab('th_estado')."</th><th>".get_vocab('th_consumo_estimado')."</th></tr>\n";
			for ($i=0; $i < count($jobs); $i++) {
				print "  <tr>
                                <td>".$jobs[$i][0]."</td>
                                <td>".($jobs[$i][3]*$jobs[$i][4])."</td>
                                <td>".$funciones->convSeg2($jobs[$i][5])."</td>
                                <td>".($jobs[$i][6]*$jobs[$i][4])."GB</td>
                                <td>".$jobs[$i][7]."</td>
                                <td>".$jobs[$i][8]."</td>
                                <td>".$funciones->convSeg2($jobs[$i][9])."</td>
                                </tr>\n";
			}
		
			print "</table>\n";
			print "</div>";
		}
	} else {
		// echo "<h3>".$salida."</h3>";
                 print "<div class=\"alert alert-warning\">.$salida.</div>";
                // exit;
	}
?>      
     
             
             
             

        <!-- Información de Consumo -->
        <h2><i class="fa fa-barcode fa-2x"></i> <a name="4"> <?=get_vocab("informacion_consumo");?></a></h2>
        <div class="panel panel-default" style="margin-bottom:3em">    
<?php             
// Obtenemos los datos de Consumo   
        $ahora = time();
        $accounting = $funciones->getConsumo($ahora, $proyectos, $solicitud);

        print "<table class='table'\n";
        print "<tr><th>".get_vocab('th_horas_pri')."</th><th>".get_vocab('th_horas_npri')."</th><th>".get_vocab('th_consumo_diario')."</th><th>".get_vocab('th_consumo_total')."</th></tr>\n";
	print "<tr>
		<td>$horas_pri</td>
		<td>$horas_npri</td>";

        if($accounting[$solicitud][$user])
                print "<td>".$funciones->convSeg2($accounting[$solicitud][$user])."</td>";
        else
                print "<td>0</td>";
	
	if($accounting[$solicitud]['total'])
		print "<td>".$funciones->convSeg2($accounting[$solicitud]['total'])."</td>";
	else
		print "<td>0</td>";

	print "<tr>\n";
	print "</table>\n";
?>
            
        </div>    


        
        </div> <!-- END COL-WIDE -->

</div><!-- END CONTENT -->



<form name="datos" action="kill_mv.php" method="post">
<input type="hidden" value="" name="idmv">
</form>



<?php
    // FOOTER   
    require_once "footer.inc";

    //Actualiza cada 15 sec.
    if ($pending) {
	echo "<meta http-equiv=\"Refresh\" content=\"15\">\n";
    }
?>

</body>
</html>

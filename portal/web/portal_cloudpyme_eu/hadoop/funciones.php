<?

class funciones {
        var $webmaster = "prey@cesga.es";

	var $server = "localhost";
	var $db_user = "cpyme_adm";
	var $db_pass = "cpyme.12";
	var $database = "CloudPyme";

	var $FILE_XML = '/COMPARTIDO/solicitudes/cpyme/cpyme_SVG.xml';
	var $FILE_ACCT = '/COMPARTIDO/solicitudes/cpyme/cpyme_acct_user_SVG.dat';
	var $FILE_CLOUD_DATE = '/COMPARTIDO/solicitudes/cpyme/cloudLastDate.info';
	var $FILE_JOBS_DATE = '/COMPARTIDO/solicitudes/cpyme/jobsLastDate.info';

	var $EPILOG_DIR = '/opt/cesga/sistemas/sge/tools/util';
	var $EPILOG_FILE_CPYME = 'cpyme_finish.job';

// funcion que comprueba que una maquina pertenece a un usuario y solicitud
        function Check_MV_User($id, $login, $proyecto) {
		if (!$id || !$login || !$proyecto) {
			return "Algunos de los campos no se han introducido.";
		}
                // Connect to database server
                $hd = mysql_connect($this->server, $this->db_user, $this->db_pass) or die ("No pudo conectarse a la BD: " . mysql_error());

                // Select database
                mysql_select_db ($this->database, $hd) or die ("No ha sido posible seleccionar la BD: " . mysql_error());

                // Execute query
                $sql = "SELECT count(*) as info FROM instancias WHERE login='$login' AND proyecto='$proyecto' AND id_vm = $id ";
                //echo "<p>$sql<p>";
                $res = mysql_query($sql, $hd) or die("0");

		$fila = mysql_fetch_assoc($res);
		mysql_free_result($res);
		mysql_close($hd);
		return $fila['info'];
	}

// funcion que devuelve la lista de MV corriendo o finalizadas en los ultimos N dias
	function MV_List ($login, $proyecto, $dias=0) {
		if (!$login || !$proyecto) {
                        return "Algunos de los campos no se han introducido.";
		}

		// Connect to database server
		$hd = mysql_connect($this->server, $this->db_user, $this->db_pass) or die ("No pudo conectarse a la BD: " . mysql_error());

		// Select database
		mysql_select_db ($this->database, $hd) or die ("No ha sido posible seleccionar la BD: " . mysql_error());

		// Execute query
		$sql = "SELECT * FROM instancias WHERE login='$login' AND proyecto='$proyecto' AND ";

		if ($dias > 0) {
			//Esto tendria en cuenta la hora actual
			//$limit = time() - ($dias * 24 * 60 * 60);
			//Asi nos quedamos a las 00:00 de $dias dias antes
			$limit  = mktime(0, 0, 0, date("m"), date("d")-$dias, date("Y"));
			$sql.="fin > $limit order by fin desc";
			$txt_error = "No hay m&aacute;quinas finalizadas en los ultimos $dias dias.";
		} else {
			$sql.='fin IS NULL order by ini desc';
			$txt_error = "No hay m&aacute;quinas en ejecuci&oacute;n.";
		}
		//echo "<p>$sql<p>";
		$res = mysql_query($sql, $hd) or die ("No pudo ejecutarse satisfactoriamente la consulta: " . mysql_error());

                // Query number of rows in rowset
                $numrows = mysql_num_rows($res);
                if ($numrows < 1) {
                        mysql_free_result($res);
                        mysql_close($hd);
                        return $txt_error;
                } else {
                        while(($ar[] = mysql_fetch_assoc($res)) || array_pop($ar));

                        mysql_free_result($res);
                        mysql_close($hd);
                        return $ar;
                }
	}

// acct_epilog_CPYME: Procesa los ficheros creados por epilog del SGE para obtener
//el accounting de los trabajos de CPYME finalizados y que no estan aun en la
//base de datos de accounting
	function acct_epilog_CPYME() {
		$acct_fin = array();
		$files = array();

                if ($handle = opendir($this->EPILOG_DIR)) {
                        while (false !== ($entry = readdir($handle))) {
                                if ($entry != "." && $entry != ".." && eregi("$this->EPILOG_FILE_CPYME*", $entry)) {
                                        //echo "$entry\n";
                                        $files[] = $entry;
                                }
                        }
                        closedir($handle);
                }

                #Procesamos cada uno de los ficheros
                foreach ($files as $file) {
                        //print "fichero: $file\n";
                        // Get a file into an array.
                        //$lines = file($this->FILE_ACCT);
                        // Using the optional flags parameter since PHP 5
                        $lines = file($this->EPILOG_DIR.'/'.$file, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
                        foreach ($lines as $line) {
				//echo "linea: $line<p>";
                                list($cpyme, $consumo, $udata) = explode('=', $line);
                                $acct_fin[$cpyme][$udata]+=$consumo;
                                $acct_fin[$cpyme]['total']+=$consumo;
                        }
                }

                return $acct_fin;
	}



// Funcion que devuelve el tiempo consumido de todos los proyectos y usuarios
	function getConsumo($ahora, $proyectos, $solicitud = '') {
		if (!$ahora || !$proyectos) {
                        return "Algunos de los campos no se han introducido.";
                }
	
	// Obtenemos los datos del fichero de accounting
		// Get a file into an array.
		//$lines = file($this->FILE_ACCT);
		// Using the optional flags parameter since PHP 5
		$lines = file($this->FILE_ACCT, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
		foreach ($lines as $line) {
			list($cpyme, $udata) = explode(' => ', $line, 2);
			$udata=str_replace(array('{','},','}',' '),'',$udata);
			$udata2=explode(',', $udata);
			$accounting[$cpyme]['total']=0;
			foreach ($udata2 as $data){
				list($user, $acct) = explode('=>', $data);
				$accounting[$cpyme][$user]=$acct;
				$accounting[$cpyme]['total']+=$acct;
			}
		}

	// Obtenemos la informacion de accounting generada por el script del epilog
		$acct_fin = $this->acct_epilog_CPYME();

		foreach ($acct_fin as $cpyme => $values) {
			foreach ($values as $user => $acct) {
				$accounting[$cpyme][$user]+=$acct;
				// $accounting[$cpyme]['total']+=$acct; // NO HACE FALTA PUES HAY UN user=total
			}
		}		

	// Obtenemos la informacion de accounting de los trabajos en cola
		$acct_queue = array();
		foreach ($proyectos as $proyecto) {
			$pr_nombre = $proyecto['attr']['nombre'];
			if (!empty($solicitud) && ($pr_nombre != $solicitud)) {
				continue;
			}

			$out = array();
			$salida = exec(". /etc/profile;". $GLOBALS['GET_JOBS']." $pr_nombre 2>&1; echo EXIT_STATUS=\$?", $out, $return_var);

                        if(stristr($salida, 'exit') === FALSE) {
                                $salida=implode('<br>', $out);
                        }

                        $return_var = 0;
                        $temp = stristr($salida, 'exit_status');
                        if(!$temp === FALSE) {
                                $return_var = array_pop(explode('=',$temp));
                        }
                        //echo "<p>return_var = $return_var<p>";

                        if ($return_var == 0 ) {
				array_pop($out);
				for ($i=0; $i < count($out); $i++) {
					$dat=explode('|',$out[$i]);
					$acct_queue[$dat[2]][$dat[1]]+=$dat[9];
					$acct_queue[$dat[2]]['total']+=$dat[9];
				}
                        }
		}

                foreach ($acct_queue as $cpyme => $values) {
                        foreach ($values as $user => $acct) {
                                $accounting[$cpyme][$user]+=$acct;
                                // $accounting[$cpyme]['total']+=$acct; // NO HACE FALTA PUES HAY UN user=total
                        }
                }

	// Obtenemos la fecha de la ultima entrada en la BBDD de accounting de Cloud
		$lines = file($this->FILE_CLOUD_DATE, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
		//echo "<p>FILE_CLOUD_DATE => ".$lines[0]." -- ".date('d/m/Y H:i:s O',$lines[0]);
		$fecha_limit=$lines[0];

	// Consumo de los trabajos en ejecucion
                // Connect to database server
                $hd = mysql_connect($this->server, $this->db_user, $this->db_pass) or die ("No pudo conectarse a la BD: " . mysql_error());

                // Select database
                mysql_select_db ($this->database, $hd) or die ("No ha sido posible seleccionar la BD: " . mysql_error());

                // Execute query
//                $sql = "SELECT proyecto, login, SUM(($ahora - GREATEST(ini,$fecha_limit))*ncpu*maquinas) AS suma FROM instancias WHERE fin IS NULL GROUP BY 1,2";
		$sql = "SELECT proyecto, login, SUM((GREATEST($ahora, ini + tiempo*3600) - GREATEST(ini,$fecha_limit))*ncpu*maquinas) AS suma FROM instancias WHERE fin IS NULL GROUP BY 1,2";
		//echo "<P>$sql<P>";
                $res = mysql_query($sql, $hd) or die ("No pudo ejecutarse satisfactoriamente la consulta: " . mysql_error());

                // Query number of rows in rowset
                $numrows = mysql_num_rows($res);
                if ($numrows > 0) {
			while ($fila = mysql_fetch_assoc($res)) {
				$accounting[$fila['proyecto']][$fila['login']]+=$fila['suma'];
				$accounting[$fila['proyecto']]['total']+=$fila['suma'];
			}
		}
		mysql_free_result($res);

        // Consumo de los trabajos finalizados despues de la fecha del fichero FILE_CLOUD_DATE
                $sql = "SELECT proyecto, login, SUM((fin - GREATEST(ini,$fecha_limit))*ncpu*maquinas) AS suma FROM instancias WHERE fin > $fecha_limit GROUP BY 1,2";
                //echo "<P>$sql<P>";
                $res = mysql_query($sql, $hd) or die ("No pudo ejecutarse satisfactoriamente la consulta: " . mysql_error());

                // Query number of rows in rowset
                $numrows = mysql_num_rows($res);
                if ($numrows > 0) {
                        while ($fila = mysql_fetch_assoc($res)) {
                                $accounting[$fila['proyecto']][$fila['login']]+=$fila['suma'];
                                $accounting[$fila['proyecto']]['total']+=$fila['suma'];
                        }
                }
                mysql_free_result($res);
		mysql_close($hd);

		return $accounting;
	}






// Funcion que convierte segundos en un intervalo (dias horas min seg)
        function convSeg ($seg) {
                $dat = array("d-", ":", ":", "");
                $op = array(86400, 3600, 60, 1);

                for($i=0; $i<4; $i++) {
                        $val[$i] = (int) ($seg / $op[$i]);
                        // El operador % solo funciona bien con enteros. Para floats hay
                        // la funcion fmod pero a partir de la version 4.2 de php
                        //$seg = $seg % $op[$i];
                        $seg = $seg - $val[$i]*$op[$i];
                }

                $ifor = $val[0] . $dat[0];
                for($j=1; $j<4; $j++)
                        $ifor = $ifor . sprintf("%02d", $val[$j]) . $dat[$j];

                return $ifor;
        }

// Funcion que convierte segundos en un intervalo (horas min seg)
        function convSeg2 ($seg) {
                $dat = array(":", ":", "");
                $op = array(3600, 60, 1);

                for($i=0; $i<3; $i++) {
                        $val[$i] = (int) ($seg / $op[$i]);
                        // El operador % solo funciona bien con enteros. Para floats hay
                        // la funcion fmod pero a partir de la version 4.2 de php
                        //$seg = $seg % $op[$i];
                        $seg = $seg - $val[$i]*$op[$i];
                }

                $ifor = $val[0] . $dat[0];
                for($j=1; $j<3; $j++)
                        $ifor = $ifor . sprintf("%02d", $val[$j]) . $dat[$j];

                return $ifor;
        }

//////////////////////////////////////////
}//FIn clase funciones
$funciones = new funciones;

?>

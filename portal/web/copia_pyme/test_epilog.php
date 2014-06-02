<?
        header("Expires: Mon, 26 Jul 1997 05:00:00 GMT"); // Date in the past
        header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT"); // always modified
        header("Cache-Control: no-cache, must-revalidate"); // HTTP/1.1
        header("Pragma: no-cache");


// acct_epilog_CPYME: Procesa los ficheros creados por epilog del SGE para obtener
//el accounting de los trabajos de CPYME finalizados y que no estan aun en la
//base de datos de accounting
        function acct_epilog_CPYME() {
	        $EPILOG_DIR = '/opt/cesga/sistemas/sge/tools/util';
        	$EPILOG_FILE_CPYME = 'cpyme_finish.job';

                $acct_fin = array();
                $files = array();

		if ($handle = opendir($EPILOG_DIR)) {
			while (false !== ($entry = readdir($handle))) {
				if ($entry != "." && $entry != ".." && eregi("$EPILOG_FILE_CPYME*", $entry)) {
					echo "$entry\n";
					$files[] = $entry;
				}
			}
			closedir($handle);
		}

                #Procesamos cada uno de los ficheros
                foreach ($files as $file) {
                        print "fichero: $file\n";
                	// Get a file into an array.
	                //$lines = file($this->FILE_ACCT);
        	        // Using the optional flags parameter since PHP 5
                	$lines = file($EPILOG_DIR.'/'.$file, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
			foreach ($lines as $line) {
				list($cpyme, $consumo, $udata) = explode('=', $line);
				$acct_fin[$cpyme][$udata]+=$consumo;
				$acct_fin[$cpyme]['total']+=$consumo;
			}
                }

                return $acct_fin;
        }



        require_once "grab_globals.inc.php";
        require_once "config_auth.inc";
        require_once "functions.inc";
        require_once "mrbs_auth.inc";

        // Check the user is authorised for this page
        checkAuthorised("TEST_EPILOG");
        $user = getUserName();

	require("config_var.inc");

	require("funciones.php");

?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<HEAD>
        <TITLE>CloudPyme.- TEST_EPILOG</TITLE>
        <LINK TYPE="text/css" REL="stylesheet" HREF="./styles/style.css">
        <link rel="stylesheet" href="./styles/main.css" media="screen">
        <link rel="stylesheet" href="./styles/colors.css" media="screen">
	<meta name="robots" content="noindex" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</HEAD>

<body bgcolor="#ffffff" text="#000000" link="#0000ff" vlink="#800080" alink="#ff0000">

<center>
<table  width="600">
        <tr>
                <td align='center'><h1>TEST EPILOG</H1></td>
        </tr>
</table>
<P>

<table width="600" COLS=2 border=0>
        <tr bgcolor="#c0c0c0">
                <td class="tablan" colspan=2>Datos del usuario:</td>
        </tr>
        <tr>
                <td class="tablan" bgcolor="#e0e0e0" width='100'>Login: </td>
                <TD class="login">&nbsp;&nbsp;<?=strtolower($user);?></TD>
        </tr>
</table>
</center>
<p>

<?
	$ahora = time();
	$accounting = acct_epilog_CPYME();
echo "<p>";
print_r($accounting);
echo "<p>";

// Lista de solicitudes
	require("xml2array.php");
	$contenido = file_get_contents($funciones->FILE_XML);
	if( $contenido == false){ 
		echo "<p class='error'>No es posible leer el fichero XML con la informaci&oacute;n de las solicitudes de CloudPyme dadas de alta</p>";
		exit();
	}

	$solicitudes = xml2array($contenido,1,'attributes');
	//print_r($solicitudes);

	if ( is_array($solicitudes[proyectos][proyecto][0]) ) {
		$proyectos = $solicitudes[proyectos][proyecto];
	} else {
		$proyectos[] = $solicitudes[proyectos][proyecto];
	}

	print "<div class='texto2'>N&uacute;mero de solicitudes listadas: <b>" . count($proyectos) . "</b></div><P>\n";
        print "<center>";
        print "<TABLE BORDER=1 width='800' CELLSPACING=0>\n";
        print "  <TR><TH>Nombre</TH><TH>Inicio</TH><TH>Fin</TH><TH>Convocatoria</TH><TH>Usuarios</TH><TH>Horas Pri</TH><TH>Horas NPri</TH><TH>Consumo</TH></TR>\n";
	
	foreach ($proyectos as $proyecto) {
                print "  <TR>
                <TD class='tabla2'>".$proyecto['attr']['nombre']."</a></TD>
                <TD class='tabla2' align=\"center\">".$proyecto['inicio']."</TD>
                <TD class='tabla2' align=\"center\">".$proyecto['fin']."</TD>
                <TD class='tabla2' align=\"center\">".$proyecto['convocatoria']."</TD>";
		
		unset($tmp);
		if ( is_array($proyecto['usuarios']['login']) ) { 
			$tmp=$proyecto['usuarios']['login'];
		} else {
			$tmp[] = $proyecto['usuarios']['login'];
		}
                print "<TD class='tabla2'>".implode(', ',$tmp)."&nbsp;</TD>";

		print "<TD class='tabla2' align=\"right\">".$proyecto['nhoras_pri']."</TD>
		<TD class='tabla2' align=\"right\">".$proyecto['nhoras_npri']."</TD>";
		
		if($accounting[$proyecto['attr']['nombre']]['total'])
			print "<TD class='tabla2' align='right'>".$funciones->convSeg2($accounting[$proyecto['attr']['nombre']]['total'])."</TD>";
		else
			print "<TD class='tabla2' align='right'>&nbsp;</TD>";



		
                print "  </TR>\n";
        }

        print "</TABLE>\n";

?>

</center>

<p>La informaci&oacute;n de consumo recoge los datos hasta <b><u><?=date('d/m/Y H:i:s O', $ahora);?></u></b>

</body>
</html>


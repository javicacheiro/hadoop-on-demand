<?php
/*****************************************************************************\
*                                                                             *
*   File name       session_php.inc                                           *
*                                                                             *
*   Description     Use PHP built-in sessions handling                        *
*                                                                             *
*   Notes           To use this authentication scheme, set in                 *
*                   config.inc.php:                                           *
*                       $auth["session"]  = "php";                            *
*                                                                             *
*                                                                             *
*   History                                                                   *
*    2003/11/09 JFL Created this file                                         *
*    Remaining history in ChangeLog and CVS logs                              *
*                                                                             *
\*****************************************************************************/

// $Id: session_php.inc 2207 2011-12-21 16:53:15Z cimorrison $

global $PHP_SELF;

// Get non-standard form variables
$Action = get_form_var('Action', 'string');
$NewUserName = get_form_var('NewUserName', 'string');
$NewUserPassword = get_form_var('NewUserPassword', 'string');
$TargetURL = get_form_var('TargetURL', 'string');
$returl = get_form_var('returl', 'string');

if (isset($cookie_path_override)) {
  $cookie_path = $cookie_path_override;
} else {
  $cookie_path = $PHP_SELF;
  // Strip off everything after the last '/' in $PHP_SELF
  $cookie_path = preg_replace('/[^\/]*$/', '', $cookie_path);
}

global $auth;

if (!isset($auth["session_php"]["session_expire_time"])) {
  // Default to the behaviour of previous versions of MRBS, use only
  // session cookies - no persistent cookie.
  $auth["session_php"]["session_expire_time"] = 0;
}

session_name("CloudPyme_Proyect_SESSID");  // call before session_set_cookie_params() - see PHP manual
session_set_cookie_params($auth["session_php"]["session_expire_time"],
                          $cookie_path);
session_start();

/*
  Target of the form with sets the URL argument "Action=SetName".
  Will eventually return to URL argument "TargetURL=whatever".
*/
if (isset($Action) && ($Action == "SetName")) {
  /* First make sure the password is valid */
  if ($NewUserName == "") {
    // Unset the session variables
    if (isset($_SESSION)) {
      $_SESSION = array();
    } else {
      global $HTTP_SESSION_VARS;
      $HTTP_SESSION_VARS = array();
    }
  } else {
    if (!authValidateUser($NewUserName, $NewUserPassword)) {

?>



<!DOCTYPE html>
<html>
<head>
    <title>CloudPyme || Login (X)</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="robots" content="noindex">
    <link rel="shortcut icon" href="iconos/favicon.ico" />
    <!-- Google Fonts -->    
    <link href='http://fonts.googleapis.com/css?family=Open+Sans&subset=latin,cyrillic-ext' rel='stylesheet' type='text/css'>
    <!-- Bootstrap: CSS, JS, iconos -->
    <link rel="stylesheet" href="bootstrap/bootstrap.css" >
    <link rel="stylesheet" href="bootstrap/bootstrap-theme.css">
    <link rel="stylesheet" href="font-awesome/css/font-awesome.css">
    <!-- <script src="http://portal.cloudpyme.eu/js/bootstrap.min.js"></script> -->
    <!-- Estilos genéricos -->
    <link rel="stylesheet" href="http://portal.cloudpyme.eu/css/style.css"> 
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>


<?php
      require_once "header.inc";

      printLoginForm($TargetURL, get_vocab('unknown_user'));
      exit();
    }

    // Tenemos que recrear el fichero de autorizacion de ONE por si el usuario 
    // cambio la clave en el LDAP
    exec("/usr/bin/expect -c \"spawn su - ".$NewUserName." -c \\\"echo '".$NewUserName.":".$NewUserPassword."' > /.one_auth/".$NewUserName."; echo EXIT_STATUS=\\\$?\\\"; expect ssword; send ".$NewUserPassword."\\r; expect close;\"", $out, $return_var);

    if (isset($_SESSION)) {
      $_SESSION["UserName"] = $NewUserName;
      $_SESSION["Project"] = $NewUserPassword;
    } else {
      global $HTTP_SESSION_VARS;
      $HTTP_SESSION_VARS["UserName"] = $NewUserName;
      $HTTP_SESSION_VARS["Project"] = $NewUserPassword;
    }
  }
  // preserve the original $HTTP_REFERER by sending it as a GET parameter
  if (!empty($returl)) {
    // check to see whether there's a query string already
    if (strpos($TargetURL, '?') === false) {
      $TargetURL .= "?returl=" . urlencode($returl);
    } else {
      $TargetURL .= "&returl=" . urlencode($returl);
    }
  }
  header ("Location: $TargetURL"); /* Redirect browser to initial page */

  /* Note HTTP 1.1 mandates an absolute URL. Most modern browsers support relative URLs,
    which allows to work around problems with DNS inconsistencies in the server name.
    Anyway, if the browser cannot redirect automatically, the manual link below will work. */

?>




<!DOCTYPE html>
<html>
<head>
    <title>CloudPyme || Login (Y) </title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="robots" content="noindex">
    <link rel="shortcut icon" href="iconos/favicon.ico" />
    <!-- Google Fonts -->    
    <link href='http://fonts.googleapis.com/css?family=Open+Sans&subset=latin,cyrillic-ext' rel='stylesheet' type='text/css'>
    <!-- Bootstrap: CSS, JS, iconos -->
    <link rel="stylesheet" href="bootstrap/bootstrap.css" >
    <link rel="stylesheet" href="bootstrap/bootstrap-theme.css">
    <link rel="stylesheet" href="font-awesome/css/font-awesome.css">
    <!-- <script src="http://portal.cloudpyme.eu/js/bootstrap.min.js"></script> -->
    <!-- Estilos genéricos -->
    <link rel="stylesheet" href="bootstrap/style.css"> 
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<?php
  require_once "header.inc";
  echo "<p>Please click <a href=\"".htmlspecialchars($TargetURL)."\">here</a> if you're not redirected automatically to the page you requested.</p>\n";
  require_once "footer.inc";
  echo "</body>\n";
  echo "</html>\n";
  exit();
}





/*
  Display the login form. Used by two routines below.
  Will eventually return to $TargetURL.
*/
function printLoginForm($TargetURL, $msg)
{
  global $PHP_SELF, $HTTP_REFERER;
  global $returl;
           
?>
<!-- BLOQUE FORMULARIO LOGIN -->
<!-- CONTENT -->
<div id="container"> 
          
        <!-- col_center -->
        <div id="col_center">   

        <center>

            <div class="panel panel-info" style="width:35%;">
             <div class="panel-heading"><strong><?='LOG IN';?></strong></div>
                <div class="panel-body">

                <form role="form" id="logon" method="post" action="<?php echo htmlspecialchars(basename($PHP_SELF)); ?>">
                                     
                    <div class="input-group margin-bottom-sm">
                        <span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
                        <input class="form-control" placeholder="<?=get_vocab("usuario");?>" type="text" id="NewUserName" name="NewUserName" value="" />
                     </div><br/>
                     
                      
                     <div class="input-group">
                          <span class="input-group-addon"><i class="fa fa-key fa-fw"></i></span> 
                          <input class="form-control" placeholder="<?=get_vocab("contraseña");?>" type="password" id="NewUserPassword" name="NewUserPassword" />
                     </div><br/> 

                      
                     <div>                      
                           <button type="submit" class="btn btn-primary" id="login_btn" value="Enviar" name="submit"><?=get_vocab("acceder");?></button>
                     </div>

                        
                        <?php
                        // We need to preserve the original calling page, so that it's there when we eventually get
                        // to the TargetURL (especially if that's edit_entry.php).  If this is the first time through then $HTTP_REFERER holds
                        // the original caller.If this is the second time through we will have stored it in $returl.
                        if (!isset($returl))
                        {
                          $returl = isset($HTTP_REFERER) ? $HTTP_REFERER : "";
                        }
                        echo "<input type=\"hidden\" name=\"returl\" value=\"" . htmlspecialchars($returl) . "\">\n";
                        ?>
                     
                        <input type="hidden" name="TargetURL" value="<?php echo htmlspecialchars($TargetURL) ?>">
                        <input type="hidden" name="Action" value="SetName">
                        
                        
                        <?php
                        if (isset($msg)) {
                            
                            if ($msg != ""){
                                echo "<br/><div class='alert alert-danger'><strong><i class='fa fa-warning'></i></strong> ".get_vocab("msg_error_login")."</div>";
                             } else {
                                //echo "<br/><div class='alert alert-warning'><strong><i class='fa fa-question-circle'></i></strong> ".get_vocab("msg_exito_login")."</div>";
                                echo "<br/><div class='alert alert-warning'><strong><i class='fa fa-question-circle'></i></strong> ".'You can use your existing CESGA credentials to access the service.'."</div>";
                             }
                        }
                        ?>
  
              </form>
              <!-- END FORMULARIO LOGIN -->
             </div> <!-- End Panel Body -->                 
         </div> 
        </center>
        </div>
        <!-- end col_center -->
 </div> 
<!-- END CONTAINER -->
<!-- END BLOQUE FORMULARIO LOGIN -->
<?php
  require_once "footer.inc";
  echo "</body>\n";
  echo "</html>\n";
  exit();
}
?>




<?php
/* authGet()
 * Request the user name/password
 * Returns: Nothing
 */
function authGet($titulo)
{
  global $PHP_SELF, $QUERY_STRING;
?>
<!DOCTYPE html>
<html>
<head>
    <TITLE><?=$titulo;?></TITLE>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="robots" content="noindex">
    <link rel="shortcut icon" href="iconos/favicon.ico" />
    <!-- Google Fonts -->    
    <link href='http://fonts.googleapis.com/css?family=Open+Sans&subset=latin,cyrillic-ext' rel='stylesheet' type='text/css'>
    <!-- Bootstrap: CSS, JS, iconos -->
    <link rel="stylesheet" href="bootstrap/bootstrap.css" >
    <link rel="stylesheet" href="bootstrap/bootstrap-theme.css">
    <link rel="stylesheet" href="font-awesome/css/font-awesome.css">
    <!--  <script src="http://portal.cloudpyme.eu/js/bootstrap.min.js"></script> -->
    <!-- Estilos genéricos -->
    <link rel="stylesheet" href="bootstrap/style.css">
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<?php
  require_once "header.inc";

  $TargetURL = basename($PHP_SELF);
  if (isset($QUERY_STRING))
  {
    $TargetURL = $TargetURL . "?" . $QUERY_STRING;
  }
  printLoginForm($TargetURL, '');

  exit();
}


function getUserName()
{
  if (isset($_SESSION) && isset($_SESSION["UserName"]) && ($_SESSION["UserName"] != ""))
  {
    return $_SESSION["UserName"];
  }
  else
  {
    global $HTTP_SESSION_VARS;
    if (isset($HTTP_SESSION_VARS["UserName"]) && ($HTTP_SESSION_VARS["UserName"] != ""))
    {
      return $HTTP_SESSION_VARS["UserName"];
    }
  }
}

function getUserPassword() 
{
  if (isset($_SESSION) && isset($_SESSION["Project"]) && ($_SESSION["Project"] != ""))
  {
    return $_SESSION["Project"];
  }
  else
  {
    global $HTTP_SESSION_VARS;
    if (isset($HTTP_SESSION_VARS["Project"]) && ($HTTP_SESSION_VARS["Project"] != ""))
    {
      return $HTTP_SESSION_VARS["Project"];
    }
  }
}










/* NO NECESARIO
// Print the logon entry on the top banner.

function PrintLogonBox()
{
  global $PHP_SELF, $QUERY_STRING, $user_list_link, $day, $month, $year;

  $TargetURL = basename($PHP_SELF);
  if (isset($url_base) && ($url_base != ""))
  {
    $TargetURL = $url_base . '/' . $TargetURL;
  }
  if (isset($QUERY_STRING))
  {
    $TargetURL = $TargetURL . "?" . $QUERY_STRING;
  }
  $user=getUserName();
  if (isset($user))
  {
    // words 'you are xxxx' becomes a link to the
    // report page with only entries created by xxx. Past entries are not
    // displayed but this can be changed
    $search_string = "report.php?from_day=$day&amp;from_month=$month&amp;".
      "from_year=$year&amp;to_day=1&amp;to_month=12&amp;to_year=2030&amp;areamatch=&amp;".
      "roommatch=&amp;namematch=&amp;descrmatch=&amp;summarize=1&amp;sortby=r&amp;display=d&amp;".
      "sumby=d&amp;creatormatch=".urlencode($user); ?>

                <a href="<?php echo "$search_string\" title=\""
         . get_vocab('show_my_entries') . "\">" . get_vocab('you_are')." "
         . htmlspecialchars($user) ?></a>
                <form method="post" action="admin.php">
                  <div>
                    <input type="hidden" name="TargetURL" value="<?php echo htmlspecialchars($TargetURL) ?>">
                    <input type="hidden" name="Action" value="SetName">
                    <input type="hidden" name="NewUserName" value="">
                    <input type="hidden" name="NewUserPassword" value="">
                    <input type="submit" value=" <?php echo get_vocab('logoff') ?> ">
                  </div>
                </form>
<?php
  }
  else
  {
?>
                <a href=""><?php echo get_vocab('unknown_user'); ?></a>
                <form method="post" action="admin.php">
                  <div>
                    <input type="hidden" name="TargetURL" value="<?php echo htmlspecialchars($TargetURL) ?>">
                    <input type="hidden" name="Action" value="QueryName">
                    <input type="submit" value=" <?php echo get_vocab('login') ?> ">
                  </div>
                </form>
<?php
  } 
  if (isset($user_list_link))
  {
    print "<a href=\"$user_list_link\">" . get_vocab('user_list') . "</a>\n";
  }
}*/
?>

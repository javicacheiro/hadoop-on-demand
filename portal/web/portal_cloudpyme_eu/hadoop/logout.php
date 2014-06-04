<?
	require_once "config_lang.inc";
        require_once "grab_globals.inc.php";
        require_once "config_auth.inc";
        require_once "functions.inc";
        require_once "mrbs_auth.inc";

        echo "<p>Antes de checkAuthorised<p>";

        // Check the user is authorised for this page
        checkAuthorised("Desconectar sesi&oactue;n");

	#header("Location: /index.php");
	exit;
?>

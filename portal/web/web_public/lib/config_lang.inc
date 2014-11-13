<?php
//session_start();

	$default_language_tokens = "es";
        
     
	$lang_timezone = array(
			'es' => 'Europe/Madrid',
			'pt' => 'Europe/Lisbon',
			'en' => 'Europe/London');

	$lang_support = array_keys($lang_timezone);

	$lang_map_windows = array(
			'en' => 'en',
			'en-au' => 'en',
			'en-bz' => 'en',
			'en-ca' => 'en',
			'en-cb' => 'en',
			'en-en' => 'en',
			'en-gb' => 'en',
			'en-ie' => 'en',
			'en-jm' => 'en',
			'en-nz' => 'en',
			'en-ph' => 'en',
			'en-tt' => 'en',
			'en-us' => 'en',
			'en-za' => 'en',
			'es' => 'es',
			'es-es' => 'es',
			'es-mx' => 'es',
			'pt' => 'pt',
			'pt-br' => 'pt',
			'pt-pt' => 'pt');

	global $idioma;

	if (isset($_GET['lang'])) {
		if (in_array($_GET['lang'], $lang_support)) {
			$idioma = $_GET['lang'];
		} else {
			$idioma = $default_language_tokens;                     
		}
	}
	// Otherwise we enumerate the user's language preferences...
	elseif (isset($_SERVER["HTTP_ACCEPT_LANGUAGE"])) // Attempt to use $HTTP_ACCEPT_LANGUAGE only when defined.
	{
		$lang_specifiers = explode(',',$_SERVER["HTTP_ACCEPT_LANGUAGE"]);
		foreach ($lang_specifiers as $specifier) {
			if (preg_match('/([a-zA-Z\-]+);q=([0-9\.]+)/', $specifier, $matches)) {
				$langs[$matches[1]] = $matches[2];
			} else if (preg_match("/([a-zA-Z\-]+)/", $specifier, $matches)) {
				$langs[$matches[1]] = 1.0;
			}
		}
		arsort($langs,SORT_NUMERIC);

		$keys = array_keys($langs);

		if (isset($lang_map_windows[$keys[0]])) {
			$idioma=$lang_map_windows[$keys[0]];
		} else {
			$idioma = $default_language_tokens;
		}
	}
	else // Else use the default value
	{
		 $idioma = $default_language_tokens;
                // $idioma = $_SESSION['idioma'];
	}

	// Get a default set of language tokens, you can change this if you like.
	// Always include English as the fallback language, in case the selected language
	// is missing some translated tokens
	// include "lang." . $default_language_tokens;
        
         //Prueba
          if (!isset($_SESSION['idioma'])) {
             
                $_SESSION['idioma']= $idioma;
                         
           } else {
               
                if (isset ($_GET['lang'])){
                    $_SESSION['idioma']= $_GET['lang'];
                } else {
                    $_SESSION['idioma'] = $_SESSION['idioma'];
                } 
           }

        
        include_once "lang." . $_SESSION['idioma'];
        
        
	//include_once just to avoid reloading the 'en' file
	//include_once "lang." . $idioma;

/*    if (isset($_SESSION)) {
      $_SESSION["Language"] = $idioma;
    } else {
      global $HTTP_SESSION_VARS;
      $HTTP_SESSION_VARS["Language"] = $idioma;
    }
*/
?>

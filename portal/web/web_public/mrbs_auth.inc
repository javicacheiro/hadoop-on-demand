<?php
// $Id: mrbs_auth.inc 2149 2011-10-25 09:47:01Z cimorrison $

// include the authentification wrappers
require_once "auth_$auth[type].inc";

if (isset($auth['session']))
{
  require_once "session_$auth[session].inc";
}

// MINIMUM PAGE ACCESS LEVELS
// These pages are set to have a minimum access level of 1 as ordinary users
// should be able to access them because they will have read access and in some
// cases write access for their own entries.   Where necessary further checks are
// made within the page to prevent ordinary users gaining access to admin features.
########################################################################################
#
#  Cambiamos para que por defecto todas las paginas tengan required_level=1 excepto que
# se pongan mas abajo en la lista de paginas con required_level = 2
#
########################################################################################
//$page_level['admin.php'] = 1;  // Ordinary users can view room details 
// These pages have a minimum access level of 2 as they only contain admin features
// $page_level['test_xml.php'] = 2;


/* getAuthorised($level)
 * 
 * Check to see if the current user has a certain level of rights
 * 
 * $level - The access level required
 * 
 * Returns:
 *   0        - The user does not have the required access
 *   non-zero - The user has the required access
 */

function getAuthorised($level, $titulo)
{
  // If the minimum level is zero (or not set) then they are
  // authorised, whoever they are
  if (empty($level))
  {
    return TRUE;
  }
  // Otherwise we need to check who they are
  $user = getUserName();
  if(isset($user) == FALSE)
  {
    authGet($titulo);
    return 0;
  }
  return authGetUserLevel($user) >= $level;
}

/* checkAuthorised()
 *
 * Checks to see that a user is authorised to access the current page.
 * If they are not, then shows an Access Denied message and exits.
 *
 * Required page level authorisations are defined above.
 * If they are not defined then the function plays safe and requires the
 * highest level of authorisation.
 */
function checkAuthorised($titulo)
{
  global $page_level, $max_level;
  global $day, $month, $year, $area, $room;
  global $PHP_SELF;
  
  // Get the minimum authorisation level for this page
  if (isset($page_level[basename($PHP_SELF)]))
  {
    $required_level = $page_level[basename($PHP_SELF)];
  }
  elseif (isset($max_level))
  {
    $required_level = $max_level;
  }
  else
  {
    $required_level = 1;
  }

  // Check that the user has this level
  if (!getAuthorised($required_level, $titulo))
  {
    showAccessDenied($titulo);
    exit();
  }
}

/* getWritable($creator, $user, $room)
 * 
 * Determines if a user is able to modify an entry
 *
 * $creator - The creator of the entry
 * $user    - Who wants to modify it
 * $room    - The id of the room that the entry is in
 *
 * Returns:
 *   0        - The user does not have the required access
 *   non-zero - The user has the required access
 */

/*
function getWritable($creator, $user, $room)
{
  // Always allowed to modify your own stuff
  if(strcasecmp($creator, $user) == 0)
  {
    return 1;
  }
  
  // Otherwise you have to be a (booking) admin for this room
  if (auth_book_admin($user, $room))
  {
    return 1;
  }
  
  // Unathorised access
  return 0;
}
*/




/* showAccessDenied()
 * 
 * Displays an appropriate message when access has been denied
 * 
 * Returns: Nothing
 */


/*
function showAccessDenied($titulo)
{
  global $HTTP_REFERER;

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<HEAD>
        <TITLE>CloudPyme.- <?=$titulo;?>: <?=get_vocab("accessdenied");?></TITLE>
        <LINK TYPE="text/css" REL="stylesheet" HREF="./styles/style.css">
        <link rel="stylesheet" href="./styles/main.css" media="screen">
        <link rel="stylesheet" href="./styles/colors.css" media="screen">
        <link rel="stylesheet" href="./styles/login.css" media="screen">
        <meta name="robots" content="noindex" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</HEAD>

<?php 
 require_once "header.inc"; 
?>

<?php 
<center>
<table  width="600">
        <tr>
           <td align='center'><h1><?=$titulo;?>: <?=get_vocab("accessdenied");?></h1></td>
        </tr>
</table>
<H3><?php echo get_vocab("norights")?><p>La p&aacute;gina a la que est&aacute; intentando acceder es restringida. Para cualquier consulta envie un correo a <u>sistemas@cesga.es</u></H3>
<a href="<?php echo htmlspecialchars($HTTP_REFERER); ?>"><?php echo get_vocab("returnprev"); ?></a>

 ?>

<?php 
  // Print footer and exit
  require_once "footer.inc";
  echo "</body>\n";
  echo "</html>\n";
}
*/




/* auth_can_edit_user($creator, $user)
 * 
 * Determines if a user is able to edit another user's details
 *
 * $user    - Who wants to modify it
 * $target  - The user that is being edited
 *
 * Returns:
 *   0        - The user does not have the required access
 *   non-zero - The user has the required access
 */
/*
function auth_can_edit_user($user, $target)
{
  global $min_user_editing_level;
  
  // Always allowed to modify your own stuff
  if(strcasecmp($user, $target) == 0)
  {
    return 1;
  }

  if(authGetUserLevel($user) >= $min_user_editing_level)
  {
    return 1;
  }

  // Unathorised access
  return 0;
}
*/


// auth_book_admin($user, $room)
//
// Checks whether $user is allowed has booking administration rights
// for $room - ie is allowed to modify and delete other people's bookings
// and to approve bookings.
// At the moment $room is ignored, but is passed here so that later
// it can be enhanced to provide fine-grained permissions
// Returns:  TRUE if the user is allowed has booking admin rights for
// the room; otherwise FALSE
/*
function auth_book_admin($user, $room)
{
  return (authGetUserLevel($user) >= 2);
}*/
?>

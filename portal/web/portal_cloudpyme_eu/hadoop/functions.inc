<?php
// $Id: functions.inc 2222 2011-12-31 10:45:20Z cimorrison $


// Function to call string translations
function get_vocab($tag)
{
	global $vocab;
	return $vocab[$tag];
}



// Set the default timezone.  
// Caters for PHP servers that don't
// have date_default_timezone_set()
function mrbs_default_timezone_set($timezone)
{
  global $area_defaults;
  
  if (!isset($timezone))
  {
    // to prevent people running into DST problems
    $message = 'Configuration error: $timezone has not been set.';
    trigger_error($message, E_USER_WARNING);
    fatal_error(TRUE, $message);
  }
  else
  {
    if (function_exists("date_default_timezone_set"))
    {
      // We try and set the timezone to $timezone, which is the timezone setting
      // for the area.   If that fails we try the default for new areas.   And if
      // that fails we try the server timezone.
      if (!date_default_timezone_set($timezone))
      {
        $fallback_timezone = $area_defaults['timezone'];
        if (!date_default_timezone_set($fallback_timezone))
        {
          // date_default_timezone_get() was introduced at the same time as
          // date_default_timezone_set(), so it will exist
          $fallback_timezone = date_default_timezone_get();
          if (!date_default_timezone_set($fallback_timezone))
          {
            $message = "Could not set any timezone";
            trigger_error($message, E_USER_WARNING);
            fatal_error(TRUE, $message);
          }
        }
        trigger_error("Could not set timezone to '$timezone'; using '$fallback_timezone' instead", E_USER_WARNING);
      }
    }
    else
    {
      putenv("TZ=$timezone");
    }
  }
}




// Deal with $private_xxxx overrides.  Simplifies
// logic related to private bookings.
global $private_override;
if ($private_override == "private" )
{
  $private_mandatory=TRUE;
  $private_default=TRUE;
}
elseif ($private_override == "public" )
{
  $private_mandatory=TRUE;
  $private_default=FALSE;
}

$done_header = FALSE;



// Format a timestamp in RFC 1123 format, for HTTP headers
// e.g. Wed, 28 Jul 2010 12:43:58 GMT
function rfc1123_date($timestamp)
{
  return gmdate("D, d M Y G:i:s \\G\\M\\T",$timestamp);
}




// A little helper function to send an "Expires" header. Just one
// parameter, the number of seconds in the future to set the expiry
function expires_header($seconds)
{
  header("Expires: ".rfc1123_date(time()+$seconds));
}



// Converts a duration of $dur seconds into a duration of
// $dur $units
function toTimeString(&$dur, &$units, $translate=TRUE)
{
  if (abs($dur) >= 60)
  {
    $dur /= 60;

    if (abs($dur) >= 60)
    {
      $dur /= 60;

      if((abs($dur) >= 24) && ($dur % 24 == 0))
      {
        $dur /= 24;

        if((abs($dur) >= 7) && ($dur % 7 == 0))
        {
          $dur /= 7;

          if ((abs($dur) >= 52) && ($dur % 52 == 0))
          {
            $dur  /= 52;
            $units = "years";
          }
          else
          {
            $units = "weeks";
          }
        }
        else
        {
          $units = "days";
        }
      }
      else
      {
        $units = "hours";
      }
    }
    else
    {
      $units = "minutes";
    }
  }
  else
  {
    $units = "seconds";
  }
  
  // Limit any floating point values to three decimal places
  if (is_float($dur))
  {
    $dur = sprintf('%.3f', $dur);
    $dur = rtrim($dur, '0');  // removes trailing zeros
  }
  
  // Translate into local language if required
  if ($translate)
  {
    $units = get_vocab($units);
  }
}




// Converts a time period of $units into seconds, when it is originally
// expressed in $dur_units.   (Almost the inverse of toTimeString(),
// but note that toTimeString() can do language translation)
function fromTimeString(&$units, $dur_units)
{
  if (!isset($units) || !isset($dur_units))
  {
    return;
  }
  
  switch($dur_units)
  {
    case "years":
      $units *= 52;
    case "weeks":
      $units *= 7;
    case "days":
      $units *= 24;
    case "hours":
      $units *= 60;
    case "periods":
    case "minutes":
      $units *= 60;
    case "seconds":
      break;
  }
  $units = (int) $units;
}




function toPeriodString($start_period, &$dur, &$units, $translate=TRUE)
{
  global $periods;

  $max_periods = count($periods);
  $dur /= 60;  // duration now in minutes
  $mins_in_day = 24*60;
  $days = $dur / $mins_in_day;
  $remainder = $dur % $mins_in_day;
  // strip out any gap between the end of the last period on one day
  // and the beginning of the first on the next
  if ($remainder > $max_periods)
  {
    $remainder += $max_periods - $mins_in_day;
  }
  
  // We'll express the duration as an integer, in days if possible, otherwise periods
  if (($remainder == 0) || (($start_period == 0) && ($remainder == $max_periods)))
  {
    $dur = (int) $days;
    if ($remainder == $max_periods)
    {
      $dur++;
    }
    $units = $translate ? get_vocab("days") : "days";
  }
  else
  {
    $dur = (intval($days) * $max_periods) + $remainder;
    $units = $translate ? get_vocab("periods") : "periods";
  }
}




// Converts a period of $units starting at $start_period into seconds, when it is
// originally expressed in $dur_units (periods or days).   (Almost the inverse of
// toPeriodString(), but note that toPeriodString() can do language translation)
function fromPeriodString($start_period, &$units, $dur_units)
{
  global $periods;
  
  if (!isset($units) || !isset($dur_units))
  {
    return;
  }
  
  // First get the duration in minutes
  $max_periods = count($periods);
  if ($dur_units == "periods")
  {
    $end_period = $start_period + $units;
    if ($end_period > $max_periods)
    {
      $units = (24*60*floor($end_period/$max_periods)) + ($end_period%$max_periods) - $start_period;
    }
  }
  if ($dur_units == "days")
  {
    if ($start_period == 0)
    {
      $units = $max_periods + ($units-1)*60*24;
    }
    else
    {
      $units = $units * 60 * 24;
    }
  }
  
  // Then convert into seconds
  $units = (int) $units;
  $units = 60 * $units;
}




// Get the duration of an interval given a start time and end time.  Corrects for
// DST changes so that the duration is what the user would expect to see.  For
// example 12 noon to 12 noon crossing a DST boundary is 24 hours.
//
// Returns an array indexed by 'duration' and 'dur_units'
//
//    $start_time   int     start time as a Unix timestamp
//    $end_time     int     end time as a Unix timestamp
//    $translate    boolean whether to translate into the browser language

function get_duration($start_time, $end_time, $translate=TRUE)
{
  global $enable_periods;
  
  $result = array();
  $result['duration'] = $end_time - $start_time;
  // Need to make DST correct in opposite direction to entry creation
  // so that user see what he expects to see
  $result['duration'] -= cross_dst($start_time, $end_time);
  if ($enable_periods)
  {
    $time = getdate($start_time);
    $start_period = $time['minutes'];
    toPeriodString($start_period, $result['duration'], $result['dur_units'], $translate);
  }
  else
  {
    toTimeString($result['duration'], $result['dur_units'], $translate);
  }
  return $result;
}




// Error handler - this is used to display serious errors such as database
// errors without sending incomplete HTML pages. This is only used for
// errors which "should never happen", not those caused by bad inputs.
// If $need_header!=0 output the top of the page too, else assume the
// caller did that. Alway outputs the bottom of the page and exits.
function fatal_error($need_header, $message, $show_form_data = true)
{
  global $simple_trailer, $weekstarts, $view_week_number, $strftime_format;
  
  if ($need_header)
  {
    print_header(0, 0, 0, 0, "");
  }
  error_log("MRBS: ".$message);

  if ($show_form_data)
  {
    if (!empty($_GET))
    {
      error_log("MRBS GET: ".print_r($_GET, true));
    }
    if (!empty($_POST))
    {
      error_log("MRBS POST: ".print_r($_POST, true));
    }
  }
  if (!empty($_SESSION))
  {
    error_log("MRBS SESSION: ".print_r($_SESSION, true));
  }
  echo "<p>$message</p>";
  require_once "trailer.inc";
  exit;
}




// Escape a PHP string for use in JavaScript
//
// Based on a function contributed by kongaspar at gmail dot com at 
// http://www.php.net/manual/function.addcslashes.php
function escape_js($str)
{
  return addcslashes($str, "\\\'\"&\n\r<>/");
}



// Remove backslash-escape quoting if PHP is configured to do it with
// magic_quotes_gpc. Use this whenever you need the actual value of a GET/POST
// form parameter (which might have special characters) regardless of PHP's
// magic_quotes_gpc setting.
function unslashes($s)
{
  if (get_magic_quotes_gpc())
  {
    return stripslashes($s);
  }
  else
  {
    return $s;
  }
}


// Clean up a row from the area table, making sure there are no nulls, casting
// boolean fields into bools and doing some sanity checking
function clean_area_row($row)
{
  global $force_resolution, $area_defaults, $boolean_fields, $private_override_options;
  
  foreach ($row as $key => $value)
  {
    // If the "per area" setting is in the database, then use that.   Otherwise
    // just stick with the default setting from the config file.
    // (don't use the database setting if $force_resolution is TRUE 
    // and we're looking at the resolution field)
    if (($key != 'resolution') || empty($force_resolution))
    {
      $row[$key] = (isset($row[$key])) ? $value : $area_defaults[$key];
    }
    // Cast those fields which are booleans into booleans
    if (in_array($key, $boolean_fields['area']))
    {
      $row[$key] = (bool) $row[$key];
    }
  }
  // Do some sanity checking in case the area table is somehow messed up
  // (1) 'private_override' must be a valid value
  if (array_key_exists('private_override', $row) &&
      !in_array($row['private_override'], $private_override_options))
  {
    $row['private_override'] = 'private';  // the safest default
    $message = "Invalid value for 'private_override' in the area table.  Using '${row['private_override']}'.";
    trigger_error($message, E_USER_WARNING);
  }
  // (2) 'resolution' must be positive
  if (array_key_exists('resolution', $row) &&
      (empty($row['resolution']) || ($row['resolution'] < 0)))
  {
    $row['resolution'] = 30*60;  // 30 minutes, a reasonable fallback
    $message = "Invalid value for 'resolution' in the area table.   Using ${row['resolution']} seconds.";
    trigger_error($message, E_USER_WARNING);
  }
  
  return $row;
}




// generate the predicate for use in an SQL query to test whether
// an area has $field set
function some_area_predicate($field)
{
  global $area_defaults;
  
  $predicate = "(($field IS NOT NULL) AND ($field > 0))";
  if ($area_defaults[$field])
  {
    $predicate = "(" . $predicate . " OR ($field IS NULL))";
  }
  return $predicate;
}



function hour_min_format()
{
  global $twentyfourhour_format, $strftime_format;
  
  if ($twentyfourhour_format)
  {
    return $strftime_format['time24'];
  }
  else
  {
    return $strftime_format['time12'];
  }
}




function period_date_string($t, $mod_time=0)
{
  global $periods, $strftime_format;

  $time = getdate($t);
  $p_num = $time["minutes"] + $mod_time;
  if( $p_num < 0 )
  {
    $p_num = 0;
  }
  if( $p_num >= count($periods) - 1 )
  {
    $p_num = count($periods ) - 1;
  }
  // I have made the separator a ',' as a '-' leads to an ambiguious
  // display in report.php when showing end times.
  return array($p_num, $periods[$p_num] . utf8_strftime(", " . $strftime_format['date'], $t));
}



function period_time_string($t, $mod_time=0)
{
  global $periods;

  $time = getdate($t);
  $p_num = $time["minutes"] + $mod_time;
  if ( $p_num < 0 )
  {
    $p_num = 0;
  }
  if ( $p_num >= count($periods) - 1 )
  {
    $p_num = count($periods ) - 1;
  }
  return $periods[$p_num];
}



function time_date_string($t)
{
  global $twentyfourhour_format, $strftime_format;

  if ($twentyfourhour_format)
  {
    return utf8_strftime($strftime_format['datetime24'], $t);
  }
  else
  {
    return utf8_strftime($strftime_format['datetime12'], $t);
  }
}



// version of the standard PHP function nl2br() that takes account of the fact
// that the optional second parameter is only available from PHP 5.3.0 onwards.
function mrbs_nl2br($string)
{
  if (function_exists('version_compare') && version_compare(PHP_VERSION, '5.3.0', 'ge'))
  {
    return nl2br($string, IS_XHTML);
  }
  else
  {
    return nl2br($string);
  }
}



// Version of the standard PHP function html_entity_decode()
// Although html_entity_decode() was introduced in PHP 4.3.0, support for
// multi-byte character sets was only introduced in PHP 5.0.0.   
// So if we're running PHP5 or higher we'll use the standard
// PHP function; otherwise we'll do the best we can.   At the moment
// we just replace &nbsp; with an ordinary space, which
// should be sufficient in most MRBS circumstances.   This could
// always be extended later to do something more sophisticated if
// necessary.
//
// Note: the charset defaults to UTF-8.  This is slightly different from the
// behaviour in the PHP html_entity_decode() function where the default charset
// was only changed from ISO-8859-1 to UTF-8 in PHP 5.4.0
function mrbs_entity_decode($string, $quote_style=ENT_COMPAT, $charset = 'UTF-8')
{
  if (function_exists('version_compare') && version_compare(PHP_VERSION, '5.0.0', 'ge'))
  {
    $string = html_entity_decode($string, $quote_style, $charset);
  }
  else
  {
    $string = str_replace('&nbsp;', ' ', $string);
  }
  return $string;
}



// This will return the appropriate value for isdst for mktime().
// The order of the arguments was chosen to match those of mktime.
// hour is added so that this function can when necessary only be
// run if the time is between midnight and 3am (all DST changes
// occur in this period.
function is_dst ( $month, $day, $year, $hour="-1" )
{
  if ( $hour != -1  && $hour > 3)
  {
    return( -1 );
  }
   
  // entering DST
  if( !date( "I", mktime(12, 0, 0, $month, $day-1, $year)) && 
      date( "I", mktime(12, 0, 0, $month, $day, $year)))
  {
    return( 0 ); 
  }

  // leaving DST
  else if( date( "I", mktime(12, 0, 0, $month, $day-1, $year)) && 
           !date( "I", mktime(12, 0, 0, $month, $day, $year)))
  {
    return( 1 );
  }
  else
  {
    return( -1 );
  }
}



// if crossing dst determine if you need to make a modification
// of 3600 seconds (1 hour) in either direction
function cross_dst ( $start, $end )
{
  // entering DST
  if ( !date( "I", $start) &&  date( "I", $end))
  {
    $modification = -3600;
  }

  // leaving DST
  else if(  date( "I", $start) && !date( "I", $end))
  {
    $modification = 3600;
  }
  else
  {
    $modification = 0;
  }

  return $modification;
}



// Generate a globally unique id
//
// We will generate a uid of the form "MRBS-uniqid-MD5hash@domain_name" 
// where uniqid is time based and is generated by uniqid() and the
// MD5hash is the first 8 characters of the MD5 hash of $str concatenated
// with a random number.
function generate_global_uid($str)
{
  $uid = uniqid('MRBS-');
  $uid .= "-" . substr(md5($str . rand(0,10000)), 0, 8);
  $uid .= "@";
  // Add on the domain name if possible, if not the server name,
  // otherwise 'MRBS'
  if (empty($_SERVER['SERVER_NAME']))
  {
    $uid .= 'MRBS';
  }
  elseif (strpos($_SERVER['SERVER_NAME'], 'www.') === 0)
  {
    $uid .= substr($_SERVER['SERVER_NAME'], 4);
  }
  else
  {
    $uid .= $_SERVER['SERVER_NAME'];
  }

  return $uid;
}

// Tests whether an array is associative
//
// Thanks to magentix at gmail dot com at http://php.net/manual/function.is-array.php
function is_assoc($arr)
{
  return (is_array($arr) && count(array_filter(array_keys($arr),'is_string')) == count($arr));
}


// Checks whether we are running as a CLI module
//
// Based on code from mniewerth at ultimediaos dot com at 
// http://php.net/manual/features.commandline.php
function is_cgi()
{
  return (substr(PHP_SAPI, 0, 3) == 'cgi');
}


// Checks whether we are running from the CLI
//
// Based on code from mniewerth at ultimediaos dot com at 
// http://php.net/manual/features.commandline.php
function is_cli()
{
  global $allow_cli;
  
  if (!$allow_cli)
  {
    return FALSE;
    
  }
  if (defined('STDIN'))
  {
    return TRUE;
  }
  elseif (is_cgi() && getenv('TERM'))
  {
    return TRUE;
  }
  else
  {
    return FALSE;
  }
}
?>

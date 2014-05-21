<html>
<head>
	<title>INDEX LOCAL DESARROLLO RESTCLOUD PORTAL</title>
	<script type="text/javascript" src="js/main.js"></script>
	<script type="text/javascript" src="js/starthadoop.js"></script>
	<script type="text/javascript" src="js/stophadoop.js"></script>
	<script type="text/javascript" src="js/listhadoop.js"></script>
	<script type="text/javascript" src="js/clusterinfo.js"></script>
	<script type="text/javascript" src="js/startprogress.js"></script>
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/json2.js"></script>
</head>
<body>
	<script type="text/javascript">
		doIndex();
	</script>
</body>
</html>


<!--
<html>
<head>
	<title>CloudCesga Hadoop</title>
	<script type="text/javascript">
		function signupSubmit(){
			document.getElementById("signup_form").submit();
		}
		
		function signupClear(){
			document.getElementById("input_username").value = "";
			document.getElementById("input_password").value = "";
		}
		
		function generateSignupForm() {
			document.getElementById("mainDiv").innerHTML = "<form action=\"php/signup.php\" method=\"POST\" id=\"signup_form\">\n"+
								"<table class=\"form_table\">\n" +
									"<tr>\n" +
										"<td>Username</td>\n"+
										"<td><input type=\"text\" id=\"input_username\" name=\"username\"/></td>\n"+
									"</tr>\n"+
									"<tr>\n"+
										"<td>Password</td>\n"+
										"<td><input type=\"password\" id=\"input_password\" name=\"password\"/></td>\n"+
									"</tr>\n"+
									"<tr>\n"+
										"<td colspan=\"2\">\n"+
											"<button type=\"button\" onclick=\"signupSubmit()\" class=\"form_button\">Signup</button>\n"+
											"<button type=\"button\" onclick=\"signupClear()\" class=\"form_button\">Clear</button>\n"+ 
										"</td>\n"+
									"</tr>\n"+
								"</table>\n"+
							"</form>\n";
		}
	</script>
	<link rel="stylesheet" type="text/css" href="css/general.css"/>
</head>
<body>
	<div id="mainDiv">
		<span class="clickable_span" onclick="generateSignupForm()">SIGN UP</span>
	</div>
</body>
</html>
-->
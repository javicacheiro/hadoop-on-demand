<html>
<head>
	<title>Portal Hadoop</title>
	<title>INDEX LOCAL DESARROLLO RESTCLOUD PORTAL</title>
	<script type="text/javascript" src="js/main.js"></script>
	<script type="text/javascript" src="js/clusters.js"></script>
	<script type="text/javascript" src="js/keys.js"></script>
	<script type="text/javascript" src="js/ips.js"></script>
	<script type="text/javascript" src="js/help.js"></script>
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/json2.js"></script>
</head>
<body>
	<script type="text/javascript">
		var user = window.localStorage.getItem("user");
		//var passwd = window.localStorage.getItem("passwd");
		if(user){
			doIndex();
		}else
			window.location="index.php";
	</script>
</body>
</html>

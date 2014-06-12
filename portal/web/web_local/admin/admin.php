<html>
<head>
	<title>Hadoop management</title>
		<script type="text/javascript" src="js/main.js"></script>
		<script type="text/javascript" src="js/manageusers.js"></script>
		<script type="text/javascript" src="js/stophadoop.js"></script>
		<script type="text/javascript" src="js/listhadoop.js"></script>
		<script type="text/javascript" src="js/clusterinfo.js"></script>
		<script type="text/javascript" src="js/jquery.min.js"></script>
		<script type="text/javascript" src="js/json2.js"></script>
</head>
<body>
<?php
	echo '<script type="text/javascript"> var user = "' . $_POST["user"] . '";'
		. ' doIndex();</script>'
?>


</body>
</html>
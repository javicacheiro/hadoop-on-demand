var user = "albertoep"; // Comentar cuando se use en el servidor pues este valor lo recibira del php
var receivedJsonData = null;


function doIndex(){
	document.head.innerHTML ='<title>Hadoop Portal</title>'
			+'<script type="text/javascript" src="js/main.js"></script>'
			+'<script type="text/javascript" src="js/starthadoop.js"></script>'
			+'<script type="text/javascript" src="js/stophadoop.js"></script>'
			+'<script type="text/javascript" src="js/listhadoop.js"></script>'
			+'<script type="text/javascript" src="js/clusterinfo.js"></script>'
			+'<script type="text/javascript" src="js/jquery.min.js"></script>'
			+'<script type="text/javascript" src="js/json2.js"></script>'
			+'<link rel="stylesheet" type="text/css" href="css/general.css"/>'
			+'<link rel="stylesheet" type="text/css" href="css/header.css"/>'
			+'<link rel="stylesheet" type="text/css" href="css/content.css"/>'
			+'<link rel="stylesheet" type="text/css" href="css/footer.css"/>';
	
	document.body.innerHTML = '<div id="mainDiv">'
		+'<div id="headerDiv">'
			+'<table>'
				+'<tr><td onclick="doStartHadoop()">Start hadoop cluster</td>'
				+'<td onclick="doStopHadoop()">Stop hadoop cluster</td>'
				+'<td onclick="doListHadoop()">List hadoop clusters</td>'
				+'<td onclick="doGetClusterInfo()">Get cluster info by id</td></tr>'
			+'</table>'
		+'</div>'
		+'<div id="contentDiv">'
			+'Bienvenido: '+user;
		+'</div>'
		+'<div id="footerDiv">'
			+'<span class="footerSpan">Hadoop Portal uses RESTx API. Developed in 05-2014</span>'
		+'</div>'
		+'</div>';
}

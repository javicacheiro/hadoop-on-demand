var user = "albertoep"; // Comentar cuando se use en el servidor pues este valor lo recibira del php
var receivedJsonData = null;


function doIndex(){
	document.head.innerHTML ='<title>Hadoop Portal</title>'
			+'<script type="text/javascript" src="js/main.js"></script>'
			+'<script type="text/javascript" src="js/starthadoop.js"></script>'
			+'<script type="text/javascript" src="js/stophadoop.js"></script>'
			+'<script type="text/javascript" src="js/listhadoop.js"></script>'
			+'<script type="text/javascript" src="js/clusterinfo.js"></script>'
			+'<script type="text/javascript" src="js/startprogress.js"></script>'
			+'<script type="text/javascript" src="js/jquery.min.js"></script>'
			+'<script type="text/javascript" src="js/json2.js"></script>'
			+'<link rel="stylesheet" type="text/css" href="css/general.css"/>'
			+'<link rel="stylesheet" type="text/css" href="css/header.css"/>'
			+'<link rel="stylesheet" type="text/css" href="css/content.css"/>'
			+'<link rel="stylesheet" type="text/css" href="css/footer.css"/>'
			+'<link rel="stylesheet" type="text/css" href="css/details.css"/>';
	
	document.body.innerHTML = '<div id="mainDiv">'
		+'<div id="headerDiv">'
			+'<table>'
				+'<tr><td onclick="doStartHadoop()">Start hadoop cluster</td>'
				+'<td onclick="doListHadoop()">Manage hadoop clusters</td>'
			+'</table>'
		+'</div>'
		+'<div id="containerDiv">'
			+'<div id="contentDiv">'
				+'Bienvenido: '+user
			+'</div>'
			+'<div id="detailsDiv">'
				+'<span onclick="doStartProgress(27065)">Detalles</span>'
			+'</div>'
		+'</div>'
		+'<div id="footerDiv">'
			+'<span class="footerSpan">Hadoop Portal uses RESTx API. Development year 2014</span>'
		+'</div>'
		+'</div>';
}

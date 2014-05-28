var receivedJsonData = null;


function doIndex(){
		// is localStorage available?
	if (typeof window.localStorage != "undefined") {
		// retrieve
		user = localStorage.getItem("user");
	}else{
		alert("You should update your browser.\n"
			+"This page works with HTML5 and looks like your browser don't support it.");
	}
	
	
	document.head.innerHTML ='<title>Hadoop Portal</title>'
			+'<link rel="stylesheet" type="text/css" href="css/general.css"/>'
			+'<link rel="stylesheet" type="text/css" href="css/header.css"/>'
			+'<link rel="stylesheet" type="text/css" href="css/content.css"/>'
			+'<link rel="stylesheet" type="text/css" href="css/footer.css"/>'
			+'<link rel="stylesheet" type="text/css" href="css/details.css"/>'
			+'<script type="text/javascript" src="js/main.js"></script>'
			+'<script type="text/javascript" src="js/starthadoop.js"></script>'
			+'<script type="text/javascript" src="js/stophadoop.js"></script>'
			+'<script type="text/javascript" src="js/listhadoop.js"></script>'
			+'<script type="text/javascript" src="js/clusterinfo.js"></script>'
			+'<script type="text/javascript" src="js/startprogress.js"></script>'
			+'<script type="text/javascript" src="js/keys.js"></script>'
			+'<script type="text/javascript" src="js/jquery.min.js"></script>'
			+'<script type="text/javascript" src="js/json2.js"></script>';
	
	document.body.innerHTML = '<div id="mainDiv">'
		+'<div id="headerDiv">'
			+'<table>'
				+'<tr><td onclick="doStartHadoop()" id="startHadoopTd">Start hadoop cluster</td>'
				+'<td onclick="doListHadoop()" id="listHadoopTd">Manage hadoop clusters</td>'
				+'<td onclick="doKeys()" id="keysTd">SSH Keys</td>'
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

// Remove all class using from navigation TDs
function clearAllUsing(){
	document.getElementById("keysTd").className="";
	document.getElementById("listHadoopTd").className="";
	document.getElementById("startHadoopTd").className="";
}
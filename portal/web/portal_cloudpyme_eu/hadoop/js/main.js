var receivedJsonData = null;

var menuVisible = false;

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
			+'<link rel="stylesheet" type="text/css" href="css/clusters.css"/>'
			+'<link rel="stylesheet" type="text/css" href="css/progress.css"/>';
	
	document.body.innerHTML = '<div id="mainDiv">'
		+'<div id="headerDiv">'
			+'<div id="headerOptionDiv" onclick="toggleMenu()"></div>'
			+'<div id="headerOptionsDiv"></div>'
		+'</div>'
		+'<div id="contentDiv">'
			+'<div id="clustersDiv"> </div>'
			+'<div id="addClusterDiv"> </div>'
			+'<div id="addKeyDiv"> </div>'
			+'<div id="keysDiv"> </div>'
			+'<div id="helpDiv"> </div>'
			+'<div id="addIpDiv"> </div>'
			+'<div id="ipsDiv"> </div>'
		+'</div>'
		+'<div id="footerDiv"> </div>'
		+"</div>";

	
		hideAllContentDivs();
		hideMenu();
		
		doHeaderDivs();
		doClusters();
}

function hideAllContentDivs(){
	document.getElementById("clustersDiv").style.display="none";
	document.getElementById("addClusterDiv").style.display="none";
	document.getElementById("addKeyDiv").style.display="none";
	document.getElementById("keysDiv").style.display="none";
	document.getElementById("addIpDiv").style.display="none";
	document.getElementById("ipsDiv").style.display="none";
	document.getElementById("helpDiv").style.display="none";
}

function clearAllContentDivs(){
	document.getElementById("clustersDiv").innerHTML="";
	document.getElementById("addClusterDiv").innerHTML="";
	document.getElementById("addKeyDiv").innerHTML="";
	document.getElementById("keysDiv").innerHTML="";
	document.getElementById("addIpDiv").innerHTML="";
	document.getElementById("ipsDiv").innerHTML="";
	document.getElementById("helpDiv").innerHTML="";
}

// ** HEADER ** //
// ************ //
function showMenu(){
	document.getElementById("headerOptionsDiv").style.display="block";
	menuVisible=true;
}

function hideMenu(){
	document.getElementById("headerOptionsDiv").style.display="none";
	menuVisible = false;
}

function toggleMenu(){
	if(menuVisible)
		hideMenu();
	else
		showMenu();
}

function doHeaderDivs(){
	document.getElementById("headerOptionsDiv").innerHTML='<table id="optionsTable">'
		+'<tr><td class="optionsTd" onclick="doClusters()">Clusters</td></tr>'
		+'<tr><td class="optionsTd" onclick="doIps()">Firewall</td></tr>'
		+'<tr><td class="optionsTd" onclick="doKeys()">Login Credentials</td></tr>'
		//+'<tr><td class="optionsTd">Default Settings</td></tr>'
		+'<tr><td class="optionsTd" onclick="doHelp()">Help</td></tr>'
		+'<tr><td class="optionsTd" onclick="hideMenu()">Close</td></tr>'
		+'<tr><td class="optionsTd" onclick="exit()">Exit (logout)</td></tr>'
		+'</table>';
}

// ** EXIT ** //
// ********** //
function exit(){
	user = "";
	
	if (typeof window.localStorage != "undefined") {
		window.localStorage.setItem("user",user);
		window.localStorage.removeItem("user");
	}
	
	window.location = window.location.toString().substring(0,
		window.location.toString().lastIndexOf("/"));
}
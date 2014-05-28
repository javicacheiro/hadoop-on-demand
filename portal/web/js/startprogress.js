var progressWindow = null;
var progressReloading = false;
var firstProgressUpdate = false;

var timeIntervalId = null; // References the id given by the setInterval which updates the elapsedTime value

var progressWindowClosed = false;

function doStartProgress(clusterId){
	if(progressWindow != null){
		progressWindowClosed = true;
		onBeforeWindowUnload();
		progressWindow.close();
		return;
	}else
		progressWindowClosed = false;
	
	
	firstProgressUpdate = true;
	progressReloading = true;
	progressWindow = window.open("","_blank","resizable=0,scrollbars=1,location=0,width=500,height=200,top=50,left=50");
	
	// Add head
	progressWindow.document.write('<html><head><title>Hadoop Start Progress</title>' 
			+'<link rel="stylesheet" type="text/css" href="css/progress.css"/>'
			+'<link rel="stylesheet" type="text/css" href="css/content.css"/></head>'
			+'<body><div id="progressDiv"></div></body></html>');
	
	progressWindow.onbeforeunload = function(){
		onBeforeWindowUnload();
		progressWindow = null;
	}
	
	//timeIntervalId = progressWindow.setInterval(updateElapsedTime,500);
	
	progress_request(clusterId);
	
}



// ************ progress_request() ******************** //
// ***** Do the request for list all hadoop clusters ** //
// **************************************************** //
function progress_request(clusterId){
	
	var requestData = {
		user : user
	}
	
	var request = $.ajax({
		type: "POST",
		url: "php/list_clusters.php",
		data: JSON.stringify(requestData),
		dataType: "json",
		success: function(data){
			//Create jQuery object from the response HTML.
			receivedJsonData = jQuery.parseJSON(data);
			var printing = ""; // Stuff going to be put inside contentDiv
			
			var clusters = receivedJsonData.clusters; // Array
			var cluster = null;
		
			// Find cluster in cluster list
			for(var i = 0 ; i < clusters.length ; i++ ){
				if(clusters[i].user == user && clusters[i].id == clusterId){
					cluster = clusters[i];
					break;
				}
			}
			
			// If cluster found
			if(cluster!=null){
				if(firstProgressUpdate==true){
					firstProgressUpdate = false;
					printClusterProgress(cluster);
				}
				else
					updateClusterProgress(cluster);
			}

		}
	});
	
}

// ********************************* printClusterProgress ********************************* //
// ***** Prints the cluster progress according to the number of running virtual machines ** //
// **************************************************************************************** //
function printClusterProgress(cluster){
	var content = "";
	
	var vmTotal = cluster.vms.length;
	var totalCells = 50;
	var vmRunn = findRunningVirtualMachines(cluster.vms);
	var runnCells = (vmRunn*totalCells)/vmTotal;
	var elapsedTime = (new Date().getTime() - startInstant)/1000;	

	var content = '<span class="spanResponseLabel">Total Virtual Machines : </span>'
			+'<span class="spanResponseValue" id="spanProgressVmTotal">'+vmTotal+'</span><br/>'
		+'<span class="spanResponseLabel">Running Virtual Machines : </span>'
			+'<span class="spanResponseValue" id="spanProgressVmRunn">'+vmRunn+'</span><br/>'
		+'<span class="spanResponseLabel">Virtual Machines not running : </span>'
			+'<span class="spanResponseValue" id="spanProgressVmNotRunn">'+(vmTotal-vmRunn)+'</span><br/><br/>';
	
	content += '<div id="progressBarDiv"><table class="progressTable"><tr>';
	for(var i = 0 ; i < totalCells ; i++){
		if(i<=runnCells)
			content += '<td class="running"> </td>';
		else
			content += '<td> </td>';
	}
	content += "</tr></table></div><br/>";
	
	content += "Elapsed time: <span id=\"spanProgressElapsedTime\">"+parseInt(elapsedTime)+"</span>s";
	
	content += "<br/><br/><span id=\"spanProgressClusterStatus\">"+getProgressStatus(cluster)+"</span>";
	
	progressWindow.document.getElementById("progressDiv").innerHTML = content+"</body></html>";
	//progressWindow.document.write(content); // Write page
	
	
	if(cluster.exitStatus == -1 && progressReloading == true){
		timeIntervalId = progressWindow.setInterval(updateElapsedTime,500);
		progressWindow.setTimeout(progress_request(cluster.id),500);

	}
	
}

function updateClusterProgress(cluster){
	updateVmNumbers(cluster);
	updateProgressBar(cluster);
	
	if(cluster.exitStatus == -1 && progressReloading == true){
		progressWindow.setTimeout(progress_request(cluster.id),500);
	}else
		progressWindow.clearInterval(timeIntervalId);
	
}

function updateVmNumbers(cluster){
	var vmTotal = cluster.vms.length;
	var vmRunn = findRunningVirtualMachines(cluster.vms);
	
	progressWindow.document.getElementById("spanProgressVmTotal").innerHTML = vmTotal;
	progressWindow.document.getElementById("spanProgressVmRunn").innerHTML = vmRunn;
	progressWindow.document.getElementById("spanProgressVmNotRunn").innerHTML = (vmTotal-vmRunn);
	progressWindow.document.getElementById("spanProgressClusterStatus").innerHTML = getProgressStatus(cluster);
}

function updateProgressBar(cluster){
	var vmTotal = cluster.vms.length;
	var totalCells = 50;
	var vmRunn = findRunningVirtualMachines(cluster.vms);
	var runnCells = (vmRunn*totalCells)/vmTotal;	
	
	var pTbl = '<table class="progressTable"><tr>';
	for(var i = 0 ; i < totalCells ; i++){
		if(i<=runnCells)
			pTbl += '<td class="running"> </td>';
		else
			pTbl += '<td> </td>';
	}
	pTbl += "</tr></table>";
	
	progressWindow.document.getElementById("progressBarDiv").innerHTML = pTbl;
}

function updateElapsedTime(){
	var elapsedTime = (new Date().getTime() - startInstant)/1000;
	
	if(progressWindow.document.getElementById("spanProgressElapsedTime"))
		progressWindow.document.getElementById("spanProgressElapsedTime").innerHTML = parseInt(elapsedTime);
}

function findRunningVirtualMachines(vms){
	var numRunningVms = 0;
	for(var i = 0 ; i < vms.length ; i++){
		if(vms[i].status.toLowerCase()=="runn")
			numRunningVms++;
	}
	return numRunningVms;
}
function getProgressStatus(cluster){
	switch (cluster.exitStatus){
		case -1:
			return "Starting cluster...";
		case 0:
			return "OK";
		default:
			return "ERROR";
	}
	return "????";
}

// ************************ onBeforeWindowUnload *********************** //
// ***** Unload handler which defines what to do to unload the window ** //
// ********************************************************************* //
function onBeforeWindowUnload(){
	progressReloading = false;
	progressWindow.clearInterval(timeIntervalId);

	timeIntervalId = null;
}
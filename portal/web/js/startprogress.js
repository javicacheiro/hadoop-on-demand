var progressWindow;
var progressReloading = false;
var progressReload = null;
var startInstant = 0;
function doStartProgress(clusterId){
	startInstant = new Date().getTime();
	progressWindow = window.open("","_blank","resizable=0,scrollbars=1,location=0,width=500,height=200,top=50,left=50");
	
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
				printClusterProgress(cluster);
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
			+'<span class="spanResponseValue">'+vmTotal+'</span><br/>'
		+'<span class="spanResponseLabel">Running Virtual Machines : </span>'
			+'<span class="spanResponseValue">'+vmRunn+'</span><br/>'
		+'<span class="spanResponseLabel">Virtual Machines not running : </span>'
			+'<span class="spanResponseValue">'+(vmTotal-vmRunn)+'</span><br/><br/>';
	
	content += '<table class="progressTable"><tr>';
	for(var i = 0 ; i < totalCells ; i++){
		if(i<=runnCells)
			content += '<td class="running"> </td>';
		else
			content += '<td> </td>';
	}
	content += "</tr></table><br/>";
	
	content += "Elapsed time: "+parseInt(elapsedTime)+"s";
	
	content += "<br/><br/>"+getProgressStatus();
	
	progressWindow.document.body.innerHTML = "";
	progressWindow.document.write(content); // Write page
	progressWindow.document.head.innerHTML='<title>Hadoop Start Progress</title>' // Add headers
			+'<link rel="stylesheet" type="text/css" href="css/progress.css"/>'
			+'<link rel="stylesheet" type="text/css" href="css/content.css"/>';
	
	if(progressReloading == false && vmRunn < vmTotal)
		progressReload = progressWindow.setInterval(progress_request(cluster.id),500);
	
	
	if(vmRunn == vmTotal)
		progressWindow.clearInterval(progressReload);
}

function findRunningVirtualMachines(vms){
	var numRunningVms = 0;
	for(var i = 0 ; i < vms.length ; i++){
		if(vms[i].status.toLowerCase()=="runn")
			numRunningVms++;
	}
	return numRunningVms;
}
function getProgressStatus(){
	return "OK";
}
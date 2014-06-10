var clusters;

var advancedMode = false;
var defaultSize = 3;
var defaultReplicas = 3;
var defaultBlockSize = 16;
var defaultReduceTasks = 1;
var defaultHadoopVersion = 1;

var hadoopSize = defaultSize;
var hadoopReplicas = defaultReplicas;
var hadoopBlockSize = defaultBlockSize;
var hadoopReduceTasks = defaultReduceTasks;

function doClusters(){
	hideMenu();
	hideAllContentDivs();
	
	document.getElementById("clustersDiv").style.display="block";
	document.getElementById("addClusterDiv").style.display="block";
	
	fillClustersDiv();
	fillAddClusterDiv();
}

// ** CLUSTER LIST ** //
// ****************** //
function fillClustersDiv(){
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
			
			clusters = receivedJsonData.clusters; // Array
			
			for(var i = 0 ; i < clusters.length ; i++ ){
				if(clusters[i].user == user){
					printing += '<div class="divClusterLine" id="divClusterLine'+i+'">Cluster '+clusters[i].id+' ('+clusters[i].vms[0].ip+') '
							+'<span class="spanInfoCluster" id="spanInfoCluster'+i+'" onclick="toggleClusterInfo('+clusters[i].id+' , '+i+')">INFO</span>'
							+'<span class="spanStopCluster" onclick="deleteCluster('+clusters[i].id+' , '+i+')">DELETE</span></div>'
							+'<div id="divClusterInfo'+i+'" class="divClusterInfo"></div>';
				}
			}
			
			document.getElementById("clustersDiv").innerHTML = printing;
		}
	});
}

// ** CLUSTER INFO ** //
// ****************** //
function toggleClusterInfo(clusterId , which){
	var originalClass = document.getElementById("spanInfoCluster"+which).className;
	if(originalClass == 'spanInfoCluster')
		fillClusterInfo(clusterId, which);
	else
		hideClusterInfo(which);
}

function fillClusterInfo(clusterId, which){
	document.getElementById("spanInfoCluster"+which).className = "spanInfoClusterInUse";
	document.getElementById("divClusterInfo"+which).style.display="block";
	
	document.getElementById("divClusterInfo"+which).innerHTML = "Requesting cluster with id <b>"+clusterId+"</b> information.<br/>"
		+"This process can take a while. Please be patient.";
	
	var requestData = {
		id : clusterId
	}
	
	var request = $.ajax({
		type: "POST",
		url: "php/obtain_cluster_info.php",
		data: JSON.stringify(requestData),
		dataType: "json",
		success: function(data){
			//Create jQuery object from the response HTML.
			receivedJsonData = jQuery.parseJSON(data);
			
			var printing = "";
			
			try{
				// ** CLUSTER CONNECTION INFO ** //
				// ***************************** //
				printing = 'SSH access: \'<i>ssh hadoop@'+clusters[which].vms[0].ip+'</i>\'<br/>'
					+'JobTracker Web Interface: <span class="clickable_span_bgwhite" onclick="window.open(\'http://'+clusters[which].vms[0].ip+':50030/jobtracker.jsp\')">http://'+clusters[which].vms[0].ip+':50030/jobtracker.jsp</span><br/>'
					+'NameNode Web Interface: <span class="clickable_span_bgwhite" onclick="window.open(\'http://'+clusters[which].vms[0].ip+':50070/dfshealth.jsp\')">http://'+clusters[which].vms[0].ip+':50070/dfshealth.jsp</span><br/>';
				
				// ** VIRTUAL MACHINES ** //
				// ********************** //
				printing += '<table class="clusterInfoLongTable">'
					+'<tr><td colspan="8" class="tdInfoBigHeader">CLUSTER VIRTUAL MACHINES</td></tr>'
					+'<tr>'
						+'<td class="tdInfoHeader">VMID</td>'
						+'<td class="tdInfoHeader">Status</td>'
						+'<td class="tdInfoHeader">uCPU</td>'
						+'<td class="tdInfoHeader">uMEM</td>'
						+'<td class="tdInfoHeader">HOST</td>'
						+'<td class="tdInfoHeader">TIME</td>'
						+'<td class="tdInfoHeader">NAME</td>'
						+'<td class="tdInfoHeader">IP</td>'
					+'</tr>';
				
				for(var i = 0 ; i < clusters[which].vms.length ; i++){
					vm = clusters[which].vms[i];
					printing += '<tr>'
							+'<td>'+vm.vmid+'</td>'
							+'<td>'+vm.status+'</td>'
							+'<td>'+vm.ucpu+'</td>'
							+'<td>'+vm.umem+'</td>'
							+'<td>'+vm.host+'</td>'
							+'<td>'+vm.time+'</td>'
							+'<td>'+vm.name+'</td>'
							+'<td>'+vm.ip+'</td>'
						+'</tr>';
				}
				
				printing += "</table><br/>";
				
				// ** CLUSTER INFO ** //
				// ****************** //
				printing+='<table class="clusterInfoTable">'
					+'<tr><td colspan="9" class="tdInfoBigHeader">CLUSTER INFO</td></tr>'
					+'<tr>'
						+'<td class="tdInfoHeader">ID</td>'
						+'<td class="tdInfoHeader">User</td>'
						+'<td class="tdInfoHeader">Group</td>'
						+'<td class="tdInfoHeader">Name</td>'
						+'<td class="tdInfoHeader">VMs Total</td>'
						+'<td class="tdInfoHeader">VMs Running</td>'
						+'<td class="tdInfoHeader">uMEM</td>'
						+'<td class="tdInfoHeader">uCPU</td>'
						+'<td class="tdInfoHeader">Task Trackers</td>'
					+'</tr>'
					+'<tr>'
						+'<td>'+clusters[which].id+'</td>'
						+'<td>'+clusters[which].user+'</td>'
						+'<td>'+clusters[which].group+'</td>'
						+'<td>'+clusters[which].name+'</td>'
						+'<td>'+clusters[which].vms.length+'</td>'
						+'<td>'+receivedJsonData.vmRunning+'</td>'
						+'<td>'+receivedJsonData.umem+'</td>'
						+'<td>'+receivedJsonData.ucpu+'</td>'
						+'<td>'+receivedJsonData.taskTrackers.length+'</td>'
					+'</tr>'
					+'<tr>'
						+'<td class="tdInfoHeader">aDatanodes</td>'
						+'<td class="tdInfoHeader">dDatanodes</td>'
						+'<td class="tdInfoHeader">DFS Configured Capacity</td>'
						+'<td class="tdInfoHeader">DFS Present Capacity</td>'
						+'<td class="tdInfoHeader">DFS Remaining</td>'
						+'<td class="tdInfoHeader">DFS Used</td>'
						+'<td class="tdInfoHeader">Under Replicated Blocks</td>'
						+'<td class="tdInfoHeader">Blocks Corrupt Replicas</td>'
						+'<td class="tdInfoHeader">Missing Blocks</td>'
					+'</tr>'
					+'<tr>'
						+'<td>'+receivedJsonData.aDataNodes+'</td>'
						+'<td>'+receivedJsonData.dDataNodes+'</td>'
						+'<td>'+receivedJsonData.dfsConfiguredCapacity+'</td>'
						+'<td>'+receivedJsonData.dfsPresentCapacity+'</td>'
						+'<td>'+receivedJsonData.dfsRemaining+'</td>'
						+'<td>'+receivedJsonData.dfsUsed+'</td>'
						+'<td>'+receivedJsonData.underReplicatedBlocks+'</td>'
						+'<td>'+receivedJsonData.blocksCorruptReplicas+'</td>'
						+'<td>'+receivedJsonData.missingBlocks+'</td>'
					+'</tr>'
					+'</table><br/>';
				
				// ** DATANODES ** //
				// *************** //
				dataNodes = receivedJsonData.dataNodes;
				
				printing += '<table class="clusterInfoLongTable">'
					+'<tr><td colspan="8" class="tdInfoBigHeader">CLUSTER DATANODES</td></tr>'
					+'<tr>'
						+'<td class="tdInfoHeader">NUM</td>'
						+'<td class="tdInfoHeader">Name</td>'
						+'<td class="tdInfoHeader">Status</td>'
						+'<td class="tdInfoHeader">Configured Capacity</td>'
						+'<td class="tdInfoHeader">DFS Used</td>'
						+'<td class="tdInfoHeader">Non DFS Used</td>'
						+'<td class="tdInfoHeader">DFS Remaining</td>'
						+'<td class="tdInfoHeader">Last Contact</td>'
					+'</tr>';
				for(var i = 0 ; i < dataNodes.length ; i++){
					printing+="<tr>"
						+'<td>'+(i+1)+'</td>'
						+'<td>'+dataNodes[i].name+'</td>'
						+'<td>'+dataNodes[i].status+'</td>'
						+'<td>'+dataNodes[i].configuredCapacity+'</td>'
						+'<td>'+dataNodes[i].dfsUsed+'</td>'
						+'<td>'+dataNodes[i].nonDfsUsed+'</td>'
						+'<td>'+dataNodes[i].dfsRemaining+'</td>'
						+'<td>'+dataNodes[i].lastContact+'</td>'
					+'</tr>';
				}
				printing += '</table><br/>';
					
				// ** TASK TRACKERS ** //
				// ******************* //
				taskTrackers = receivedJsonData.taskTrackers;
				
				printing += '<table class="clusterInfoLongTable">'
					+'<tr><td colspan="2" class="tdInfoBigHeader">CLUSTER TASK TRACKERS</td></tr>'
					+'<tr>'
						+'<td class="tdInfoHeader">NUM</td>'
						+'<td class="tdInfoHeader">Task Tracker</td>'
					+'</tr>';
				
				for(var i = 0 ; i < taskTrackers.length ; i++){
					printing += '<tr>'
							+'<td>'+(i+1)+'</td>'
							+'<td>'+taskTrackers[i]+'</td>'
						+'</tr>';
				}
				
				printing += '</table><br/>';
				
			}catch(ex){
				try{
				
					// ** VIRTUAL MACHINES ** //
					// ********************** //
					printing = '<table class="clusterInfoLongTable">'
						+'<tr><td colspan="8" class="tdInfoBigHeader">CLUSTER VIRTUAL MACHINES</td></tr>'
						+'<tr>'
							+'<td class="tdInfoHeader">VMID</td>'
							+'<td class="tdInfoHeader">Status</td>'
							+'<td class="tdInfoHeader">TIME</td>'
							+'<td class="tdInfoHeader">NAME</td>'
							+'<td class="tdInfoHeader">IP</td>'
						+'</tr>';
					
					for(var i = 0 ; i < clusters[which].vms.length ; i++){
						vm = clusters[which].vms[i];
						printing += '<tr>'
								+'<td>'+vm.vmid+'</td>'
								+'<td>'+vm.status+'</td>'
								+'<td>'+vm.time+'</td>'
								+'<td>'+vm.name+'</td>'
								+'<td>'+vm.ip+'</td>'
							+'</tr>';
					}
					
					printing += "</table><br/>";
					
					// ** CLUSTER INFO ** //
					// ****************** //
					printing+='<table class="clusterInfoTable">'
						+'<tr><td colspan="9" class="tdInfoBigHeader">CLUSTER INFO</td></tr>'
						+'<tr>'
							+'<td class="tdInfoHeader">ID</td>'
							+'<td class="tdInfoHeader">User</td>'
							+'<td class="tdInfoHeader">Group</td>'
							+'<td class="tdInfoHeader">Name</td>'
							+'<td class="tdInfoHeader">VMs Total</td>'
						+'</tr>'
						+'<tr>'
							+'<td>'+clusters[which].id+'</td>'
							+'<td>'+clusters[which].user+'</td>'
							+'<td>'+clusters[which].group+'</td>'
							+'<td>'+clusters[which].name+'</td>'
							+'<td>'+clusters[which].vms.length+'</td>'
						+'</tr>'
						+'</table><br/>'
						
					printing += "<br/>Not all the information was ready yet.<br/>"
						+"If you want to see more specific info about cluster try again later.<br/>"
						+"Sorry of the inconveniences!";
					
				}catch(ex2){
					printing="An error ocurred retrieving cluster info.<br/>"
						+"Maybe it is not fully launched yet or it isn't up.<br/>"
						+"If this was a server error it should works just trying again.<br/>"
						+"Sorry for the inconveniences!";
				}
			}
			// PRINT
			document.getElementById("divClusterInfo"+which).innerHTML = printing;
		}
	});
}

function hideClusterInfo(which){
	try{
		document.getElementById("spanInfoCluster"+which).className = "spanInfoCluster";
	}catch(ex){
		// DO NOTHING
	}
	document.getElementById("divClusterInfo"+which).style.display="none";
}

// ** DELETE CLUSTER ** //
// ****************** //
function deleteCluster(clusterId , which){
	if(confirm('If you delete a cluster it will be unrecoverable.\n'
		+'Are you sure it is your definitive and last will to continue '
		+'even when data is gonna be lost unappealably and for ever?')	
	){
		
		if(isValidIdForDeleteCluster(clusterId)){
			warnClusterDeleted(clusterId , which);
			request_delete_cluster(clusterId , which);
		}
	}
}

function warnClusterDeleted(clusterId , which){
	document.getElementById("divClusterInfo"+which).innerHTML="Cluster with ID "+clusterId+" was DELETED";
	window.setTimeout(function(){hideClusterInfo(which)},5000);
}

// ************************** VALIDATION FUNCTS *************************** //
// ***** Functions which purpose is to validate stuff of this javascript ** //
// ************************************************************************ //
function isValidIdForDeleteCluster(id){
	if((""+id).length > 0)
		return true;
	return false;
}

// ************ request_list_clusters() ************** //
// ***** Do the request for list all hadoop clusters ** //
// **************************************************** //
function request_delete_cluster(id , which){
	var div = document.getElementById("divClusterLine" + which);
	div.parentNode.removeChild(div);
	
	var requestData = {
		id : id
	}
	
	var request = $.ajax({
		type: "POST",
		url: "php/delete_hadoop_cluster.php",
		data: JSON.stringify(requestData),
		dataType: "json",
		success: function(data){
			//Create jQuery object from the response HTML.
			receivedJsonData = jQuery.parseJSON(data);
			// DO NOTHING		
		}
	});
}

// ** ADD CLUSTER ** //
// ***************** //
function fillAddClusterDiv(){
	document.getElementById("addClusterDiv").innerHTML = '<br/><button class="btnAddCluster" onclick="btnAddClusterOnClick()">LAUNCH CLUSTER</button><br/><br/>';
	
}

function btnAddClusterOnClick(){
	clearAllContentDivs();
	hideAllContentDivs();
	document.getElementById("addClusterDiv").style.display="block";
	printLaunchClusterTable();
	
}

function printLaunchClusterTable(){
	save();
	document.getElementById("addClusterDiv").innerHTML = '<br/><br/><table class="form_table">'
		+'<tr>'
			+'<td class="labelTd" onmouseover="showNumberOfNodesHelp()" onmouseout="hideTipDiv()">Number of nodes</td>'
			+'<td><input type="text" id="input_size" name="nodes"/></td>'
		+'</tr>'
		+'<tr>'
			+'<td colspan="2"><span class="clickable_span" onclick="printLaunchClusterAdvancedTable()">Advanced features</span></td>'
		+'</tr>'
		+'<tr>'
			+'<td><button type="button" onclick="request_start_hadoop()">Start hadoop</button></td>'
			+'<td><button type="button" onclick="fill_default()">Default values</button></td>'
		+'</tr>'
		+"</table>"+getTipDiv();
	advancedMode = false;
	fill();
}

function printLaunchClusterAdvancedTable(){
	save();
	document.getElementById("addClusterDiv").innerHTML = '<br/><br/><table class="form_table">'
		+'<tr>'
			+'<td class="labelTd" onmouseover="showNumberOfNodesHelp()" onmouseout="hideTipDiv()">Number of slave nodes</td>'
			+'<td><input type="text" id="input_size" name="nodes"/></td>'
		+'</tr>'
		+'<tr>'
			+'<td class="labelTd" onmouseover="showReplicasHelp()" onmouseout="hideTipDiv()">DFS Replicas</td>'
			+'<td><input type="text" id="input_replicas" name="replicas"/></td>'
		+'</tr>'
		+'<tr>'
			+'<td class="labelTd" onmouseover="showBlockSizeHelp()" onmouseout="hideTipDiv()">DFS Block Size</td>'
			+'<td><input type="text" id="input_blocksize" name="blocksize"/></td>'
		+'</tr>'
		+'<tr>'
			+'<td class="labelTd" onmouseover="showReduceTasksNumber()" onmouseout="hideTipDiv()">Reduce Tasks Number</td>'
			+'<td><input type="text" id="input_reduce" name="reduce"/></td>'
		+'</tr>'
		+'<tr>'
			+'<td onmouseover="showHadoopVersion()" onmouseout="hideTipDiv()">Hadoop version</td>'
		+'</tr>'
		+'<tr>'
 			+'<td><input type="radio" name="version" checked="checked">Hadoop v1</input></td>'
			+'<td><input type="radio" name="version" disabled="disabled">Hadoop v2</input></td>'
		+'</tr>'
		+'<tr>'
			+'<td colspan="2"><span class="clickable_span" onclick="printLaunchClusterTable()">Standard features</span></td>'
		+'</tr>'
		+'<tr>'
			+'<td><button type="button" onclick="request_start_hadoop()">Start hadoop</button></td>'
			+'<td><button type="button" onclick="fill_default()">Default values</button></td>'
		+'</tr>'
		+"</table>"+getTipDiv();
	advancedMode = true;
	fill();
}

function getTipDiv(){
		return '<div id="tipDiv">'
			+'TIP<br/>DIV'
			+'</div>'
}

// ******************************* SHOW FUNCTIONS ******************************* //
// ***** showFunctions are functions which work is deal with hide n show items ** //
// ****************************************************************************** //
function showNumberOfNodesHelp(){
	document.getElementById("tipDiv").style.visibility="visible";
	document.getElementById("tipDiv").innerHTML="Number of nodes (without including master node).<br/>"
													+"default: "+defaultSize;
}

function showReplicasHelp(){
	document.getElementById("tipDiv").style.visibility="visible";
	document.getElementById("tipDiv").innerHTML="Number of DFS replicas to be created.<br/>"
													+"default: "+defaultReplicas;
}

function showBlockSizeHelp(){
	document.getElementById("tipDiv").style.visibility="visible";
	document.getElementById("tipDiv").innerHTML="Specifies block size in MB.<br/>"
													+"default: "+defaultBlockSize;
}

function showReduceTasksNumber(){
	document.getElementById("tipDiv").style.visibility="visible";
	document.getElementById("tipDiv").innerHTML="Number of reduce tasks.<br/>"
													+"default: "+defaultReduceTasks;
}

function showHadoopVersion(){
	document.getElementById("tipDiv").style.visibility="visible";
	document.getElementById("tipDiv").innerHTML="Sepecifies the hadoop version you want to use.<br/>"
													+"default: "+defaultHadoopVersion;
}

function hideTipDiv(){
	document.getElementById("tipDiv").innerHTML = "";
	document.getElementById("tipDiv").style.visibility="hidden";
}


// ********************** SAVE n FILL FUNCTIONS ********************** //
// ***** These functions are used to manage data during transitions ** //
// ******************************************************************* //
function save(){
	if(document.getElementById("input_size")) // The first time doStartHadoop is called save is invoken before any input has been created
		hadoopSize = document.getElementById("input_size").value;
	if(document.getElementById("input_replicas")){
		hadoopReplicas = document.getElementById("input_replicas").value;
		hadoopBlockSize = document.getElementById("input_blocksize").value;
		hadoopReduceTasks = document.getElementById("input_reduce").value;
	}
}

function fill(){
	document.getElementById("input_size").value = hadoopSize;
	if(document.getElementById("input_replicas")){
		document.getElementById("input_replicas").value = hadoopReplicas;
		document.getElementById("input_blocksize").value = hadoopBlockSize;
		document.getElementById("input_reduce").value = hadoopReduceTasks;
	}
}

function fill_default(){
	hadoopSize = defaultSize;
	hadoopReplicas = defaultReplicas;
	hadoopBlockSize = defaultBlockSize;
	hadoopReduceTasks = defaultReduceTasks;
	fill();
}

// **************************** VALIDATION FUNCT **************************** //
// ***** Validates everything is ok and return true if so, false otherwise ** //
// ************************************************************************** //
function validate(){
	save();
	
	// HadoopSize
	if(hadoopSize.length < 1){ // If hadoopSize is empty
		alert('Field for number of nodes is empty');
		document.getElementById("input_size").focus();
		return false;
	}
	
	if (isNaN(hadoopSize)){ // If hadoopSize is not a number
		alert('The field for number of nodes has invalid data (MUST be a number)');
		document.getElementById("input_size").focus();
		return false;
	}
	
	if (hadoopSize<0){ // If hadoopSize is less than 1
		alert('Number of nodes cannot be negative');
		document.getElementById("input_size").focus();
		return false;
	}
	
	if(hadoopSize.indexOf(".")>-1){ // If hadoopSize is decimal number
		alert('No decimal numbers allowed for number of nodes');
		document.getElementById("input_size").focus();
		return false;
	}
	
	if(advancedMode){
	// hadoopReplicas
		if(hadoopReplicas.length < 1){ // If hadoopReplicas is empty
			alert('Field for DFS Replicas is empty');
			document.getElementById("input_replicas").focus();
			return false;
		}
		
		if(isNaN(hadoopReplicas)){ // If hadoopReplicas is not a number
			alert('The field for DFS Replicas has invalid data (MUST be a number)');
			document.getElementById("input_replicas").focus();
			return false;
		}
		
		if(hadoopReplicas<0){ // If hadoopReplicas is negative number
			alert('Number of DFS Replicas cannot be negative');
			document.getElementById("input_replicas").focus();
			return false;
		}
		
		if(hadoopReplicas.indexOf(".")>-1){ // If hadoopReplicas is decimal number
			alert('No decimal numbers allowed for DFS Replicas');
			document.getElementById("input_replicas").focus();
			return false;
		}
		
	// hadoopBlockSize
		if(hadoopBlockSize.length < 1){ // If hadoopBlockSize is empty
			alert('Field for Block Size is empty');
			document.getElementById("input_blocksize").focus();
			return false;
		}
	
		if(isNaN(hadoopBlockSize)){ // If hadoopBlockSize is not a number
			alert("The field for Block Size has invalid data (MUST be a number)\n"+
				"don't specify unit [KB/MB/GB]. It is MB always.");
			document.getElementById("input_blocksize").focus();
			return false;
		}
	
		if(hadoopBlockSize<0){ // If hadoopBlockSize is negative number
			alert('Block size cannot be negative');
			document.getElementById("input_blocksize").focus();
			return false;
		}
	
		if(hadoopBlockSize.indexOf(".")>-1){ // If hadoopBlockSize is decimal number
			alert('No decimal numbers allowed for block size');
			document.getElementById("input_blocksize").focus();
			return false;
		}
		
	// hadoopReduceTasks
		if(hadoopReduceTasks.length < 1){ // If hadoopReduceTasks is empty
			alert('Field for Reduce Tasks Number is empty');
			document.getElementById("input_reduce").focus();
			return false;
		}
	
		if(isNaN(hadoopReduceTasks)){ // If hadoopReduceTasks is not a number
			alert('The field for Reduce Tasks Number has invalid data (MUST be a numer)');
			document.getElementById("input_reduce").focus();
			return false;
		}
	
		if(hadoopReduceTasks<0){ // If hadoopReduceTasks is negative number
			alert('Reduce Tasks Number cannot be negative');
			document.getElementById("input_reduce").focus();
			return false;
		}
		
		if(hadoopReduceTasks.indexOf(".")>-1){ // If hadoopReduceTasks is decimal number
			alert('No decimal numbers allowed for Reduce Tasks Number');
			document.getElementById("input_reduce").focus();
			return false;
		}
		
	}
	
	return true;
}

// ************ request_start_hadoop() ************ //
// ***** Do the request for start hadoop cluster ** //
// ************************************************ //
function request_start_hadoop(){
		if(validate()){
			
			var data = { 
				size : hadoopSize,
				replicas : defaultReplicas,
				blocksize : defaultBlockSize,
				reduce : defaultReduceTasks,
				user : user
			}
			
			if(advancedMode){
				data = { 
					size : hadoopSize,
					replicas : hadoopReplicas,
					blocksize : hadoopBlockSize,
					reduce : hadoopReduceTasks,
					user : user
				}
			}
			
			submit_request(data);
		}
}

function submit_request(requestData){
	var request = $.ajax({
		type: "POST",
		url: "php/create_hadoop_cluster.php",
		data: JSON.stringify(requestData),
		dataType: "json",
		success: function(data){
			startInstant = new Date().getTime();
			//Create jQuery object from the response HTML.
			document.getElementById("addClusterDiv").innerHTML = data;
		}
	});
	
}

// ** LAUNCHED CLUSTER PROGRESS ** //
// ******************************* //
var progressReloading = false;
var firstProgressUpdate = false;

var timeIntervalId = null; // References the id given by the setInterval which updates the elapsedTime value

function doStartProgress(clusterId){
	document.getElementById("addClusterDiv").innerHTML += '<div id="progressDiv"> </div>';
	document.getElementById("progressDiv").innerHTML = "Retrieving launched cluster status information...";
	document.getElementById("watchProgress").innerHTML = "";
	
	firstProgressUpdate = true;
	progressReloading = true;
	
	if(timeIntervalId!=null)
		window.clearInterval(timeIntervalId);
	
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
	for(var i = 0 ; i < totalCells+2 ; i++){
		if(i<=runnCells)
			content += '<td class="running"> </td>';
		else if(cluster.exitStatus == 0)
			content += '<td class="running"> </td>';
		else
			content += '<td> </td>';
	}
	
	content += "</tr></table></div><br/>";
	
	content += "Elapsed time: <span id=\"spanProgressElapsedTime\">"+parseInt(elapsedTime)+"</span>s";
	
	progressStatus = getProgressStatus(cluster);
	if(vmRunn==vmTotal)
		progressStatus = "Configuring cluster...";
	
	content += "<br/><br/><span id=\"spanProgressClusterStatus\">"+progressStatus+"</span>";
	
	document.getElementById("progressDiv").innerHTML = content;
	
	
	if(cluster.exitStatus == -1 && progressReloading == true){
		timeIntervalId = window.setInterval(updateElapsedTime,500);
		window.setTimeout(function(){progress_request(cluster.id)},10000);

	}
	
}

function updateClusterProgress(cluster){
	updateVmNumbers(cluster);
	updateProgressBar(cluster);
	
	if(cluster.exitStatus == -1 && progressReloading == true){
		window.setTimeout(function(){progress_request(cluster.id)},10000);
	}else
		window.clearInterval(timeIntervalId);
	
}

function updateVmNumbers(cluster){
	var vmTotal = cluster.vms.length;
	var vmRunn = findRunningVirtualMachines(cluster.vms);
	
	document.getElementById("spanProgressVmTotal").innerHTML = vmTotal;
	document.getElementById("spanProgressVmRunn").innerHTML = vmRunn;
	document.getElementById("spanProgressVmNotRunn").innerHTML = (vmTotal-vmRunn);
	progressStatus = getProgressStatus(cluster);
	if(vmRunn==vmTotal && cluster.exitStatus==-1)
		progressStatus = "Configuring cluster...";
	if(cluster.exitStatus==0)
		progressStatus = "OK<br/>"
			+'SSH access: \'<i>ssh hadoop@'+cluster.vms[0].ip+'</i>\'<br/>'
			+'JobTracker Web Interface: <span class="clickable_span_bgwhite" onclick="window.open(\'http://'+cluster.vms[0].ip+':50030/jobtracker.jsp\')">http://'+cluster.vms[0].ip+':50030/jobtracker.jsp</span><br/>'
			+'NameNode Web Interface: <span class="clickable_span_bgwhite" onclick="window.open(\'http://'+cluster.vms[0].ip+':50070/dfshealth.jsp\')">http://'+cluster.vms[0].ip+':50070/dfshealth.jsp</span><br/>'
			+'NOTICE: The cluster is protected using a <span class="clickable_span_bgwhite" onclick="doIps()">firewall</span> and uses <span class="clickable_span_bgwhite" onclick="doKeys()">ssh keys</span> for authentication.<br/>';

	document.getElementById("spanProgressClusterStatus").innerHTML = progressStatus;
}

function updateProgressBar(cluster){
	var vmTotal = cluster.vms.length;
	var totalCells = 50;
	var vmRunn = findRunningVirtualMachines(cluster.vms);
	var runnCells = (vmRunn*totalCells)/vmTotal;	
	
	var pTbl = '<table class="progressTable"><tr>';
	for(var i = 0 ; i < totalCells+2 ; i++){
		if(i<=runnCells)
			pTbl += '<td class="running"> </td>';
		else if(cluster.exitStatus == 0)
			pTbl += '<td class="running"> </td>';
		else
			pTbl += '<td> </td>';
	}
	pTbl += "</tr></table>";
	
	document.getElementById("progressBarDiv").innerHTML = pTbl;
}

function updateElapsedTime(){
	var elapsedTime = (new Date().getTime() - startInstant)/1000;
	
	if(document.getElementById("spanProgressElapsedTime"))
		document.getElementById("spanProgressElapsedTime").innerHTML = parseInt(elapsedTime);
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
// ** GLOBAL VARS ** //
// ***************** //
var advancedMode = false;
var defaultSize = 10;
var defaultReplicas = 3;
var defaultBlockSize = 16;
var defaultReduceTasks = 1;
var defaultHadoopVersion = 1;

var hadoopSize = defaultSize;
var hadoopReplicas = defaultReplicas;
var hadoopBlockSize = defaultBlockSize;
var hadoopReduceTasks = defaultReduceTasks;

// var user = "albertoep"; // Comentar cuando se use en el servidor pues este valor lo recibira del php

// **************************** DO FUNCTIONS **************************** //
// ***** doFunctions are functions which work is to "draw" the webpage ** //
// ********************************************************************** //
function doStartHadoop(){
	save();
	document.getElementById("contentDiv").innerHTML = '<table class="form_table">'
		+'<tr>'
			+'<td class="labelTd" onmouseover="showNumberOfNodesHelp()" onmouseout="hideTipDiv()">Number of nodes</td>'
			+'<td><input type="text" id="input_size" name="nodes"/></td>'
		+'</tr>'
		+'<tr>'
			+'<td colspan="2"><span class="clickable_span" onclick="doStartHadoopAdvanced()">Advanced features</span></td>'
		+'</tr>'
		+'<tr>'
			+'<td><button type="button" onclick="request_start_hadoop()">Start hadoop</button></td>'
			+'<td><button type="button" onclick="fill_default()">Default values</button></td>'
		+'</tr>'
		+"</table>"+getTipDiv();
	advancedMode = false;
	fill();
}

function doStartHadoopAdvanced(){
	save();
	document.getElementById("contentDiv").innerHTML = '<table class="form_table">'
		+'<tr>'
			+'<td class="labelTd" onmouseover="showNumberOfNodesHelp()" onmouseout="hideTipDiv()">Number of nodes</td>'
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
			+'<td><input type="radio" name="version">Hadoop v2</input></td>'
		+'</tr>'
		+'<tr>'
			+'<td colspan="2"><span class="clickable_span" onclick="doStartHadoop()">Standard features</span></td>'
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
			//Create jQuery object from the response HTML.
			document.getElementById("contentDiv").innerHTML = data;
		}
	});
	
}
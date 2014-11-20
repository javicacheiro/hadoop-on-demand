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

var ClusterService = {
	show: function () {
		document.getElementById("tipDiv").style.visibility="visible";
		document.getElementById("tipDiv").innerHTML="Number of nodes (without including master node).<br/>"
			+"default: "+defaultSize;
	}
	,hide: function (){
		document.getElementById("tipDiv").innerHTML = "";
		document.getElementById("tipDiv").style.visibility="hidden";
	}

	// ********************** SAVE n FILL FUNCTIONS ********************** //
	// ***** These functions are used to manage data during transitions ** //
	// ******************************************************************* //
	,save: function(){
		if(document.getElementById("input_size")) // The first time doStartHadoop is called save is invoken before any input has been created
			hadoopSize = document.getElementById("input_size").value;
		if(document.getElementById("input_replicas")){
			hadoopReplicas = document.getElementById("input_replicas").value;
			hadoopBlockSize = document.getElementById("input_blocksize").value;
			hadoopReduceTasks = document.getElementById("input_reduce").value;
		}
	}

	,fill: function(){
		document.getElementById("input_size").value = hadoopSize;
		if(document.getElementById("input_replicas")){
			document.getElementById("input_replicas").value = hadoopReplicas;
			document.getElementById("input_blocksize").value = hadoopBlockSize;
			document.getElementById("input_reduce").value = hadoopReduceTasks;
		}
	}

	,validate: function(){
		ClusterService.save();
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
	,request_start_hadoop: function(){
		
		if(ClusterService.validate()){	
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
			
			ClusterService.submit_request(data);
		}
	}
	,submit_request: function(requestData){
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
};

// ** GLOBAL VARS ** //
// ***************** //
//var receivedJsonData = null; // Defined in main.sj


function doGetClusterInfo(){
	document.getElementById("contentDiv").innerHTML = "";
	var id = prompt("Type cluster id");
	if(isValidIdForGetClusterInfo(id)){
		request_cluster_info(id);
	}
}

// ************************** VALIDATION FUNCTS *************************** //
// ***** Functions which purpose is to validate stuff of this javascript ** //
// ************************************************************************ //
function isValidIdForGetClusterInfo(id){
	return true;
}

// ************ request_list_clusters() ************** //
// ***** Do the request for list all hadoop clusters ** //
// **************************************************** //
function request_cluster_info(id){
	document.getElementById("contentDiv").innerHTML = "Requesting cluster with id <b>"+id+"</b> information.<br/>"
		+"This process can take a while. Please be patient.";
	
	var requestData = {
		id : id
	}
	
	var request = $.ajax({
		type: "POST",
		url: "php/obtain_cluster_info.php",
		data: JSON.stringify(requestData),
		dataType: "json",
		success: function(data){
			//Create jQuery object from the response HTML.
			receivedJsonData = jQuery.parseJSON(data);
			
			printing='<span class="spanResponseTitle">Cluster '+id+' info</span><br/>'
				+'<span class="spanResponseLabel">id : </span>'
					+'<span class="spanResponseValue">'+receivedJsonData.id+'</span><br/>'
				+'<span class="spanResponseLabel">user : </span>'
					+'<span class="spanResponseValue">'+receivedJsonData.user+'</span><br/>'
				+'<span class="spanResponseLabel">vmRunning : </span>'
					+'<span class="spanResponseValue">'+receivedJsonData.vmRunning+'</span><br/>'
				+'<span class="spanResponseLabel">vmTotal : </span>'
					+'<span class="spanResponseValue">'+receivedJsonData.vmTotal+'</span><br/>'
				+'<span class="spanResponseLabel">umem : </span>'
					+'<span class="spanResponseValue">'+receivedJsonData.umem+'</span><br/>'
				+'<span class="spanResponseLabel">ucpu : </span>'
					+'<span class="spanResponseValue">'+receivedJsonData.ucpu+'</span><br/>'
				+'<span class="spanResponseLabel">aDataNodes : </span>'
					+'<span class="spanResponseValue">'+receivedJsonData.aDataNodes+'</span><br/>'
				+'<span class="spanResponseLabel">dDataNodes : </span>'
					+'<span class="spanResponseValue">'+receivedJsonData.dDataNodes+'</span><br/>'
				+'<span class="spanResponseLabel">dfsConfiguredCapacity : </span>'
					+'<span class="spanResponseValue">'+receivedJsonData.dfsConfiguredCapacity+'</span><br/>'
				+'<span class="spanResponseLabel">dfsPresentCapacity : </span>'
					+'<span class="spanResponseValue">'+receivedJsonData.dfsPresentCapacity+'</span><br/>'
				+'<span class="spanResponseLabel">dfsRemaining : </span>'
					+'<span class="spanResponseValue">'+receivedJsonData.dfsRemaining+'</span><br/>'
				+'<span class="spanResponseLabel">dfsUsed : </span>'
					+'<span class="spanResponseValue">'+receivedJsonData.dfsUsed+'</span><br/>'
				+'<span class="spanResponseLabel">underReplicatedBlocks : </span>'
					+'<span class="spanResponseValue">'+receivedJsonData.underReplicatedBlocks+'</span><br/>'
				+'<span class="spanResponseLabel">blocksCorruptReplicas : </span>'
					+'<span class="spanResponseValue">'+receivedJsonData.blocksCorruptReplicas+'</span><br/>'
				+'<span class="spanResponseLabel">missingBlocks : </span>'
					+'<span class="spanResponseValue">'+receivedJsonData.missingBlocks+'</span><br/>';
					
			dataNodes = receivedJsonData.dataNodes;
			if(dataNodes!=null){
				printing += '<span class="spanResponseLabel">dataNodes : </span><br/>'
				for(var i = 0 ; i < dataNodes.length ; i++){
					printing+='<span class="spanWhiteSpace">&nbsp;</span>'
						+'<span class="spanResponseLabel">dataNode '+(i+1)+'</span><br/>'
						+'<span class="spanWhiteSpace">&nbsp;</span>'
						+'<span class="spanWhiteSpace">&nbsp;</span>'
							+'<span class="spanResponseLabel">name : </span>'
								+'<span class="spanResponseValue">'+dataNodes[i].name+'</span><br/>'
						+'<span class="spanWhiteSpace">&nbsp;</span>'
						+'<span class="spanWhiteSpace">&nbsp;</span>'
							+'<span class="spanResponseLabel">status : </span>'
								+'<span class="spanResponseValue">'+dataNodes[i].status+'</span><br/>'
						+'<span class="spanWhiteSpace">&nbsp;</span>'
						+'<span class="spanWhiteSpace">&nbsp;</span>'
							+'<span class="spanResponseLabel">configuredCapacity : </span>'
								+'<span class="spanResponseValue">'+dataNodes[i].configuredCapacity+'</span><br/>'
						+'<span class="spanWhiteSpace">&nbsp;</span>'
						+'<span class="spanWhiteSpace">&nbsp;</span>'
							+'<span class="spanResponseLabel">dfsUsed : </span>'
								+'<span class="spanResponseValue">'+dataNodes[i].dfsUsed+'</span><br/>'
						+'<span class="spanWhiteSpace">&nbsp;</span>'
						+'<span class="spanWhiteSpace">&nbsp;</span>'
							+'<span class="spanResponseLabel">nonDfsUsed : </span>'
								+'<span class="spanResponseValue">'+dataNodes[i].nonDfsUsed+'</span><br/>'
						+'<span class="spanWhiteSpace">&nbsp;</span>'
						+'<span class="spanWhiteSpace">&nbsp;</span>'
							+'<span class="spanResponseLabel">dfsRemaining : </span>'
								+'<span class="spanResponseValue">'+dataNodes[i].dfsRemaining+'</span><br/>'
						+'<span class="spanWhiteSpace">&nbsp;</span>'
						+'<span class="spanWhiteSpace">&nbsp;</span>'
							+'<span class="spanResponseLabel">lastContact : </span>'
								+'<span class="spanResponseValue">'+dataNodes[i].lastContact+'</span><br/>';
					
				}
			}else{
				printing+= '<span class="spanResponseLabel">dataNodes : </span>'
					+'<span class="spanResponseValue">null</span>';
			}
			
			taskTrackers = receivedJsonData.taskTrackers;
			if(taskTrackers!=null){
				printing+='<span class="spanResponseLabel">taskTrackers : </span><br/>'
				
				for(var i = 0 ; i < taskTrackers.length ; i++){
					printing+='<span class="spanWhiteSpace">&nbsp;</span>'
						+'<span class="spanResponseLabel">taskTracker : </span>'
							+'<span class="spanResponseValue">'+taskTrackers[i]+'</span><br/>';
				}
			}else{
				printing+='<span class="spanResponseLabel">taskTrackers : </span>'
					+'<span class="spanResponseValue">null</span>';
			}
			
			
			
			document.getElementById("contentDiv").innerHTML = printing;
		}
	});
	
}
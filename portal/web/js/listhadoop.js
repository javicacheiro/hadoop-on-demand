// ** GLOBAL VARS ** //
// ***************** //
var receivedJsonData = null;


function doListHadoop(){
	document.getElementById("contentDiv").innerHTML = "";
	request_list_clusters();
}

// ************ request_list_clusters() ************** //
// ***** Do the request for list all hadoop clusters ** //
// **************************************************** //
function request_list_clusters(requestData){
	
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
			
			for(var i = 0 ; i < clusters.length ; i++ ){
				if(clusters[i].user == user){
					printing += '<span class="spanResponseTitle">Cluster '+(i+1)+'</span><br/>'
						+'<span class="spanResponseLabel">id : </span>'
							+'<span class="spanResponseValue">' + clusters[i].id +'</span><br/>'
						+'<span class="spanResponseLabel">user : </span>'
							+'<span class="spanResponseValue">' + clusters[i].user+'</span><br/>'
						+'<span class="spanResponseLabel">group : </span>'
							+'<span class="spanResponseValue">' + clusters[i].group+'</span><br/>'
						+'<span class="spanResponseLabel">name : </span>'
							+'<span class="spanResponseValue">' + clusters[i].name+'</span><br/>'
						+'<span class="spanResponseLabel">vms : </span><br/>';
					
					for(var j = 0 ; j < clusters[i].vms.length ; j++){
							printing+='<span class="spanWhiteSpace">&nbsp;</span>'
							+'<span class="spanResponseLabel">VM '+(j+1)+'</span><br/>'
							+'<span class="spanWhiteSpace">&nbsp;</span>'
							+'<span class="spanWhiteSpace">&nbsp;</span>'
								+'<span class="spanResponseLabel">vmid : </span>'
									+'<span class="spanResponseValue">'+clusters[i].vms[j].vmid+'</span><br/>'
							+'<span class="spanWhiteSpace">&nbsp;</span>'
							+'<span class="spanWhiteSpace">&nbsp;</span>'
								+'<span class="spanResponseLabel">status : </span>'
									+'<span class="spanResponseValue">'+clusters[i].vms[j].status+'</span><br/>'
							+'<span class="spanWhiteSpace">&nbsp;</span>'
							+'<span class="spanWhiteSpace">&nbsp;</span>'
								+'<span class="spanResponseLabel">ucpu : </span>'
									+'<span class="spanResponseValue">'+clusters[i].vms[j].ucpu+'</span><br/>'
							+'<span class="spanWhiteSpace">&nbsp;</span>'
							+'<span class="spanWhiteSpace">&nbsp;</span>'
								+'<span class="spanResponseLabel">umem : </span>'
									+'<span class="spanResponseValue">'+clusters[i].vms[j].umem+'</span><br/>'
							+'<span class="spanWhiteSpace">&nbsp;</span>'
							+'<span class="spanWhiteSpace">&nbsp;</span>'
								+'<span class="spanResponseLabel">host : </span>'
									+'<span class="spanResponseValue">'+clusters[i].vms[j].host+'</span><br/>'
							+'<span class="spanWhiteSpace">&nbsp;</span>'
							+'<span class="spanWhiteSpace">&nbsp;</span>'
								+'<span class="spanResponseLabel">time : </span>'
									+'<span class="spanResponseValue">'+clusters[i].vms[j].time+'</span><br/>'
							+'<span class="spanWhiteSpace">&nbsp;</span>'
							+'<span class="spanWhiteSpace">&nbsp;</span>'
								+'<span class="spanResponseLabel">name : </span>'
									+'<span class="spanResponseValue">'+clusters[i].vms[j].name+'</span><br/>';
					}
				}
			}
			
			document.getElementById("contentDiv").innerHTML = printing;
		}
	});
	
}
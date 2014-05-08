// ** GLOBAL VARS ** //
// ***************** //
//var receivedJsonData = null; // Defined in main.sj


function doStopHadoop(){
	document.getElementById("contentDiv").innerHTML = "";
	var id = prompt("Type cluster id");
	if(isValidIdForDeleteCluster(id)){
		request_delete_cluster(id);
	}
}

// ************************** VALIDATION FUNCTS *************************** //
// ***** Functions which purpose is to validate stuff of this javascript ** //
// ************************************************************************ //
function isValidIdForDeleteCluster(id){
	if(id.length > 0)
		return true;
	return false;
}

// ************ request_list_clusters() ************** //
// ***** Do the request for list all hadoop clusters ** //
// **************************************************** //
function request_delete_cluster(id){
	document.getElementById("contentDiv").innerHTML = "Deleting cluster with id <b>"+id+"</b> . . . <br/>"
		+"This process can take a while. Please be patient.";
	
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
			var printing = receivedJsonData.message;
			document.getElementById("contentDiv").innerHTML = printing;
		}
	});
}
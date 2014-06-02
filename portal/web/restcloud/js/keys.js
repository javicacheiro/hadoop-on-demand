function doKeys(){
	hideMenu();
	clearAllContentDivs();
	hideAllContentDivs();
	
	doAddKeys();
	doDelKeys();
}

// ** DO ADD KEYS ** //
// ***************** //
function doAddKeys(){
	document.getElementById("addKeyDiv").style.display="block";
	document.getElementById("addKeyDiv").innerHTML='<br/><table class="form_table">'
		+'<tr>'
			+'<td class="labelTd_forTextarea">SSH key (rsa/dsa) </td>'
			+'<td><textarea id="input_key" name="key" class="tdTextarea"></textarea></td>'
		+'</tr>'
		+'<tr>'
			+'<td colspan="2" align="center" width="100%"><button type="button" onclick="request_add_key()">Add key</button>'
			+'<button type="button" onclick="clear_key()">Clear key</button></td>'
		+'</tr>'
		+'</table><br/><div id="addKeyResponseDiv"></div><br/>';
}

function clear_key(){
	document.getElementById("input_key").value="";
}

function request_add_key(){
	var key = document.getElementById("input_key").value;
	
	if(isValidKey(key)){
		var json = {
			username : user,
			key : key
		}
		
		var request = $.ajax({
			type: "POST",
			url: "php/add_sshkey.php",
			data: JSON.stringify(json),
			dataType: "json",
			success: function(data){
				var print = "<b>ERROR</b> adding key '<u>"+
					document.getElementById("input_key").value
					+"</u>' for user "+user;
				
				if(data.message == "OK"){
					print = "KEY '<u>"+document.getElementById("input_key").value
						+"</u>' <b>SUCCESSFULLY</b> added for user "+user;
				}
				
				document.getElementById("addKeyResponseDiv").innerHTML = print;
				
				request_sshkeys();
			}
		});
		
		alert('You have added a key.\nDepending on the server load, database performance, number of registered keys and'
				+' several other factors this operation can take a little. The page will reload information relative to'
				+' sshkeys automatically after this operation ends.');
	}else
		alert('You\'ve typed a non valid key');
}

// ** VALIDATION KEY FUNCTS ** //
function isValidKey(key){
	if(isValidDSAKey(key))
		return true;
	else if(isValidRSAKey(key))
		return true;
	
	return false;
}

function isValidDSAKey(key){
	keyHead = key.substring(0,7);
	if(keyHead!='ssh-dss')
		return false;
	
	if(key.length<(586))
		return false;
	
	if(key.indexOf("'")>-1)
		return false;
	
	return true;
}

function isValidRSAKey(key){
	keyHead = key.substring(0,7);
	if(keyHead!='ssh-rsa')
		return false;
	
	if(key.length<(380))
		return false;
	
	if(key.indexOf("'")>-1)
		return false;
	
	return true;
}

// ** DO DEL KEYS ** //
// ***************** //
function doDelKeys(){
	document.getElementById("keysDiv").style.display="block";
	document.getElementById("keysDiv").innerHTML="Loading SSHKeys . . .";
	
	request_sshkeys();
}

function request_sshkeys(){
	var json = {
		user : user
	}
	
	var request = $.ajax({
		type: "POST",
		url: "php/get_sshkeys.php",
		data: JSON.stringify(json),
		dataType: "json",
		success: function(data){
			var jsonData = jQuery.parseJSON(data);
			var print = "You have not any sshkey stored in our database.";
			
			if(jsonData.keys.length > 0){
				print = "";
				for(var i = 0 ; i < jsonData.keys.length ; i++){
					label = '<span class="spanResponseLabel">KEY '+i+' </span>';
					print += label+'<div class="keyDiv">'+jsonData.keys[i]+"</div>"
					+'<span class="spanStopCluster" onclick="deleteKey(\''+jsonData.keys[i]+'\')">DELETE</span><br/>'
					+"<br/>";
				}
			}
			
			document.getElementById("keysDiv").innerHTML=print;
			
			
		}
	});
}

function deleteKey(key){
	var json = {
		username : user,
		key : key
	}
	
	var request = $.ajax({
		type: "POST",
		url: "php/del_sshkey.php",
		data: JSON.stringify(json),
		dataType: "json",
		success: function(data){
			request_sshkeys();
		}
	});
	
	alert('You have deleted a key.\nDepending on the server load, database performance, number of registered keys and'
				+' several other factors this operation can take a little. The page will reload information relative to'
				+' sshkeys automatically after this operation ends.');
	
}


function doIps(){
	hideMenu();
	clearAllContentDivs();
	hideAllContentDivs();
	
	doAddIps();
	doDelIps();
}

// ** DO ADD IPS ** //
// **************** //
function doAddIps(){
	document.getElementById("addIpDiv").style.display="block";
	document.getElementById("addIpDiv").innerHTML='<br/><table class="form_table">'
		+'<tr>'
			+'<td class="labelTd_forTextarea">IP address </td>'
			+'<td><textarea id="input_ip" name="ip" class="tdTextarea"></textarea></td>'
		+'</tr>'
		+'<tr>'
			+'<td colspan="2" align="center" width="100%"><button type="button" onclick="request_add_ip()">Add IP</button>'
			+'<button type="button" onclick="clear_ip()">Clear IP</button></td>'
		+'</tr>'
		+'</table><br/><div id="addIPResponseDiv"></div><br/>';
}

function clear_ip(){
	document.getElementById("input_ip").value="";
}

function request_add_ip(){
	var ip = document.getElementById("input_ip").value;
	
	if(isValidIp(ip)){
		var json = {
			username : user,
			ip : ip
		}
		
		var request = $.ajax({
			type: "POST",
			url: "php/add_ip.php",
			data: JSON.stringify(json),
			dataType: "json",
			success: function(data){
				var print = "<b>ERROR</b> adding IP '<u>"+
					document.getElementById("input_ip").value
					+"</u>' for user "+user;
				
				if(data.message == "OK"){
					print = "IP '<u>"+document.getElementById("input_ip").value
						+"</u>' <b>SUCCESSFULLY</b> added for user "+user;
				}
				
				document.getElementById("addIPResponseDiv").innerHTML = print;
				
				request_ips();
			}
		});
		
		alert('You have added an IP.\nDepending on the server load, database performance, number of registered ips and'
				+' several other factors this operation can take a little. The page will reload information relative to'
				+' ips automatically after this operation ends.');
	}else
		alert('You\'ve typed a non valid IP');
}

// ** IP VALIDATION FUNCTS ** //
function isValidIp(ip){
	dotCount = 0;
	barCount = 0;
	
	for(var i=0 ; i<ip.length ; i++){
		if(!isValidIPChar(ip.charAt(i))){ // Es una caracter no valido
			console.log("No valid char found: "+ip.charAt(i));
			return false;
		}else{ // Es un caracter valido
			if(ip.charAt(i)=='.'){ // Es un punto
				dotCount++;
				if(dotCount>3)
					return false;
			}
			
			if(ip.charAt(i)=='/'){ // Es una barra
				barCount++;
				if(barCount>1)
					return false;
			}
		}
	}
	
	if(dotCount<3) // No hay suficientes puntos como para que sea una IP completa
		return false;
	
	splitted = ip.split(".");
	for(var i = 0 ; i < splitted.length && i <= 3 ; i++){
		var num = parseInt(splitted[i]);
		if(num>255)
			return false;
	}
	
	return true;
}

function isValidIPChar(c){
	validChars = ['1','2','3','4','5','6','7','8','9','0','.','/'];	
	
	for(var i = 0 ; i < validChars.length ; i++){
		if(validChars[i]==c)
			return true;
	}
	
	return false;
}

// ** DO DEL IPs ** //
// **************** //
function doDelIps(){
	document.getElementById("ipsDiv").style.display="block";
	document.getElementById("ipsDiv").innerHTML="Loading IPs . . .";
	
	request_ips();
}

function request_ips(){
	var json = {
		user : user
	}
	
	var request = $.ajax({
		type: "POST",
		url: "php/get_ips.php",
		data: JSON.stringify(json),
		dataType: "json",
		success: function(data){
			var jsonData = jQuery.parseJSON(data);
			var print = "You have not any IP stored in our database.";
			
			if(jsonData.ips.length > 0){
				print = "";
				for(var i = 0 ; i < jsonData.ips.length ; i++){
					label = '<span class="spanResponseLabel">IP </span>';
					print += label+jsonData.ips[i]
						+'<span class="spanStopCluster" onclick="deleteIp(\''+jsonData.ips[i]+'\')">DELETE</span><br/>'
						+"<br/>";
				}
			}
			
			document.getElementById("ipsDiv").innerHTML=print;
			
			
		}
	});
}

function deleteIp(ip){
	var json = {
		username : user,
		ip : ip
	}
	
	var request = $.ajax({
		type: "POST",
		url: "php/del_ip.php",
		data: JSON.stringify(json),
		dataType: "json",
		success: function(data){
			request_ips();
		}
	});
	
	alert('You have deleted an IP.\nDepending on the server load, database performance, number of registered IPs and'
				+' several other factors this operation can take a little. The page will reload information relative to'
				+' IPs automatically after this operation ends.');
	
}


  function closeD(){
    close();
  }

  function clear_key(){
    document.getElementById("input_key").value="";
  }

  function request_add_key(){
  var key = document.getElementById("input_key").value;

  if(isValidKey(key)){
    // TODO: Pass it from login screen instead of loading credentials.js
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
        var keyStr = document.getElementById("input_key").value;
        keyStr = keyStr.substring(0,10) + '...' + keyStr.substring(keyStr.length-10);
        print = "KEY '<u>"+ keyStr
          +"</u>' <b>SUCCESSFULLY</b> added for user "+user;
      }
      
      document.getElementById("addKeyResponseDiv").innerHTML = print;
      
      request_sshkeys();
    }
  });
  
  alert('You have added a key.\nDepending on the server load, database performance, number of registered keys and'
      +' several other factors this operation can take a little. The page will reload information relative to'
      +' sshkeys automatically after this operation ends.');
} else {
  alert('You\'ve typed a non valid key');
}
}

// ** KEY VALIDATION FUNCTS ** //
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


var x=0 // variable declarad e iniciada como cero

function operacion(){
  if(x==0){
    //solo entra aquí cuando x sea cero
    document.getElementById("input_ip").value=document .getElementById("texto").value;
    x++; // variable cambia a uno despues de copiar el texto.
  } else {
    //solo entra aquí cuando x sea uno
    document.formulario.submit(); //aquí envias el form
  }
}

function clear_ip(){
  document.getElementById("input_ip").value="";
}

function request_add_ip(){
  var ip = document.getElementById("input_ip").value;
  if(isValidIp(ip)){
    var json = {
      uusername : user,
      key : key
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
  } else {
    alert('You\'ve typed a non valid IP');
  }
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

// Setup: https://github.com/mstratman/jQuery-Smart-Wizard
$(document).ready(
    function(){
      // Smart Wizard   
      $('#wizard').smartWizard(
        onFinish: onFinishCallback
      );

      function onFinishCallback(){
        $('#wizard').smartWizard('showMessage','Finish Clicked');
      }
    }
);

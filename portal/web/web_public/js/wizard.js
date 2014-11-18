var KeyService = {
    //TODO: FIXME: Rewrite get function
    get: function(user) {
        var json = {
            user: user
        }
        var request = $.ajax({
            type: 'POST',
            url: 'php/get_sshkeys.php',
            data: JSON.stringify(json),
            dataType: 'json',
            success: function (data) {
                var jsonData = jQuery.parseJSON(data);
                var print = 'You have not any sshkey stored in our database.';
                if (jsonData.keys.length > 0) {
                    print = '';
                    for (var i = 0; i < jsonData.keys.length; i++) {
                        label = '<span class="spanResponseLabel">KEY ' + i + ' </span>';
                        print += label + '<div class="keyDiv">' + jsonData.keys[i] + '</div>'
                        + '<span class="spanStopCluster" onclick="deleteKey(\'' + jsonData.keys[i] + '\')">DELETE</span><br/>'
                        + '<br/>';
                    }
                }
                $('addKeyResponseDiv').html(print);
                //document.getElementById('keysDiv').innerHTML = print;
            }
        });
    }
    ,add: function() {
        var key = document.getElementById('input_key').value;
        if (this.validate(key)) {
            // TODO: Pass it from login screen instead of loading credentials.js
            var json = {
                username: user,
                key: key
            }
            //console.log(json);
            var request = $.ajax({
                type: 'POST',
                url: 'php/add_sshkey.php',
                data: JSON.stringify(json),
                dataType: 'json',
                timeout: 5000,
                success: function(data, status, exception) {
                    if (data.message == 'OK') {
                        $('<div class="alert alert-success" role="alert">Key succesfully configured</div>').appendTo('#messages').fadeOut(3000);
                        //TODO: Check if we need to list all keys or not
                        //KeyService.get(user);
                        var keyType = key.substring(0, 7);
                        var re = /= (.+)$/;
                        var match = re.exec(key);
                        var keyLabel="N/A";
                        if (match != null) {
                            keyLabel = match[1];
                        }
                        $('#ssh_keys_table > tbody:last').prepend('<tr><td>'+keyType+'</td><td>'+keyLabel+'</td><td></td></tr>');
                    } else {
                        $('<div class="alert alert-danger alert-dismissible" role="alert">Unknown error adding key</div>').appendTo('#messages');
                    }
                },
                error: function(xhrObj, textStatus, exception) {
                    $('<div class="alert alert-danger alert-dismissible" role="alert">Error adding key</div>').appendTo('#messages');
                }
            });
            $('<div class="alert alert-info" role="alert">Adding the new key to database</div>').appendTo('#messages').fadeOut(2000);
        } else {
            $('<div class="alert alert-danger" role="alert">You\'ve typed a non valid key</div>').appendTo('#messages').fadeOut(3000);
        }
    }
    ,validate: function(key) {
        keyHead = key.substring(0, 7);
        keyLength = key.indexOf(' ', 9) - 8;
        // DSA
        if (keyHead == 'ssh-dss') {
          if (keyLength < 588)
              return false;
          if (key.indexOf('\'') > - 1)
              return false;
          return true;
        } else if (keyHead == 'ssh-rsa') {
            // RSA
            if (keyLength < 200)
                return false;
            if (key.indexOf('\'') > - 1)
                return false;
            return true;
        }
        return false;
    }
    ,clear: function() {
        document.getElementById('input_key').value = '';
    }
    ,delete: function(key){
        var json = {
            username: user,
            key: key
        }
        var request = $.ajax({
            type: 'POST',
            url: 'php/del_sshkey.php',
            data: JSON.stringify(json),
            dataType: 'json',
            success: function (data) {
                request_sshkeys();
            }
        });
        alert('You have deleted a key.\nDepending on the server load, database performance, number of registered keys and'
        + ' several other factors this operation can take a little. The page will reload information relative to'
        + ' sshkeys automatically after this operation ends.');
    }
};

var IPService = {
    clear: function() {
        document.getElementById('input_ip').value = '';
    }
    ,add: function() {
        var ip = document.getElementById('input_ip').value;
        if (this.validate(ip)) {
            var json = {
                username: user,
                ip: ip 
            }
            var request = $.ajax({
                type: 'POST',
                url: 'php/add_ip.php',
                data: JSON.stringify(json),
                dataType: 'json',
                success: function (data) {
                    if (data.message == 'OK') {
                        $('<div class="alert alert-success" role="alert">IP succesfully configured</div>').appendTo('#messages').fadeOut(3000);
                        //TODO: Check if we need to list all keys or not
                        //IPService.get(user);
                        $('#ip_keys_table > tbody:last').prepend('<tr><td>'+ip+'</td><td>DELETE</td></tr>');
                    } else {
                        $('<div class="alert alert-danger alert-dismissible" role="alert">Unknown error adding IP</div>').appendTo('#ipmessages');
                    }
                },
                error: function(xhrObj, textStatus, exception) {
                    $('<div class="alert alert-danger alert-dismissible" role="alert">Error adding IP</div>').appendTo('#ipmessages');
                }
            });
            $('<div class="alert alert-info" role="alert">Adding the new IP to database</div>').appendTo('#ipmessages').fadeOut(2000);
        } else {
            $('<div class="alert alert-danger" role="alert">You\'ve typed a non valid IP</div>').appendTo('#ipmessages').fadeOut(3000);
        }
    }
    ,validate: function(ip) {
        function isValidIPChar(c) {
            validChars = [ '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.', '/' ];
            for (var i = 0; i < validChars.length; i++) {
                if (validChars[i] == c)
                    return true;
            }
            return false;
        }
        dotCount = 0;
        barCount = 0;
        for (var i = 0; i < ip.length; i++) {
            if (!isValidIPChar(ip.charAt(i))) { // Es una caracter no valido
                console.log('No valid char found: ' + ip.charAt(i));
                return false;
            } else { // Es un caracter valido
                if (ip.charAt(i) == '.') { // Es un punto
                    dotCount++;
                    if (dotCount > 3)
                    return false;
                }
                if (ip.charAt(i) == '/') { // Es una barra
                    barCount++;
                    if (barCount > 1)
                    return false;
                }
            }
        }
        if (dotCount < 3) // No hay suficientes puntos como para que sea una IP completa
        return false;
        splitted = ip.split('.');
        for (var i = 0; i < splitted.length && i <= 3; i++) {
            var num = parseInt(splitted[i]);
            if (num > 255)
            return false;
        }
        return true;
    }
    ,get: function(user) {
        var json = {
            user: user
        }
        var request = $.ajax({
            type: 'POST',
            url: 'php/get_ips.php',
            data: JSON.stringify(json),
            dataType: 'json',
            success: function (data) {
                var jsonData = jQuery.parseJSON(data);
                var print = 'You have not any IP stored in our database.';
                if (jsonData.ips.length > 0) {
                    print = '';
                    for (var i = 0; i < jsonData.ips.length; i++) {
                        label = '<span class="spanResponseLabel">IP </span>';
                        print += label + jsonData.ips[i]
                        + '<span class="spanStopCluster" onclick="deleteIp(\'' + jsonData.ips[i] + '\')">DELETE</span><br/>'
                        + '<br/>';
                    }
                }
                document.getElementById('addIPResponseDiv').innerHTML = print;
            }
        });
    }
    ,delete: function(ip) {
        var json = {
            username: user,
            ip: ip
        }
        var request = $.ajax({
            type: 'POST',
            url: 'php/del_ip.php',
            data: JSON.stringify(json),
            dataType: 'json',
            success: function (data) {
                request_ips();
            }
        });
        alert('You have deleted an IP.\nDepending on the server load, database performance, number of registered IPs and'
        + ' several other factors this operation can take a little. The page will reload information relative to'
        + ' IPs automatically after this operation ends.');
    }
};

// TODO: Para que sirve esto?
var x = 0 // variable declarad e iniciada como cero
function operacion() {
    if (x == 0) {
        //solo entra aquí cuando x sea cero
        document.getElementById('input_ip').value = document.getElementById('texto').value;
        x++; // variable cambia a uno despues de copiar el texto.
    } else {
        //solo entra aquí cuando x sea uno
        document.formulario.submit(); //aquí envias el form
    }
}

function closeD() {
    close();
}

// Setup: https://github.com/mstratman/jQuery-Smart-Wizard
$(document).ready(function () {
    // Smart Wizard   
    $('#wizard').smartWizard({
        onFinish: onFinishCallback
    });
    function onFinishCallback() {
        $('#wizard').smartWizard('showMessage', 'Finish Clicked');
    }
}
);

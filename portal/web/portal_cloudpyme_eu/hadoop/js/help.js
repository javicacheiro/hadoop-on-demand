function doHelp(){
	hideMenu();
	
	clearAllContentDivs();
	hideAllContentDivs();
	
	document.getElementById("helpDiv").style.display="block";
	
	document.getElementById("helpDiv").innerHTML = "<h1>Help</h1><br/>"
		+"<h2>Launching a new cluster</h2>"
		+'<p>To start a new Hadoop cluster simply press the "Launch Cluster" button.</p>'
		+'<p>Then specify the number of slave nodes that you want (the cluster will additionally launch a master node automatically).</p>'
		+'<p>You can also configure advanced features like number of replicas, block size, etc. if you press the Advanced features option.</p>'
		+'<p>Finally just wait until the cluster is started. If you want you can watch the progress of the deployment process pressing the Watch Progress option.</p>'
		+'<br/><h2>Connecting to the cluster</h2>'
		+'<p>To connect to the cluster master node just use the information provided at the end of the process.</p>'
		+'<p>You can always retrieve again the connection details of a given cluster using the Info option in the main page.</p>'
		+'<br/><h3>Firewall</h3>'
		+'<p>The cluster is protected by a firewall that, by default, allows only access from CESGA servers.</p>'
		+'<p>If you want to access the cluster from other computers you have to include their public IP using the <a href="#" onclick="doIps()">Firewall Configuration</a> section.</p>'
		+'<h3>Login Credentials (SSH Keys)</h3>'
		+'<p>The access to the cluster is controlled using SSH keys.</p>'
		+'<p>In order to connect to the cluster you have to upload first your SSH Public Key using the <a href="#" onclick="doKeys()">Login Credentials</a> section.</p>'
		+'<p>You can find more information about how to generate your SSH Public Key in the following links:</p>'
		+'<ul>'
		+'<li><a href="http://kb.siteground.com/how_to_generate_an_ssh_key_on_windows_using_putty" target="_blank">Windows</a></li>'
		+'<li>On Linux/Mac just use the \'<i>ssh-keygen -t dsa</i>\' command</li>'
		+'</ul><br/>'
		+'<br/><h2>Stopping a running cluster</h2>'
		+'<p>To stop a running cluster just press the "Delete" icon.</p>'
		+'<p>WARNING: When you stop a cluster all the information is lost and can not be recovered so please copy all the data that you will need before pressing the "Delete" button.</p>'
		+'<br/><h2>Support and additional help</h2>'
		+"In case of problems don't hesitate to contact CESGA Systems Department:<br/><br/>"
		+"<span class=\"spanWhiteSpace\"/><span class=\"spanWhiteSpace\"/>Email: sistemas@cesga.es<br/>"
		+"<span class=\"spanWhiteSpace\"/><span class=\"spanWhiteSpace\"/>Phone: 981569810";
}
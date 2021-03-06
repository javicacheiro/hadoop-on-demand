
var advancedMode = false;

var ClusterService = {
  // Default values
  size: 3,
  replicas: 3,
  blocksize: 16,
  reduce: 1,

  tooltipNodes: function () {
    document.getElementById("tipDiv").style.visibility = "visible";
    document.getElementById("tipDiv").innerHTML = "Number of nodes (without including master node).";
  }
  ,tooltipReplicas: function() {
    document.getElementById("tipDiv").style.visibility="visible";
    document.getElementById("tipDiv").innerHTML="The default number of replicas for each block.";
  }
  ,tooltipBlockSize: function(){
    document.getElementById("tipDiv").style.visibility="visible";
    document.getElementById("tipDiv").innerHTML="The default block size for new files, in MB.";
  }
  ,tooltipReduceTasksNumber: function(){
    document.getElementById("tipDiv").style.visibility="visible";
    document.getElementById("tipDiv").innerHTML="The default number of reduce tasks.";
  }
  ,hide: function (){
    document.getElementById("tipDiv").innerHTML = "";
    document.getElementById("tipDiv").style.visibility="hidden";
  }
  ,save: function(){
    if(document.getElementById("input_size")) 
      ClusterService.size = document.getElementById("input_size").value;
    if(document.getElementById("input_replicas")){
      ClusterService.replicas = document.getElementById("input_replicas").value;
      ClusterService.blocksize = document.getElementById("input_blocksize").value;
      ClusterService.reducers = document.getElementById("input_reducers").value;
    }
  }
  ,validate: function(){

    var hadoopSize = ClusterService.size;
    var hadoopReplicas = ClusterService.replicas;
    var hadoopBlockSize = ClusterService.blocksize;
    var hadoopReduceTasks = ClusterService.reducers;

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
  
    if(hadoopReduceTasks.length < 1){ // If hadoopReduceTasks is empty
      alert('Field for Reduce Tasks Number is empty');
      document.getElementById("input_reducers").focus();
      return false;
    }

    if(isNaN(hadoopReduceTasks)){ // If hadoopReduceTasks is not a number
      alert('The field for Reduce Tasks Number has invalid data (MUST be a numer)');
      document.getElementById("input_reducers").focus();
      return false;
    }

    if(hadoopReduceTasks<0){ // If hadoopReduceTasks is negative number
      alert('Reduce Tasks Number cannot be negative');
      document.getElementById("input_reducers").focus();
      return false;
    }
  
    if(hadoopReduceTasks.indexOf(".")>-1){ // If hadoopReduceTasks is decimal number
      alert('No decimal numbers allowed for Reduce Tasks Number');
      document.getElementById("input_reducers").focus();
      return false;
    }
  
    return true;
  }
  ,request_start_hadoop: function(){
    if(ClusterService.validate()){  
      data = { 
        size : ClusterService.size,
        replicas : ClusterService.replicas,
        blocksize : ClusterService.blocksize,
        reduce : ClusterService.reducers,
        user : user
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
        //document.getElementById("addClusterDiv").innerHTML = data;
        console.log(data);
      }
    });
  }
  ,setup: function(){
    // Set default values
    document.getElementById('input_size').value = ClusterService.size;
    if(document.getElementById('input_replicas')){
      document.getElementById('input_replicas').value = ClusterService.replicas;
      document.getElementById('input_blocksize').value = ClusterService.blocksize;
      document.getElementById('input_reducers').value = ClusterService.reduce;
    }
    // Smart Wizard   
    $('#wizard').smartWizard({
      labelFinish: 'Launch',
      onLeaveStep: leaveAStepCallback,
      onShowStep: showAStepCallback,
      onFinish: onFinishCallback
    });

    function leaveAStepCallback(obj, context) {
      // Save the values the user has introduced in the form
      ClusterService.save();
      // Validate them, if returned value is true then the wizard continues
      return ClusterService.validate();
    }

    function showAStepCallback(obj, context) {
      // TODO: Check why context is null when called and then context.toStep can not be used.
      //       It seems stepNum is defined globally and that the call is different from documentation.
      // In the review step fill the table
      if (stepNum == 3) {
        document.getElementById('review_size').textContent = ClusterService.size;
        document.getElementById('review_replicas').textContent = ClusterService.replicas;
        document.getElementById('review_blocksize').textContent = ClusterService.blocksize;
        document.getElementById('review_reducers').textContent = ClusterService.reduce;
      }
    }
    
    function onFinishCallback(obj, context) {
      console.log("Ready to launch cluster");
      if(ClusterService.validate()){
        ClusterService.request_start_hadoop();
      }
      return true;
    }

    //TODO: Add custom logic to validate each step using context.fromStep (see example in smartwizard page)
  }
};

$(ClusterService.setup);


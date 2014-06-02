<body>
    
<!-- CABECERA -->
<div id="header_portal">       
    
    <div id="logo">
        <a target="_blank" href="http://www.cloudpyme.eu/" title="CloudPYME web"><img src="http://portal.cloudpyme.eu/iconos/cloudpyme.png" width="250px" alt="CloudPYME web"></a>
    </div>
       
</div>
<!-- FIN CABECERA -->


   
        
    <!-- MENÚ NAVEGACIÓN -->
<nav class="navbar navbar-default" role="navigation">
     <div class="container-fluid">
         
        <!-- Collect the nav links, forms, and other content for toggling -->
       <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
       <ul class="nav navbar-nav">

                <li>
                    <a href="http://portal.cloudpyme.eu/">
                      <span class="fa-stack fa">
                                <i class="fa fa-square-o fa-stack-2x"></i>
                                <i class="fa fa-home fa-stack-1x"></i>
                      </span><?=get_vocab("inicio");?>         
                    </a>                   
                </li>
           
                                      
                <li class="active">
                    <a href="http://portal.cloudpyme.eu/gestion">
                    <span class="fa-stack fa">
                       <i class="fa fa-square-o fa-stack-2x"></i>
                       <i class="fa fa-desktop fa-stack-1x"></i>
                     </span><?=get_vocab("computo");?>              
                   </a>
               </li> 
                 
                   
                  <li>
                      <a href="https://ochpc.cesga.es">
                        <span class="fa-stack fa">
                           <i class="fa fa-square-o fa-stack-2x"></i>
                           <i class="fa fa-cloud-upload fa-stack-1x"></i>
                         </span><?=get_vocab("almacenamiento");?>               
                      </a>
                 </li>
               
                 <li>
                      <a href="http://portal.cloudpyme.eu/soporte">
                        <span class="fa-stack fa">
                           <i class="fa fa-square-o fa-stack-2x"></i>
                           <i class="fa fa-edit fa-stack-1x"></i>
                         </span><?=get_vocab("soporte");?>               
                      </a>
                 </li>
                  
                 <li>
                      <a href="http://portal.cloudpyme.eu/doc">
                        <span class="fa-stack fa">
                              <i class="fa fa-square-o fa-stack-2x"></i>
                              <i class="fa fa-archive fa-stack-1x"></i>
                         </span><?=get_vocab("documentacion");?>                  
                      </a>
                </li>
      </ul>
       
        
      <ul class="nav navbar-nav navbar-right">

         <?php
            print "<li><a href=\"".$_SERVER["PHP_SELF"]."?lang=es\"><img src=\"http://portal.cloudpyme.eu/iconos/es.png\"/></a></li>";
            print "<li><a href=\"".$_SERVER["PHP_SELF"]."?lang=pt\"><img src=\"http://portal.cloudpyme.eu/iconos/pt.png\"/></a></li>";
            print "<li><a href=\"".$_SERVER["PHP_SELF"]."?lang=en\"><img src=\"http://portal.cloudpyme.eu/iconos/en.png\"/></a></li>";
         ?>  
 
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i class="fa fa-user fa-fw"></i> <?php echo strtoupper($user); ?><b class="caret"></b></a>
          <ul class="dropdown-menu">
            <li><a href="#"><i class="fa fa-cog fa-fw"></i> <?=get_vocab("perfil");?></a></li>
            <li class="divider"></li>
            <li><a href="javascript:Logout();"><i class="fa fa-sign-out fa-fw"></i> <?=get_vocab("desconectar");?></a></li>
          </ul>
        </li>
      </ul>
           
           
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>
<!-- END MENÚ NAVEGACIÓN -->



<!-- INICIO DE LOGOUT -->
<form name="logout" method="post" action="logout.php">
 <div>
  <input type="hidden" name="TargetURL" value="/index.php">
  <input type="hidden" name="Action" value="SetName">
  <input type="hidden" name="NewUserName" value="">
  <input type="hidden" name="NewUserPassword" value="">
 </div>
</form>

<SCRIPT LANGUAGE="JavaScript">
 function Logout()
 {
        var answer = confirm ("Esta seguro de querer finalizar esta session");
        if (answer)
        {
                document.logout.submit();
        }
 }
</SCRIPT>
<!-- FIN DE LOGOUT -->


hadoop-on-demand
================

Utilities to create Hadoop clusters on demand.

Supported cloud platforms:
 - Open Nebula
 - Amazon EC2

Basic usage:
  Create a Hadoop cluster with 10 slaves:
    hadoop-start -s 10

  Connect to the cluster:
    hadoop-connect
    
  Check the status of the cluster:
    hadoop-status

  Stop the cluster:
    hadoop-stop

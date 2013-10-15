#!/bin/bash

MASTER=`head -n1 node.list`
scp run_benchmarks.sh root@$MASTER:/home/hadoop/
ssh root@$MASTER "
	chown hadoop /home/hadoop/*.sh
	su - hadoop -c '
		#module load hadoop
		./run_benchmarks.sh
	'
	"
scp root@$MASTER:/home/hadoop/run_benchmarks.log .

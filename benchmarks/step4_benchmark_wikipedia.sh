#!/bin/bash

MASTER=`head -n1 node.list`
scp run_benchmark_wikipedia.sh root@$MASTER:/home/hadoop/
ssh root@$MASTER "
	chown hadoop /home/hadoop/*.sh
	su - hadoop -c '
		module load hadoop
		./run_benchmark_wikipedia.sh
	'
	"
scp root@$MASTER:/home/hadoop/run_benchmark_wikipedia.log .

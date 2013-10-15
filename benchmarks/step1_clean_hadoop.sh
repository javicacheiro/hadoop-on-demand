NODES=`cat node.list`
MASTER=`head -n1 node.list`

ssh root@$MASTER <<EOF
	su - hadoop -c '
		/opt/cesga/hadoop/bin/stop-all.sh
	'
EOF

for node in $NODES; do ssh root@$node rm -rf /scratch/hadoop/*; done

# Start it again
#ssh root@$MASTER <<EOF
#	su - hadoop -c '
#		hadoop namenode -format
#		/opt/cesga/hadoop/bin/start-all.sh
#	'
#EOF

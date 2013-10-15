#!/bin/bash

NODES=`cat node.list`

N=0

for node in $NODES; do

  echo "Configuring node $node"

  echo " ... hostname"
  # Set node name
  if [[ $N == 0 ]]; then
	nodename="hadoop-master"
	let "N++"
  else
	#nodename="hadoop-`printf '%02d' $N`"
	nodename="hadoop-$N"
	let "N++"
  fi
  scp node.list root@$node:
  scp iptables root@$node:/etc/sysconfig/iptables
  scp conf/* root@$node:/opt/cesga/hadoop/conf/

  echo "... hadoop"
  ssh root@$node <<EOF

  	echo "127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4" > /etc/hosts
	echo "::1         localhost localhost.localdomain localhost6 localhost6.localdomain6" >> /etc/hosts
	MASTER=`head -n1 node.list`
	# /etc/hosts
        echo "\$MASTER hadoop-master" >> /etc/hosts


	# Generate hadoop slaves
	sed '1d' node.list > /opt/cesga/hadoop/conf/slaves

	n=1
	for slave in \`cat /opt/cesga/hadoop/conf/slaves\`; do
		#echo "\$slave hadoop-`printf '%02d' \$n`" >> /etc/hosts
		echo "\$slave hadoop-\$n" >> /etc/hosts
		let "n++"
	done

	# Change hostname (it is afterwards displayed in hadoop monitoring page)
	hostname $nodename
	cp /etc/sysconfig/network /etc/sysconfig/network.0
	sed "s/HOSTNAME=localhost/HOSTNAME=$nodename/" /etc/sysconfig/network.0 > /etc/sysconfig/network

	# Restart firewall
	service iptables restart
EOF

done

MASTER=`head -n1 node.list`
scp run_benchmarks.sh root@$MASTER:/home/hadoop/
ssh root@$MASTER "
	echo "StrictHostKeyChecking no" >> /etc/ssh/ssh_config
	chown hadoop /home/hadoop/*.sh
	su - hadoop -c '
		module load hadoop
		hadoop namenode -format
		cd /opt/cesga/hadoop/bin
		#./start-dfs.sh
		#./start-mapred.sh
		./start-all.sh
	'
	"

#!/bin/bash

exec &> run_benchmarks.log

echo "==> Download databases"
#time wget --quiet http://cloud.cesga.es/tmp/EncyclopaediaBritannica1911.tar.gz
time wget --quiet http://vps3.cesga.es/EncyclopaediaBritannica1911.tar.gz
time tar xzf EncyclopaediaBritannica1911.tar.gz

module load hadoop

echo "==> Hadoop cluster status"
hadoop dfsadmin -report | grep "Datanodes available"
echo -ne "Tasktrackers available "
hadoop job -list-active-trackers|wc -l
echo "===> HDFS"
hadoop dfsadmin -report
echo "===> Task trackers"
hadoop job -list-active-trackers

hadoop fs -mkdir /user/hadoop/out

echo "==> hadoop fs -put EncyclopaediaBritannica1911 ."
for i in `seq 1 4`; do
	echo "===> Run $i:"
	time hadoop fs -put EncyclopaediaBritannica1911 .
	hadoop fs -rmr /user/hadoop/EncyclopaediaBritannica1911
done
echo "===> Run 5"
time hadoop fs -put EncyclopaediaBritannica1911 .

for i in `seq 1 5`; do
	echo "===> Run $i:"
	time hadoop jar /opt/cesga/hadoop/hadoop-examples-1.0.3.jar wordcount EncyclopaediaBritannica1911 EncyclopaediaBritannica1911-output-$i
done


# time hadoop fs -put wikipedia .

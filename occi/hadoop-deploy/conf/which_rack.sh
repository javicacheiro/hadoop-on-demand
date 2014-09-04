#!/bin/bash
#grep GATEWAY /etc/sysconfig/network-scripts/ifcfg-eth0 | sed 's/GATEWAY=/\//'
GW=`route | grep '^default'| awk '{print $2}' `
echo "/${GW}/default-rack"

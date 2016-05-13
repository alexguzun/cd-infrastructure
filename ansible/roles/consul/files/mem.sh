#!/usr/bin/env bash

set -- $(free -m | awk 'NR==2{print $2 " "$4 " " $7 }')
total=$1
free_mem=$2
cached_mem=$3
used_percent=$(( (free_mem+cached_mem) * 100 / $total ))
printf "Memory Usage: Free:%s / Total:%sMB (%s%%)\n" $((free_mem+cached_mem)) $total $used_percent
if [ $used_percent -lt 20 ]; then
  exit 2
elif [ $used_percent -lt 25 ]; then
  exit 1
else
  exit 0
fi
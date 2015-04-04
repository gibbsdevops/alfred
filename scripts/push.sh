#!/bin/bash

for file in "$@"
do
  curl -X POST -H 'Content-Type: application/json' -H 'X-Github-Event: push' -H 'X-Github-Delivery: test-guid-1234' --data @$file http://localhost:8080/api/ingest
done

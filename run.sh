#!/bin/bash

./scripts/build

clear

java -jar target/drone-delivery-service-1.0-SNAPSHOT-jar-with-dependencies.jar -f "$1"
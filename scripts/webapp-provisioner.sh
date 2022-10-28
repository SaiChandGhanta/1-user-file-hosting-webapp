#!/bin/bash

sleep 30
sudo apt-get update -y

#install java
sudo apt-get install openjdk-17-jdk -y
java --version

#postgresql14-client
sudo apt-get install -y postgresql-client

cd ~ || exit

#Ensure packer file placed this at tmp, before using it.
sudo mv /tmp/webservice.service /etc/systemd/system/
sudo chmod u+x /etc/systemd/system/webservice.service
sudo systemctl daemon-reload
systemctl status webservice.service -l
sudo systemctl enable webservice.service






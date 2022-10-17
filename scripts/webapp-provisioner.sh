#!/bin/bash

sleep 30
sudo apt-get update -y

#install java
sudo apt-get install openjdk-17-jdk -y
java --version

#postgresql13-server
sudo apt-get install postgresql-14 -y
sudo psql --version
sudo -i -u postgres
psql
ALTER USER postgres PASSWORD 'postgres';
CREATE ROLE saichand SUPERUSER CREATEDB CREATEROLE LOGIN PASSWORD 'saichand';
sudo systemctl restart postgresql@14-main.service
sleep 10

cd ~ || exit

#Ensure packer file placed this at tmp, before using it.
sudo mv /tmp/webservice.service /etc/systemd/system/
chmod u+x /etc/systemd/system/webservice.service
sudo systemctl daemon-reload
systemctl status webservice.service -l
#check the ports availability
sudo lsof -PiTCP -sTCP:LISTEN





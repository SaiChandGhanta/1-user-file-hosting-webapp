#!/bin/bash

sleep 30
sudo apt-get update -y

#install java
sudo apt-get install openjdk-17-jdk -y
java --version

#postgresql13-server
sudo apt-get install postgresql-14 -y

#sudo /usr/pgsql-13/bin/postgresql-13-setup initdb
#sudo systemctl enable --now postgresql-13
#systemctl status postgresql-13
sudo psql --version
#sudo -i -u postgres
#psql
#ALTER USER postgres PASSWORD 'postgres';
#sudo systemctl restart postgresql-13
#check the ports availability
#sudo lsof -PiTCP -sTCP:LISTEN
#sudo -u postgres psql -U postgres -c "CREATE ROLE bipin SUPERUSER CREATEDB CREATEROLE LOGIN PASSWORD 'bipin';"
#sudo systemctl restart postgresql-13
#sleep 10

sudo psql --version
sudo -i -u postgres
psql
ALTER USER postgres PASSWORD 'postgres';
sudo -u postgres psql -U postgres -c "CREATE ROLE saichand SUPERUSER CREATEDB CREATEROLE LOGIN PASSWORD 'saichand';"
sudo systemctl restart postgresql@14-main.service
sleep 10

cd ~ || exit

#Ensure packer file placed this at tmp, before using it.
sudo mv /tmp/webservice.service /etc/systemd/system/
sudo chmod u+x /etc/systemd/system/webservice.service
sudo systemctl daemon-reload
systemctl status webservice.service -l
sudo systemctl enable webservice.service
#check the ports availability
sudo lsof -PiTCP -sTCP:LISTEN





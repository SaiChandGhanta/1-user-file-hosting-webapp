## Notes

- For EC2 AMI creation with baked in postgres13, java17.
  Refer `./scripts/ami.pkr.hcl`,`./scripts/create-ami.sh`
- Webservice as a systemmd process is taken care of by -
  Refer `./scripts/webservice.service`,`./scripts/webapp-provisioner.sh`

## Helpful commands

packer init .
packer validate .
packer build ami.pkr.hcl

packer build \
-var 'aws_access_key=<>' \
-var 'aws_secret_key=<>' \
-var 'aws_region=us-east-1' \
ami.pkr.hcl --debug

[//]: # (-var-file=abc.json)

- For connection to rds-postgres from ec2(having postgres client)
```shell
psql \
   --host=csye6225.cid9ix5hrysq.us-east-1.rds.amazonaws.com \
   --port=5432 \
   --username=csye6225 \
   --password \
   --dbname=csye6225
```

- For starting or stopping the service
```shell
sudo systemctl start webservice.service
sudo systemctl stop webservice.service
```

- Webapp configuration for ec2 can be changed at:

`/home/ec2-user/workspace/application.properties`

- Running Jmeter load tests:


`jmeter -n -t health-ltest.jmx -f -l health-ltest.jtl -e -o health-ltest -Jthreads=1 -Jloops=1`

- or

```shell
jmeter -n -t health-ltest.jmx \
-f -l health-ltest.jtl \
-e -o health-ltest \
-Jthreads=100 -Jloops=100 \
-Jhost=dev.bipin-mandava.me -Jport=80
```

- Note:
  - Jmeter should be installed and should be available in path or via executable directly.
  - Do not modify the jmx file directly. Use jmeter UI to edit/enhance/debug. Then use CLI for load testing.
  - The current file has threads, loops, host, port set as properties and does GET request on /healthz endpoint.
  - You may use the *.jtl for logs and folder/index.html for loadTest report.
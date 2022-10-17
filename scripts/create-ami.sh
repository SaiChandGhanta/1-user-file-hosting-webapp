#!/bin/bash

export AWS_PROFILE=dev

packer build \
ami.pkr.hcl
#-var 'subnet_id=subnet-0165716c64ce7d528' \
#-var 'ssh_username=root' \


unset AWS_PROFILE
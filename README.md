
Based on great example from @vfarcic : [https://github.com/vfarcic/docker-swarm](https://github.com/vfarcic/docker-swarm). Also highly recommend his series of articles on the topic: [Scaling To Infinity with Docker Swarm, Docker Compose and Consul](https://technologyconversations.com/2015/07/02/scaling-to-infinity-with-docker-swarm-docker-compose-and-consul-part-14-a-taste-of-what-is-to-come/)ual-services/)

## Technologies
- [Docker](https://www.docker.com/what-docker)
	- Docker Swarm cluster for running application containers
	- Docker Repository (or Registry) for storing the images
- [Consul](https://www.consul.io/intro/)
	- Consul Master server 
	- Consul Agent installed on each other servers
	- [consul-template](https://github.com/hashicorp/consul-template) for configuration management and automatic reload.
	- [registrator](https://github.com/gliderlabs/registrator), executed as a docker container, for services registration and monitoring.
- [Ansible](http://docs.ansible.com/ansible/)
- CI server ( [Jenkins](https://jenkins.io) )
- Git Repository
	The example uses [GitLab Community Edition](http://docs.gitlab.com/omnibus/docker/), executed as a docker container.
- Load Balancer
	The example uses [HAProxy](http://www.haproxy.org/#desc), feed with the configuration from the Consul

Almost all infrastructure set-up and configuration can be automated with an Ansible Playbook. Unfortunately there are some manual configuration steps that can not be automated. See [manual steps](#manual-steps).

## Overview
![CD infrastructure overview](https://raw.githubusercontent.com/alexguzun/cd-infrastructure/master/img/infra.png)

## Delivery Workflow
An usual deployment workflow will have these steps:

1. The developer pushes a new version of the code to the Git Repository
2. Git Repository send a notification to the CI server
3. CI server executes the defined job or a pipeline of jobs, that include:
	- code compilation
	- execution of tests and code quality checks
	- building of a Docker Image
	- image is pushed to Docker Registry	
	- the Ansible Playbook is executed, in which the Docker Swarm master is instructed to deploy new version of the image
4. Docker Swarm Master pulls the new image from Docker Registry
5. Docker Swarm Master identifies on which Slave node ( or nodes if multiple instances are required ) to deploy the image
6. Docker Slave Node received the deploy instruction and runs the container
7. Local Consul Agent setup identifies the change in service configuration and updates the configuration on Consul Master
8. consul-template on Load Balancer identifies the change in service configuration and updates the Balancer configuration with new ip and ports of the service.
9. Load Balancer starts to forward requests to new instances of the application. 
 
##Manual steps:

- Gitlab ( http://10.100.199.203/)
	- root user password needs to be changed on fist login
	- create new public project named _test_service_
	- jenkins hook needs to be configured in the Settings > Webhooks
		_http://10.100.199.200:8080/gitlab/build_now_
- Jenkins (http://10.100.199.200:8080/)
	- slave needs to be manually added using following steps.
		1. Click Manage Jenkins > Manage Nodes > New Node
		2. Name it cd, select Dumb Slave and click OK
		3. Type /data/jenkins/slaves/cd as Remote Root Directory
		4. Type 10.100.199.200 as Host
		5. Select Launch slave agents on Unix machines via SSH
		6. Click Add* next to **Credentials
		7. Use vagrant as both Username and Password and click Add
		8. Click Save
	- manually configure ansible:
		1. Click Manage Jenkins > System Config
		2. Find Ansible plugin config
		3. Type *ansible* in Name field
		4. Type */usr/local/bin* in _Path to ansible executables directory_ field
	- manually configure maven:
		1. Click Manage Jenkins >  System Config
		2. Find Maven plugin config
		3. Type *maven* in Name field
		4. Select _Install automatically_ option
- Test service
	- add the created repo to the existing project
		git remote add origin http://10.100.199.203/root/test_service.git

##TODO

- Jenkins
	- create pipeline
	- migrate to jenkins 2.0
	- automate the creation of slave
	- automate the configuration of plugins
- Consul
	- use separate config files for server and agent
	- configure consul for local dns lookup
- Gitlab
	- automate the configuration of jenkins hook
	- implement git workflow
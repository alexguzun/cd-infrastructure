#!/bin/bash

echo "Installing Ansible..."
apt-get install -y python-pip python-dev libssl-dev libffi-dev sshpass
pip install ansible --upgrade

echo "export ANSIBLE_HOST_KEY_CHECKING=False" >> ~/.profile

export ANSIBLE_HOST_KEY_CHECKING=False

# execute the main ansible playbook
ansible-playbook -i /vagrant/ansible/hosts/prod /vagrant/ansible/infra.yml
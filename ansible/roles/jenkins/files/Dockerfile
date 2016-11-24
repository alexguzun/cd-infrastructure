FROM jenkins:2.19.3

# if we want to install via apt
USER root

RUN /usr/local/bin/install-plugins.sh scm-api \
    git-client \
    git \
    ansible \
    gitlab-hook \
    ssh-slaves \
    maven-plugin \
    javadoc \
    workflow-aggregator \
    docker-commons \
    docker-build-publish
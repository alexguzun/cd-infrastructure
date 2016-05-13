FROM alpine

ENV CONSUL_TEMPLATE_VERSION=0.14.0

# Updata wget to get support for SSL
RUN apk --update add haproxy wget

# Download consul-template
RUN ( wget --no-check-certificate https://releases.hashicorp.com/consul-template/${CONSUL_TEMPLATE_VERSION}/consul-template_${CONSUL_TEMPLATE_VERSION}_linux_amd64.zip -O /tmp/consul_template.zip\ 
	&& unzip /tmp/consul_template.zip -d /usr/bin\
	&& rm -rf /tmp/* )
 
COPY haproxy.json /tmp/haproxy.json
COPY haproxy.ctmpl /tmp/haproxy.ctmpl

EXPOSE 80 1936
ENTRYPOINT  ["consul-template", "-config=/tmp/haproxy.json"]
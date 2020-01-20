FROM bitnami/kubectl:1.16.0 as kubectl
FROM golang as golang
FROM docker:stable-dind as dind

FROM jenkins/jnlp-slave:3.27-1
USER root

RUN apt-get update && apt-get install -y \
      gcc \
      python3-pip \
      postgresql-client && \
      pip3 install awscli && \
      curl https://raw.githubusercontent.com/helm/helm/master/scripts/get | bash

COPY --from=kubectl /opt/bitnami/kubectl/bin/kubectl /usr/local/bin/kubectl

COPY --from=dind /usr/local/bin/docker /usr/local/bin/docker

COPY --from=golang  /usr/local/go  /usr/local/go
ENV GOPATH /home/jenkins/go
ENV PATH $GOPATH/bin:/usr/local/go/bin:$PATH

RUN go get -u github.com/jstemmer/go-junit-report

USER jenkins
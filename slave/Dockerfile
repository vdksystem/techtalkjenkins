FROM bitnami/kubectl:1.16.0 as kubectl
FROM golang as golang

FROM jenkins/jnlp-slave:3.27-1
USER root

RUN apt-get update && apt-get install -y gcc

COPY --from=kubectl /opt/bitnami/kubectl/bin/kubectl /usr/local/bin/kubectl

COPY --from=golang  /usr/local/go  /usr/local/go
ENV GOPATH /go
ENV PATH $GOPATH/bin:/usr/local/go/bin:$PATH

USER jenkins
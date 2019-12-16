FROM bitnami/kubectl:1.16.0 as kubectl

FROM jenkins/jnlp-slave:3.27-1
USER root

COPY --from=kubectl /opt/bitnami/kubectl/bin/kubectl /usr/local/bin/kubectl

RUN apt-get update && apt-get install -y \
    gcc \
    curl \
    unzip && \
    curl https://raw.githubusercontent.com/helm/helm/master/scripts/get | bash
# TODO: install helm s3 plugin
USER jenkins
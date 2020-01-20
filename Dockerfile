FROM golang:alpine
WORKDIR /code
COPY . .
RUN go build -o app

FROM alpine:3.7
MAINTAINER Dmitry Kuleshov <dmytro.kuleshov@clarizen.com>

WORKDIR /app
COPY --from=0 /code/app .

EXPOSE 8080
ENTRYPOINT ["/app/app"]
FROM golang:alpine
WORKDIR /code
COPY . .
RUN go build -o main

FROM alpine:3.7
MAINTAINER Dmitry Kuleshov <dmytro.kuleshov@clarizen.com>

WORKDIR /app
COPY --from=0 /code/main .

EXPOSE 8080
ENTRYPOINT ["/app/main"]
FROM ubuntu:latest
LABEL authors="sakhile"

WORKDIR /app

ADD shared/ shared/
ADD build/ build/

ENTRYPOINT build/api-gateway

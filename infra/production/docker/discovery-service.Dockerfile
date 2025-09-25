FROM golang:1.23 as builder
LABEL authors="sakhile"

WORKDIR /app
COPY . .
WORKDIR /app/services/discovery-service
RUN CGO_ENABLED=0 GOOS=linux go build -o discovery-service

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /root/
COPY --from=builder /app/services/discovery-service/discovery-service .
EXPOSE 8761

CMD ["./discovery-service"]

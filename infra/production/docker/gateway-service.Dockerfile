FROM golang:1.23 as builder
LABEL authors="sakhile"

WORKDIR /app
COPY . .
WORKDIR /app/services/gateway-service
RUN CGO_ENABLED=0 GOOS=linux go build -o gateway-service

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /root/
COPY --from=builder /app/services/gateway-service/gateway-service .
EXPOSE 8080

CMD ["./gateway-service"]

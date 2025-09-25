FROM golang:1.23 as builder
LABEL authors="sakhile"

WORKDIR /app
COPY . .
WORKDIR /app/services/config-service
RUN CGO_ENABLED=0 GOOS=linux go build -o config-service

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /root/
COPY --from=builder /app/services/config-service/config-service .
EXPOSE 8082

CMD ["./config-service"]

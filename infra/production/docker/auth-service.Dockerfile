FROM golang:1.23 as builder
LABEL authors="sakhile"

WORKDIR /app
COPY . .
WORKDIR /app/services/auth-service
RUN CGO_ENABLED=0 GOOS=linux go build -o auth-service

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /root/
COPY --from=builder /app/services/auth-service/auth-service .
EXPOSE 8081

CMD ["./auth-service"]

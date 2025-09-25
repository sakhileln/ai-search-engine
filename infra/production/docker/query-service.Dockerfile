FROM golang:1.23 as builder
LABEL authors="sakhile"

WORKDIR /app
COPY . .
WORKDIR /app/services/query-service
RUN CGO_ENABLED=0 GOOS=linux go build -o query-service

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /root/
COPY --from=builder /app/services/query-service/query-service .
EXPOSE 8087

CMD ["./query-service"]

FROM golang:1.23 as builder
LABEL authors="sakhile"

WORKDIR /app
COPY . .
WORKDIR /app/services/data-service
RUN CGO_ENABLED=0 GOOS=linux go build -o data-service

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /root/
COPY --from=builder /app/services/data-service/data-service .
EXPOSE 8086

CMD ["./data-service"]

FROM golang:1.23 as builder
LABEL authors="sakhile"

WORKDIR /app
COPY . .
WORKDIR /app/services/search-service
RUN CGO_ENABLED=0 GOOS=linux go build -o search-service

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /root/
COPY --from=builder /app/services/search-service/search-service .
EXPOSE 8088

CMD ["./search-service"]

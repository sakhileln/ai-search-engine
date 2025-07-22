# ai search engine
An AI-powered search and question-answering platform that combines web search, natural language processing, and large language models to deliver concise, accurate, and context-aware answers to user queries. Unlike traditional search engines, this focuses on providing direct, conversational responses with sourced references, making it ideal for research and knowledge discovery. This project draws inspiration from Perplexity's approach to intelligent search and aims to replicate a secure, scalable backend to support similar query processing, data handling, and service orchestration in a production environment.

## Table of Contents
- [Overview](#overview)
- [Running the Application](#running-the-application)
- [Architecture](#architecture)
- [Microservices](#microservices)
- [Security Principles](#security-principles)
- [Technologies](#technologies)
- [Setup and Installation](#setup-and-installation)

## Overview
This project delivers a scalable, secure backend system for a search platform, leveraging Spring Boot microservices. It integrates service discovery, messaging, sharded databases, and containerized deployment, with a strong emphasis on offensive security and observability. The system is designed to handle real-world production workloads while adhering to zero-trust security principles.

## Running the Application
- Clone the project
    ```bash
    git clone https://github.com/sakhileln/ai-search-engine.git
    cd ai-search-engine
    ```
- Run the project
  ```bash
  mvn clean install -DskipTests
  sudo docker-compose up --build -d
  docker-compose logs -f auth-service
  ```
- Start Kubernetes cluster (e.g., Minikube):
    ```bash
    minikube start  
    ```
- Deploy services:
    ```bash
    kubectl apply -f k8s/
    ```
- Access the API via the NGINX gateway:
    ```bash
    curl https://api.secure-search.com/api/v1/query -H "Authorization: Bearer <JWT_TOKEN>"
    ```

## Architecture
The system follows a microservices architecture, orchestrated via Kubernetes, with services communicating through an API gateway. Key components include:
- **Service Discovery**: Consul for dynamic service registration and health checks.
- **Messaging**: Pluggable RabbitMQ or Kafka for asynchronous communication.
- **Database**: Sharded PostgreSQL for scalability and data isolation.
- **Containerization**: Docker for consistent deployments.
- **API Gateway**: NGINX and Spring Cloud Gateway for routing, rate limiting, and TLS.
- **Monitoring**: Prometheus, Grafana, and ELK Stack (planned) for observability.
- **CI/CD**: Jenkins for automated builds, tests, and deployments.

## Microservices
| Service               | Purpose                                      | Key Tech / Security Focus                       |
|------------------------|----------------------------------------------|--------------------------------------------------|
| `auth-service`         | Authentication, authorization, JWT, OAuth2  | Secure session, password hashing, RBAC          |
| `query-service`        | User query intake, routing, validation      | Input validation, audit logging                 |
| `search-service`       | Search, AI model integration (mocked)       | Output encoding, rate limiting                  |
| `data-service`         | Data persistence, PostgreSQL sharding       | SQL injection defense, data encryption          |
| `notification-service` | Async notifications, event processing       | Message integrity, replay protection            |
| `gateway-service`      | API gateway, routing, rate limiting, TLS    | API security, DoS protection                    |
| `config-service`       | Centralized config management               | Secrets management, config validation           |
| `discovery-service`    | Service registry/discovery (Consul)         | Service identity, health checks                 |

## Security Principles
- **Zero Trust**: All service-to-service calls are authenticated and authorized using JWT/OAuth2.
- **Defense in Depth**: Layered security at network, application, and data levels.
- **Observability**: Every request is traceable; security events are logged and monitored.
- **Fail Secure**: Services fail closed on errors to prevent unauthorized access.
- **Continuous Verification**: Security tests and static analysis integrated into CI/CD.

## Technologies
- Language: Java 17+ (Spring Boot)
- Service Discovery: Consul
- Messaging: RabbitMQ or Kafka (pluggable)
- Database: PostgreSQL (sharded)
- Containerization: Docker
- Orchestration: Kubernetes
- CI/CD: Jenkins, Github Actions
- API Gateway: NGINX, Spring Cloud Gateway
- Monitoring: Prometheus, Grafana, ELK Stack (future)
- Security: JWT, OAuth2, secure coding standards

## Contact
- Sakhile L. Ndlazi
- [LinkedIn Profile](https://www.linkedin.com/in/sakhile-)

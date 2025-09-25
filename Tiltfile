# Load the restart_process extension
load('ext://restart_process', 'docker_build_with_restart')

### K8s Config ###

# Uncomment to use secrets
# k8s_yaml('./infra/development/k8s/secrets.yaml')

k8s_yaml([
    './infra/development/k8s/consul.yaml',
    './infra/development/k8s/postgresql.yaml',
    './infra/development/k8s/redis.yaml',
    './infra/development/k8s/elasticsearch.yaml',
    './infra/development/k8s/rabbitmq.yaml'
])

### End of K8s Config ###

### API Gateway ###
gateway_compile_cmd = './mvnw clean compile -DskipTests'
if os.name == 'nt':
    gateway_compile_cmd = '.\\mvnw.cmd clean compile -DskipTests'

local_resource(
    'gateway-compile',
    gateway_compile_cmd,
    deps=['./services/gateway-service'],
    labels="compiles"
)

docker_build_with_restart(
    'ai-search/gateway-service',
    '.',
    dockerfile='./infra/development/docker/gateway-service.Dockerfile',
    only=[
        './services/gateway-service/target/*.jar',
        './infra/development/docker/gateway-service/'
    ],
    live_update=[
        sync('./services/gateway-service/target', '/app'),
    ],
)

k8s_yaml('./infra/development/k8s/gateway-service-deployment.yaml')
k8s_resource('gateway-service', port_forwards=8080, resource_deps=['gateway-compile'], labels="services")
### End of API Gateway ###

### Auth Service ###
auth_compile_cmd = './mvnw clean compile -DskipTests'
if os.name == 'nt':
    auth_compile_cmd = '.\\mvnw.cmd clean compile -DskipTests'

local_resource(
    'auth-service-compile',
    auth_compile_cmd,
    deps=['./services/auth-service'],
    labels="compiles"
)

docker_build_with_restart(
    'ai-search/auth-service',
    '.',
    dockerfile='./infra/development/docker/auth-service.Dockerfile',
    only=[
        './services/auth-service/target/*.jar',
        './infra/development/docker/auth-service/'
    ],
    live_update=[
        sync('./services/auth-service/target', '/app'),
    ],
)

k8s_yaml('./infra/development/k8s/auth-service-deployment.yaml')
k8s_resource('auth-service', port_forwards=8081, resource_deps=['auth-service-compile'], labels="services")
### End of Auth Service ###

### Data Service ###
data_compile_cmd = './mvnw clean compile -DskipTests'
if os.name == 'nt':
    data_compile_cmd = '.\\mvnw.cmd clean compile -DskipTests'

local_resource(
    'data-service-compile',
    data_compile_cmd,
    deps=['./services/data-service'],
    labels="compiles"
)

docker_build_with_restart(
    'ai-search/data-service',
    '.',
    dockerfile='./infra/development/docker/data-service.Dockerfile',
    only=[
        './services/data-service/target/*.jar',
        './infra/development/docker/data-service/'
    ],
    live_update=[
        sync('./services/data-service/target', '/app'),
    ],
)

k8s_yaml('./infra/development/k8s/data-service-deployment.yaml')
k8s_resource('data-service', port_forwards=8086, resource_deps=['data-service-compile'], labels="services")
### End of Data Service ###

### Search Service ###
search_compile_cmd = './mvnw clean compile -DskipTests'
if os.name == 'nt':
    search_compile_cmd = '.\\mvnw.cmd clean compile -DskipTests'

local_resource(
    'search-service-compile',
    search_compile_cmd,
    deps=['./services/search-service'],
    labels="compiles"
)

docker_build_with_restart(
    'ai-search/search-service',
    '.',
    dockerfile='./infra/development/docker/search-service.Dockerfile',
    only=[
        './services/search-service/target/*.jar',
        './infra/development/docker/search-service/'
    ],
    live_update=[
        sync('./services/search-service/target', '/app'),
    ],
)

k8s_yaml('./infra/development/k8s/search-service-deployment.yaml')
k8s_resource('search-service', port_forwards=8088, resource_deps=['search-service-compile'], labels="services")
### End of Search Service ###

### Query Service ###
query_compile_cmd = './mvnw clean compile -DskipTests'
if os.name == 'nt':
    query_compile_cmd = '.\\mvnw.cmd clean compile -DskipTests'

local_resource(
    'query-service-compile',
    query_compile_cmd,
    deps=['./services/query-service'],
    labels="compiles"
)

docker_build_with_restart(
    'ai-search/query-service',
    '.',
    dockerfile='./infra/development/docker/query-service.Dockerfile',
    only=[
        './services/query-service/target/*.jar',
        './infra/development/docker/query-service/'
    ],
    live_update=[
        sync('./services/query-service/target', '/app'),
    ],
)

k8s_yaml('./infra/development/k8s/query-service-deployment.yaml')
k8s_resource('query-service', port_forwards=8087, resource_deps=['query-service-compile'], labels="services")
### End of Query Service ###

### Notification Service ###
notification_compile_cmd = './mvnw clean compile -DskipTests'
if os.name == 'nt':
    notification_compile_cmd = '.\\mvnw.cmd clean compile -DskipTests'

local_resource(
    'notification-service-compile',
    notification_compile_cmd,
    deps=['./services/notification-service'],
    labels="compiles"
)

docker_build_with_restart(
    'ai-search/notification-service',
    '.',
    dockerfile='./infra/development/docker/notification-service.Dockerfile',
    only=[
        './services/notification-service/target/*.jar',
        './infra/development/docker/notification-service/'
    ],
    live_update=[
        sync('./services/notification-service/target', '/app'),
    ],
)

k8s_yaml('./infra/development/k8s/notification-service-deployment.yaml')
k8s_resource('notification-service', port_forwards=8085, resource_deps=['notification-service-compile'], labels="services")
### End of Notification Service ###

### Config Service ###
config_compile_cmd = './mvnw clean compile -DskipTests'
if os.name == 'nt':
    config_compile_cmd = '.\\mvnw.cmd clean compile -DskipTests'

local_resource(
    'config-service-compile',
    config_compile_cmd,
    deps=['./services/config-service'],
    labels="compiles"
)

docker_build_with_restart(
    'ai-search/config-service',
    '.',
    dockerfile='./infra/development/docker/config-service.Dockerfile',
    only=[
        './services/config-service/target/*.jar',
        './infra/development/docker/config-service/'
    ],
    live_update=[
        sync('./services/config-service/target', '/app'),
    ],
)

k8s_yaml('./infra/development/k8s/config-service-deployment.yaml')
k8s_resource('config-service', port_forwards=8082, resource_decat > /home/sakhile/projects/ai-search-engine/Tiltfile << 'EOF'
# Load the restart_process extension
load('ext://restart_process', 'docker_build_with_restart')

### K8s Config ###

# Uncomment to use secrets
# k8s_yaml('./infra/development/k8s/secrets.yaml')

k8s_yaml([
    './infra/development/k8s/consul.yaml',
    './infra/development/k8s/postgresql.yaml',
    './infra/development/k8s/redis.yaml',
    './infra/development/k8s/elasticsearch.yaml',
    './infra/development/k8s/rabbitmq.yaml'
])

### End of K8s Config ###

### API Gateway ###
gateway_compile_cmd = './mvnw clean compile -DskipTests'
if os.name == 'nt':
    gateway_compile_cmd = '.\\mvnw.cmd clean compile -DskipTests'

local_resource(
    'gateway-compile',
    gateway_compile_cmd,
    deps=['./services/gateway-service'],
    labels="compiles"
)

docker_build_with_restart(
    'ai-search/gateway-service',
    '.',
    dockerfile='./infra/development/docker/gateway-service.Dockerfile',
    only=[
        './services/gateway-service/target/*.jar',
        './infra/development/docker/gateway-service/'
    ],
    live_update=[
        sync('./services/gateway-service/target', '/app'),
    ],
)

k8s_yaml('./infra/development/k8s/gateway-service-deployment.yaml')
k8s_resource('gateway-service', port_forwards=8080, resource_deps=['gateway-compile'], labels="services")
### End of API Gateway ###

### Auth Service ###
auth_compile_cmd = './mvnw clean compile -DskipTests'
if os.name == 'nt':
    auth_compile_cmd = '.\\mvnw.cmd clean compile -DskipTests'

local_resource(
    'auth-service-compile',
    auth_compile_cmd,
    deps=['./services/auth-service'],
    labels="compiles"
)

docker_build_with_restart(
    'ai-search/auth-service',
    '.',
    dockerfile='./infra/development/docker/auth-service.Dockerfile',
    only=[
        './services/auth-service/target/*.jar',
        './infra/development/docker/auth-service/'
    ],
    live_update=[
        sync('./services/auth-service/target', '/app'),
    ],
)

k8s_yaml('./infra/development/k8s/auth-service-deployment.yaml')
k8s_resource('auth-service', port_forwards=8081, resource_deps=['auth-service-compile'], labels="services")
### End of Auth Service ###

### Data Service ###
data_compile_cmd = './mvnw clean compile -DskipTests'
if os.name == 'nt':
    data_compile_cmd = '.\\mvnw.cmd clean compile -DskipTests'

local_resource(
    'data-service-compile',
    data_compile_cmd,
    deps=['./services/data-service'],
    labels="compiles"
)

docker_build_with_restart(
    'ai-search/data-service',
    '.',
    dockerfile='./infra/development/docker/data-service.Dockerfile',
    only=[
        './services/data-service/target/*.jar',
        './infra/development/docker/data-service/'
    ],
    live_update=[
        sync('./services/data-service/target', '/app'),
    ],
)

k8s_yaml('./infra/development/k8s/data-service-deployment.yaml')
k8s_resource('data-service', port_forwards=8086, resource_deps=['data-service-compile'], labels="services")
### End of Data Service ###

### Search Service ###
search_compile_cmd = './mvnw clean compile -DskipTests'
if os.name == 'nt':
    search_compile_cmd = '.\\mvnw.cmd clean compile -DskipTests'

local_resource(
    'search-service-compile',
    search_compile_cmd,
    deps=['./services/search-service'],
    labels="compiles"
)

docker_build_with_restart(
    'ai-search/search-service',
    '.',
    dockerfile='./infra/development/docker/search-service.Dockerfile',
    only=[
        './services/search-service/target/*.jar',
        './infra/development/docker/search-service/'
    ],
    live_update=[
        sync('./services/search-service/target', '/app'),
    ],
)

k8s_yaml('./infra/development/k8s/search-service-deployment.yaml')
k8s_resource('search-service', port_forwards=8088, resource_deps=['search-service-compile'], labels="services")
### End of Search Service ###

### Query Service ###
query_compile_cmd = './mvnw clean compile -DskipTests'
if os.name == 'nt':
    query_compile_cmd = '.\\mvnw.cmd clean compile -DskipTests'

local_resource(
    'query-service-compile',
    query_compile_cmd,
    deps=['./services/query-service'],
    labels="compiles"
)

docker_build_with_restart(
    'ai-search/query-service',
    '.',
    dockerfile='./infra/development/docker/query-service.Dockerfile',
    only=[
        './services/query-service/target/*.jar',
        './infra/development/docker/query-service/'
    ],
    live_update=[
        sync('./services/query-service/target', '/app'),
    ],
)

k8s_yaml('./infra/development/k8s/query-service-deployment.yaml')
k8s_resource('query-service', port_forwards=8087, resource_deps=['query-service-compile'], labels="services")
### End of Query Service ###

### Notification Service ###
notification_compile_cmd = './mvnw clean compile -DskipTests'
if os.name == 'nt':
    notification_compile_cmd = '.\\mvnw.cmd clean compile -DskipTests'

local_resource(
    'notification-service-compile',
    notification_compile_cmd,
    deps=['./services/notification-service'],
    labels="compiles"
)

docker_build_with_restart(
    'ai-search/notification-service',
    '.',
    dockerfile='./infra/development/docker/notification-service.Dockerfile',
    only=[
        './services/notification-service/target/*.jar',
        './infra/development/docker/notification-service/'
    ],
    live_update=[
        sync('./services/notification-service/target', '/app'),
    ],
)

k8s_yaml('./infra/development/k8s/notification-service-deployment.yaml')
k8s_resource('notification-service', port_forwards=8085, resource_deps=['notification-service-compile'], labels="services")
### End of Notification Service ###

### Config Service ###
config_compile_cmd = './mvnw clean compile -DskipTests'
if os.name == 'nt':
    config_compile_cmd = '.\\mvnw.cmd clean compile -DskipTests'

local_resource(
    'config-service-compile',
    config_compile_cmd,
    deps=['./services/config-service'],
    labels="compiles"
)

docker_build_with_restart(
    'ai-search/config-service',
    '.',
    dockerfile='./infra/development/docker/config-service.Dockerfile',
    only=[
        './services/config-service/target/*.jar',
        './infra/development/docker/config-service/'
    ],
    live_update=[
        sync('./services/config-service/target', '/app'),
    ],
)

k8s_yaml('./infra/development/k8s/config-service-deployment.yaml')
k8s_resource('config-service', port_forwards=8082, resource_deps=['config-service-compile'], labels="services")
### End of Config Service ###

### Discovery Service ###
discovery_compile_cmd = './mvnw clean compile -DskipTests'
if os.name == 'nt':
    discovery_compile_cmd = '.\\mvnw.cmd clean compile -DskipTests'

local_resource(
    'discovery-service-compile',
    discovery_compile_cmd,
    deps=['./services/discovery-service'],
    labels="compiles"
)

docker_build_with_restart(
    'ai-search/discovery-service',
    '.',
    dockerfile='./infra/development/docker/discovery-service.Dockerfile',
    only=[
        './services/discovery-service/target/*.jar',
        './infra/development/docker/discovery-service/'
    ],
    live_update=[
        sync('./services/discovery-service/target', '/app'),
    ],
)

k8s_yaml('./infra/development/k8s/discovery-service-deployment.yaml')
k8s_resource('discovery-service', port_forwards=8761, resource_deps=['discovery-service-compile'], labels="services")
### End of Discovery Service ###

# Print service URLs
local('echo "\n\n=== Service URLs ==="')
local('echo "API Gateway: http://localhost:8080"')
local('echo "Auth Service: http://localhost:8081"')
local('echo "Config Service: http://localhost:8082"')
local('echo "Discovery Service: http://localhost:8761"')
local('echo "Notification Service: http://localhost:8085"')
local('echo "Data Service: http://localhost:8086"')
local('echo "Query Service: http://localhost:8087"')
local('echo "Search Service: http://localhost:8088"')
local('echo "Consul UI: http://localhost:8500\n"')
EOFps=['config-service-compile'], labels="services")
### End of Config Service ###

### Discovery Service ###
discovery_compile_cmd = './mvnw clean compile -DskipTests'
if os.name == 'nt':
    discovery_compile_cmd = '.\\mvnw.cmd clean compile -DskipTests'

local_resource(
    'discovery-service-compile',
    discovery_compile_cmd,
    deps=['./services/discovery-service'],
    labels="compiles"
)

docker_build_with_restart(
    'ai-search/discovery-service',
    '.',
    dockerfile='./infra/development/docker/discovery-service.Dockerfile',
    only=[
        './services/discovery-service/target/*.jar',
        './infra/development/docker/discovery-service/'
    ],
    live_update=[
        sync('./services/discovery-service/target', '/app'),
    ],
)

k8s_yaml('./infra/development/k8s/discovery-service-deployment.yaml')
k8s_resource('discovery-service', port_forwards=8761, resource_deps=['discovery-service-compile'], labels="services")
### End of Discovery Service ###

# Print service URLs
local('echo "\n\n=== Service URLs ==="')
local('echo "API Gateway: http://localhost:8080"')
local('echo "Auth Service: http://localhost:8081"')
local('echo "Config Service: http://localhost:8082"')
local('echo "Discovery Service: http://localhost:8761"')
local('echo "Notification Service: http://localhost:8085"')
local('echo "Data Service: http://localhost:8086"')
local('echo "Query Service: http://localhost:8087"')
local('echo "Search Service: http://localhost:8088"')
local('echo "Consul UI: http://localhost:8500\n"')

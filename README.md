flowchart TD
    subgraph Client & BFF
      A[Client: Web/Mobile] 
      B[BFF (Backend For Frontend)]
    end

    A --> B
    B -->|Request| C(API Gateway)

    subgraph Giám sát & Cấu hình
      L[Config Server]
      M[Service Registry (Discovery)]
      N[Monitoring & Logging]
    end

    C -->|Định tuyến| M
    M -->|Đăng ký| D[Service A: User Service]
    M -->|Đăng ký| E[Service B: Product Service]
    M -->|Đăng ký| F[Service C: Order Service]
    M -->|Đăng ký| G[Service D: Payment Service]

    %% Giao tiếp đồng bộ qua HTTP/REST
    C --> D
    C --> E
    C --> F
    C --> G

    %% Giao tiếp bất đồng bộ qua Message Broker
    D -- Publish/Subscribe --> H[Message Broker (Kafka/RabbitMQ)]
    E -- Publish/Subscribe --> H
    F -- Publish/Subscribe --> H
    G -- Publish/Subscribe --> H

    %% Các dịch vụ độc lập kết nối đến Database riêng
    D --> I[(Database: User DB)]
    E --> J[(Database: Product DB)]
    F --> K[(Database: Order DB)]
    G --> O[(Database: Payment DB)]

    %% Liên kết giữa các dịch vụ và Config Server
    D -- Fetch config --> L
    E -- Fetch config --> L
    F -- Fetch config --> L
    G -- Fetch config --> L

    %% Monitoring & Logging thu thập dữ liệu từ tất cả dịch vụ
    D -- Log/Metric --> N
    E -- Log/Metric --> N
    F -- Log/Metric --> N
    G -- Log/Metric --> N
    C -- Log/Metric --> N

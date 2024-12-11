# Documentação do Projeto de Pagamentos

## Introdução
Este projeto é uma aplicação baseada em **microservices** para processamento de pagamentos utilizando a arquitetura hexagonal. Ele suporta diferentes tipos de pagamento, como **cartão de crédito**, **cartão de débito** e **Pix**. A aplicação utiliza tecnologias modernas como **Kafka**, **Cassandra**, e **Spring Boot** para escalabilidade, resiliência e desempenho.

---

## Tecnologias Utilizadas
- **Java 21**
- **Spring Boot** (para a aplicação principal)
- **Apache Kafka** (para processamento assíncrono)
- **Cassandra** (para armazenamento de métricas e logs)
- **OpenFeign** (para consumo de APIs externas)
- **Resilience4j** (para implementação de fallback e circuit breaker)
- **Docker** (para conteinerização e execução local)

---

## Estrutura do Projeto

### 1. Camadas Principais
A aplicação segue a **arquitetura hexagonal**, separando as responsabilidades em:

- **Camada de Aplicação**:
  - Controla o fluxo de entrada e saída da aplicação.
  - Contém os **Controllers** para lidar com requisições REST e interações com Kafka.

- **Camada de Domínio**:
  - Contém as regras de negócio e a lógica central da aplicação.
  - Inclui os **serviços** e as **entidades** do sistema.

- **Camada de Infraestrutura**:
  - Gerencia a comunicação com tecnologias externas, como o Kafka, Cassandra, e APIs externas.
  - Configura os consumidores e produtores Kafka e o cliente Feign.

### 2. Estrutura de Pacotes
- `com.exemplo.pagamentos.application`:
  - Contém os **Controllers** e DTOs da aplicação.
- `com.exemplo.pagamentos.domain`:
  - Inclui as entidades e serviços principais.
- `com.exemplo.pagamentos.infrastructure`:
  - Configurações e integrações com tecnologias externas.

---

## Classes Principais

### 1. **PagamentoController**
- **Descrição**:
  - Gerencia as requisições REST relacionadas ao pagamento.
  - Envia as requisições para o Kafka.
- **Endpoints**:
  - `POST /api/v1/pagamentos`: Recebe dados do pagamento e os envia para o Kafka.

### 2. **KafkaPagamentoListener**
- **Descrição**:
  - Escuta mensagens no tópico `pagamentos` do Kafka.
  - Processa o pagamento e interage com as APIs externas (primária e secundária) usando o OpenFeign.
  - Implementa fallback com Resilience4j para redirecionar o pagamento para uma API secundária em caso de falha.

### 3. **PagamentoService**
- **Descrição**:
  - Responsável pela lógica principal de processamento do pagamento.
  - Calcula valores e atualiza o status do pagamento.

### 4. **Configuração Kafka**
- **Produtor**:
  - Configurado para publicar mensagens no tópico `pagamentos`.
- **Consumidor**:
  - Configurado para consumir mensagens do mesmo tópico e processar pagamentos.

### 5. **CassandraConfig**
- Configura a conexão com o banco Cassandra para armazenamento de logs e métricas.

---

## Fluxo de Execução
1. **Recebimento do Pagamento**:
   - O cliente envia uma requisição para o endpoint `/api/v1/pagamentos` com os dados do pagamento.
   - A `PagamentoController` publica os dados no Kafka.

2. **Processamento pelo Kafka**:
   - O `KafkaPagamentoListener` consome a mensagem e processa o pagamento.
   - O pagamento é enviado para a API externa (primária ou secundária em caso de fallback).

3. **Armazenamento de Métricas**:
   - As métricas do pagamento são armazenadas no Cassandra para análise futura.

---

## Configurações Necessárias

### 1. **Arquivo `application.properties`**
```properties
# Configurações do Kafka
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=pagamentos-group
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer

# Configurações do Cassandra
spring.cassandra.contact-points=localhost
spring.cassandra.port=9042
spring.cassandra.keyspace-name=meu_keyspace
spring.cassandra.local-datacenter=datacenter1
spring.cassandra.username=cassandra
spring.cassandra.password=cassandra

#Configuração do H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Outras configurações
server.port=8080
```

### 2. **Docker Compose para Kafka e Cassandra**

#### Arquivo `docker-compose.yml`
```yaml
version: '3.8'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000

  kafka:
    image: confluentinc/cp-kafka:latest
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1

  cassandra:
    image: cassandra:latest
    ports:
      - "9042:9042"
```

---

## Como Executar o Projeto

### 1. **Subir as Dependências**
1. Certifique-se de que o Docker esteja instalado e configurado.
2. Execute o comando para iniciar os containers:
   ```bash
   docker-compose up
   ```

### 2. **Executar a Aplicação**
1. Certifique-se de que o Java 21 está instalado.
2. Compile o projeto:
   ```bash
   ./mvnw clean install
   ```
3. Execute a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```

### 3. **Testar o Projeto**
- Enviar uma requisição de pagamento:
  ```bash
  curl -X POST http://localhost:8080/api/v1/pagamentos \
       -H "Content-Type: application/json" \
       -d '{"tipoPagamento": "PIX", "valor": 150.0, "destinatario": "Loja Exemplo"}'
  ```

---

## Considerações Finais
Este projeto demonstra uma solução robusta para processamento de pagamentos utilizando um ecossistema moderno e escalável. Ele pode ser facilmente expandido para incluir novos tipos de pagamento ou integrações com outras tecnologias.


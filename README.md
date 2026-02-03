<div align="center">

# ⚡ Spring WebFlux Reactive API

API moderna construída com **Java e Spring WebFlux**, focada em **programação reativa**, **arquitetura não bloqueante**, **event streaming** e **alto desempenho**.

</div>

---

## 🧠 Stack & Tecnologias

<div align="center">

![Java](https://img.shields.io/badge/Java-25-00FFD1?style=for-the-badge&logo=java&logoColor=black)
![Spring](https://img.shields.io/badge/Spring%20WebFlux-4.0.x-6DB33F?style=for-the-badge&logo=spring&logoColor=black)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18.x-00C2FF?style=for-the-badge&logo=postgresql&logoColor=black)
![Docker](https://img.shields.io/badge/Docker-00BFFF?style=for-the-badge&logo=docker&logoColor=black)
![Reactive](https://img.shields.io/badge/Reactive-FF00FF?style=for-the-badge)

</div>

---

## 🚀 Visão Geral

Este projeto demonstra a construção de uma **API reativa e escalável**, utilizando o ecossistema Spring com foco em:

- Processamento **assíncrono**
- Operações **não bloqueantes**
- Streaming de eventos em tempo real
- Integração com APIs externas
- Testes e análise de performance

---

## ⚙️ Funcionalidades

### 🔹 CRUD Reativo
- Operações completas de criação, leitura, atualização e exclusão
- Busca por ID
- Uso de **DTOs**
- Validação de dados

### 🔹 Programação Reativa
- Uso de `Mono` e `Flux`
- Arquitetura orientada a eventos
- Reatividade ponta a ponta

### 🔹 Event Streaming
- **Server-Sent Events (SSE)**
- Geração contínua de eventos
- Propagação de mudanças em tempo real
- Controle de ingressos e reservas

### 🔹 Consumo de APIs Externas
- Integração com API de tradução (DeepL)
- Comunicação não bloqueante com **WebClient**
- Tradução de conteúdos em tempo real

### 🔹 Testes & Performance
- Testes reativos com **WebTestClient**
- Testes assíncronos
- Benchmark entre APIs Servlet e Reativas
- Testes de carga com **Apache AB**

---

## 🗄️ Banco de Dados

- PostgreSQL
- Migrations automatizadas
- Execução via Docker
- Visualização e persistência de dados

---

## 🧪 Executando os Testes

```bash
./mvnw test

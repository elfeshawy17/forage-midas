# Midas Core

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2_Database-0000BB?style=for-the-badge&logo=h2&logoColor=white)

**A high-performance transaction processing system with real-time balance management and incentive integration**

[Features](#features) • [Architecture](#architecture) • [Getting Started](#getting-started) • [Testing](#testing)

</div>

---

## 📋 Overview

Midas Core is an enterprise-grade financial transaction processing system developed as part of the JPMorgan Chase & Co. Software Engineering Job Simulation. The system demonstrates advanced software engineering concepts including distributed messaging, concurrent transaction handling, and real-time balance calculations with incentive rewards.

## ✨ Features

### Core Capabilities

- **🔄 Real-time Transaction Processing** - Kafka-based message consumption for high-throughput transaction handling
- **💰 Balance Management** - RESTful API for querying user account balances
- **🎁 Incentive Integration** - External service integration for transaction-based rewards
- **🔒 Concurrency Control** - Pessimistic locking to prevent race conditions and ensure data consistency
- **⚡ Error Handling** - Comprehensive exception handling with custom business logic validations

### Technical Highlights

- **Deadlock Prevention** - Ordered resource locking strategy for concurrent transactions
- **Transaction Validation** - Pre-flight checks for amount validity and sufficient balance
- **Idempotent Operations** - Safe retry mechanisms with Kafka consumer error handling
- **REST API** - Clean endpoint design for balance queries

# 🏗️ Architecture

```
                    ┌─────────────────────────┐
                    │     Kafka Topic         │
                    │    (Transactions)       │
                    └───────────┬─────────────┘
                                │
                                ▼
                    ┌─────────────────────────┐
                    │  TransactionListener    │
                    │   (Kafka Consumer)      │
                    └───────────┬─────────────┘
                                │
                                ▼
                    ┌─────────────────────────┐
                    │  TransactionService     │
                    │  - Validation           │
                    │  - Pessimistic Locking  │
                    │  - Incentive Calculation│
                    │  - Balance Updates      │
                    └─────┬─────────────┬─────┘
                          │             │
                          ▼             ▼
                  ┌───────────┐   ┌────────────────┐
                  │ Database  │   │Incentive Client│
                  │(JPA/ORM)  │   │  (REST API)    │
                  └───────────┘   └────────────────┘
```

### Key Components

| Component | Responsibility |
|-----------|---------------|
| **TransactionListener** | Consumes transaction messages from Kafka topic |
| **TransactionService** | Orchestrates transaction processing with validation and persistence |
| **DatabaseConduit** | Provides database access layer with pessimistic locking support |
| **IncentiveClient** | Integrates with external incentive calculation service |
| **BalanceController** | Exposes REST endpoint for balance queries |
| **GlobalExceptionHandler** | Centralized error handling and API error responses |

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- Kafka (embedded for testing)
- H2 Database (in-memory, configured automatically)

### Installation

```bash
# Clone the repository
git clone https://github.com/elfeshawy17/forage-midas.git
cd forage-midas

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

## 🧪 Testing

The project includes five comprehensive test suites that validate different aspects of the system:

### Test Suite Overview

| Test | Validates |
|------|-----------|
| **Task 1** | Application bootstrap and configuration |
| **Task 2** | Kafka message consumption and deserialization |
| **Task 3** | Transaction processing logic and balance updates |
| **Task 4** | Concurrent transaction handling and race condition prevention |
| **Task 5** | End-to-end transaction flow with incentive integration |

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=TaskFiveTests
```

### Test Data

Test data files are located in `src/test/resources/test_data/`:
- User initialization data
- Transaction sequences for various scenarios
- Edge case validations

## 🔐 Transaction Safety

The system implements several safety mechanisms:

1. **Pessimistic Locking** - Database-level row locks prevent concurrent modifications
2. **Ordered Lock Acquisition** - Consistent ordering (by user ID) prevents deadlocks
3. **Balance Validation** - Pre-transaction checks ensure sufficient funds
4. **Transaction Atomicity** - All operations wrapped in database transactions
5. **Error Recovery** - Non-retryable exceptions for invalid operations

## 📊 API Reference

### Get Balance

```http
GET /balance?userId={userId}
```

**Response:**
```json
{
  "amount": 1234.56
}
```

## 🎓 Project Context

This project was developed as part of the **JPMorgan Chase & Co. Software Engineering Virtual Experience Program** on Forage, successfully completing all five tasks:

✅ Task 1: Application Setup and Configuration  
✅ Task 2: Kafka Integration and Message Consumption  
✅ Task 3: Transaction Processing Implementation  
✅ Task 4: Concurrency Control and Deadlock Prevention  
✅ Task 5: External Service Integration and End-to-End Testing  

## 📜 Certificate of Completion

You can view the official completion certificate for the  
**JPMorgan Chase & Co. Software Engineering Virtual Experience Program** here:

👉 **[View Certificate (PDF)](https://forage-uploads-prod.s3.amazonaws.com/completion-certificates/Sj7temL583QAYpHXD/E6McHJDKsQYh79moz_Sj7temL583QAYpHXD_6936b75341572da240e7659e_1765382145088_completion_certificate.pdf)**

<p align="center">
  <a href="https://forage-uploads-prod.s3.amazonaws.com/completion-certificates/Sj7temL583QAYpHXD/E6McHJDKsQYh79moz_Sj7temL583QAYpHXD_6936b75341572da240e7659e_1765382145088_completion_certificate.pdf">
    <img src="https://img.shields.io/badge/Certificate-View%20PDF-blue?style=for-the-badge" />
  </a>
</p>


## 🛠️ Technology Stack

- **Framework:** Spring Boot 3.x
- **Messaging:** Apache Kafka
- **Persistence:** Spring Data JPA
- **Database:** H2 (In-Memory)
- **Testing:** JUnit 5, EmbeddedKafka, TestContainers
- **Build Tool:** Maven

## 📝 License

This project was created for educational purposes as part of the JPMorgan Chase & Co. Software Engineering Job Simulation program.

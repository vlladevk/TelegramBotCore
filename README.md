# TelegramBotCore

*A modular, extensible engine for building Telegram bots with Spring Boot.*

---

**TelegramBotCore** is a lightweight yet powerful engine for developing Telegram bots. It provides essential building blocks for handling commands, events, and user interactions in a clean, maintainable, and easily extendable way.

---

## 🧱 Overview

TelegramBotCore is built around the principles of **modularity**, **scalability**, and **extensibility**. It uses annotations, component scanning, and selective reflection to automatically register commands and events.

### Out of the box, it acts as:
- ✅ a command-handling engine
- 🚫 a spam prevention system for group chats
- 🔐 a role-based access gate
- 🔄 a token limiter for message frequency control


### You can focus on domain-specific logic — the engine takes care of:
- Command dispatching  
- Role-based access control  
- Event transformation and dispatch  
- Telegram API integration


Extension points allow you to inject custom event processors, permission strategies, or command logic with minimal friction.

---
## 📦 Out-of-the-Box Behavior
Without writing any custom logic, **TelegramBotCore** already provides:

| Feature                         | Description                                                                 |
|---------------------------------|-----------------------------------------------------------------------------|
| 🛡️ Anti-Spam Filtering          | Automatically deletes group messages if a user exceeds their hourly token limit |
| 🔄 Token Refresh Scheduler      | Resets tokens hourly per user (customizable)                                |
| 🔐 Role-Based Command Access    | Limits access to commands like `/add_tokens`, `/remove_admin`, etc.         |
| 📋 Built-in Commands            | Includes `/help`, `/registration_chat`, `/add_tokens`, `/remaining_tokens` and more |
| 🤖 Auto User & Chat Registration| Detects new users/messages and stores them automatically                    |
| 🧩 Easy Extension               | Add new commands and events with minimal setup                              |

---


## 🔑 Key Highlights

- **Annotation-Driven Commands** — easily define and organize bot commands.  
- **Role-Based Access Control** — granular control over command execution.
- **Spam Prevention with Token Limits** — restricts message flow in group chats
- **Automatic Handler Registration** — no boilerplate for command and event binding.  
- **Event Mapping System** — turn Telegram updates into domain events.  
- **Telegram Adapter Layer** — abstracts raw TelegramBot API calls.  
- **Structured Logging** — detailed logs aid development and maintenance.

---

## 🧩 Features

### 🎯 Annotation-Driven Commands

- `@BotCommand` — attach metadata to methods that act as commands  
- `@CommandController` — designate classes containing command logic

### 🔐 Role-Based Access Control

- Use `UserRole` to restrict command execution  
- Permissions are enforced before command invocation

### ⚙️ Automatic Command & Event Registration

- Scans and registers all `@BotCommand` methods via Spring DI and reflection  
- Detects all `@BotEventBinding` event types at startup

### 📡 Event Handling System

- Converts raw Telegram updates to domain-level `EventDto` types  
- Publishes them through Spring's event infrastructure

### 🔌 Telegram API Adapter

- Cleanly separates Telegram logic from domain code  
- Easy to test and mock in isolation

### 🪵 Logging & Error Handling

- Rich logging using Log4j2  
- Centralized error handler with meaningful messages

---

## 🧱 Architecture

TelegramBotCore follows a layered, modular architecture designed for maintainability and extension:

### 1. 📦 Annotation Layer

Defines metadata annotations:
- `@BotCommand`
- `@CommandController`
- `@BotEventBinding`

### 2. 🧠 Application Layer

Core logic and interfaces:
- Command registration and dispatch
- Event publishing and transformation
- Role validation

### 3. 🧬 Domain Layer

Pure business entities and logic:
- `User`, `Chat`, `UserChat` entities
- Enums: `UserRole`, `ChatType`, `ChatStatus`

### 4. 🏗 Infrastructure Layer

Integrates external systems:
- TelegramBots library
- Spring Data JPA
- Mappers, repositories, schedulers, configuration

### 5. 🌐 Interfaces Layer

Entry points and orchestration:
- TelegramBot controllers and listeners
- Coordinates between adapters and services

---

## 🛠 Getting Started

### 📋 Prerequisites

- Java 21+
- Maven  
- MySQL / PostgreSQL / H2  
- Telegram Bot Token (from [@BotFather](https://t.me/BotFather))

### ⚙️ Configuration (`application.yml`)

```yaml
telegram:
  bot:
    username: YourBotUserName
    token: YOUR_TELEGRAM_BOT_TOKEN

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/yourdatabase
    username: your_db_user
    password: your_db_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

bot:
  message-limit:
    max-chars-per-hour: 1000
```
---

## 📦 Build & Run

To get started quickly, follow these steps:

### 1. Clone the repository
```bash
git clone git@github.com:vlladevk/TelegramBotCore.git
cd TelegramBotCore
```
### 2. Build the project using Maven
```bash
mvn clean install
mvn spring-boot:run
```
### 3. Run the bot in development mode
```bash
mvn spring-boot:run
```
### Or run it as a packaged JAR
```bash
java -jar target/TelegramBotCore.jar
```
---

## 🧯 Use Cases / Example Extensions

### ⚔ Ban a User (/ban_user)

```java
@CommandController
@RequiredArgsConstructor
public class ModerationController {
    private final ArgumentExtractor argumentExtractor;
    private final MessageResponseFactory messageFactory;

    @BotCommand(userRole = UserRole.CHAT_ADMIN,
                description = "Bans a user from the chat. Usage: /ban_user @username")
    public TextResponse banUser(ChatMessageReceivedDto receivedMessage) {
        List<String> args = argumentExtractor.extract(receivedMessage.text());
        if (args.isEmpty()) {
            return messageFactory.generateResponse(receivedMessage, "Please specify a username.");
        }
        // here is the logic of ban in chat
        ...
        return messageFactory.generateResponse(receivedMessage, args.getFirst() + " has been banned from the chat.");
    }
}
```

### ⚡ Custom Event Example

```java
public record MessageEditedDto(...) implements EventDto {}

@BotEventBinding(eventDto = MessageEditedDto.class)
public record MessageEditedEvent(MessageEditedDto dto) implements BotEvent {}
```

- Create a mapper for the new update type
- Listen and respond using Spring event listeners

---

## 📦 Dependencies
- Spring Boot — DI and config
- TelegramBots API — Telegram Java SDK
- Spring Data JPA — ORM and persistence
- MySQL Connector/J — JDBC driver
- Reflections — annotation scanning
- Log4j2 — structured logging
- Spring Events — decoupled architecture

---

## ✅ Final Thoughts
TelegramBotCore provides a structured foundation for building production-ready Telegram bots.  
It offers clean separation of concerns and supports scalable extension through well-defined modules.

Designed for both simple bots and complex, event-driven systems, this engine gives you the flexibility and structure needed for real-world use.



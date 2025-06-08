# 🧠 Spring AI RAG Chat

A full-stack **Retrieval-Augmented Generation (RAG)** application built with:

- ⚙️ **Spring Boot 3.5** + **Spring AI**
- 🧠 **Ollama** running local LLMs (e.g. `phi3:mini`, `llama3`)
- 📄 **PDF ingestion** and chunking with Apache PDFBox
- 🐘 **PostgreSQL** + **pgvector** for embedding storage and vector search
- 🌐 **Angular 19** frontend chat interface

---

## 💡 What It Does

- Upload PDFs (text + images)
- Parse documents into semantic chunks
- Embed those chunks using a local embedding model (`mxbai-embed-large-v1`, `nomic-embed-text`)
- Store embeddings in a pgvector-backed Postgres DB
- Ask questions using a local LLM, augmented by relevant vector search
- Enjoy a clean Angular chat UI with typing indicators and upload progress

---

## 📦 Project Structure

```

spring-ai-rag/
├── backend/             # Spring Boot + Spring AI
│   └── src/main/java/...
├── frontend/            # Angular 19 standalone app
│   └── src/app/...
├── docker-compose.yml
└── README.md

````

---

## 🚀 Features

### Backend
- ✅ Upload PDF (text + images)
- ✅ Chunk & embed content
- ✅ Store embeddings in pgvector
- ✅ Ask AI questions (context-aware)
- ✅ Models: `phi3:mini`, `llama3`, etc.

### Frontend
- ✅ Angular 19 with standalone components
- ✅ Chat-style UI
- ✅ PDF upload + progress feedback
- ✅ REST integration with Spring backend

---

## 🧰 Tech Stack

- **Spring Boot 3.5**, **Spring AI**
- **Ollama** (LLMs + embedding models)
- **PgVector** extension in Postgres
- **Apache PDFBox** for PDF parsing
- **Angular 19** with signals + standalone API
- **Docker Compose** for local dev

---

## 🛠️ Getting Started

### 🔧 Prerequisites

- Java 21+
- Node.js 18+ or 20
- Angular CLI
- Docker + Docker Compose
- Ollama CLI: [https://ollama.com/download](https://ollama.com/download)

---

### 🧠 1. Pull Required Models

```bash
docker exec -it $(docker ps -qf "name=ollama") ollama pull phi3:mini
docker exec -it $(docker ps -qf "name=ollama") ollama pull mxbai-embed-large
# Optional:
docker exec -it $(docker ps -qf "name=ollama") ollama pull llama3
````

---

### 🐳 2. Start the Stack

From the project root:

```bash
docker compose up --build
```

Starts:

* Spring Boot app → [http://localhost:8080](http://localhost:8080)
* PostgreSQL with pgvector
* Ollama

---

## 🧑‍💻 Frontend (Angular)

### 1. Navigate to frontend

```bash
cd frontend
```

### 2. Install dependencies

```bash
npm install --legacy-peer-deps
```

### 3. Run the app

```bash
npm start
# or
ng serve
```

Open [http://localhost:4200](http://localhost:4200)

> ✅ You'll see a UI for chatting with your PDFs.

---

## 🌐 Backend CORS Configuration

Ensure CORS is enabled in Spring Boot:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("*");
    }
}
```

---

## 📄 API Overview

### Upload a PDF

```http
POST /api/v1/rag/upload
Content-Type: multipart/form-data
Body: file=@mydoc.pdf
```

### Ask a Question

```http
POST /api/v1/rag/ask
Content-Type: application/json

{
  "question": "What is the summary of the uploaded document?"
}
```

---

## 🔧 Spring Config (`application.yml`)

```yaml
spring:
  ai:
    chat.options.model: phi3:mini
    embedding.ollama.model: mxbai-embed-large-v1
    vectorstore.pgvector:
      table-name: embeddings
      dimensions: 1024
      distance-type: cosine_distance
      index-type: hnsw
      initialize-schema: true
  datasource:
    url: jdbc:postgresql://localhost:5432/ragdb
    username: raguser
    password: ragpass
  servlet:
    multipart:
      max-file-size: 20MB
      max-request-size: 20MB
```

---

## 🐘 PostgreSQL Details

* **DB**: `ragdb`
* **User/Pass**: `raguser` / `ragpass`
* **Port**: `5432`
* **Table**: `embeddings` with pgvector extension

---

## 🧪 Testing & Debugging

* Backend logs show:

  ```
  Context retrieved: [...]
  ```
* Errors like:

  ```
  relation "embeddings" does not exist
  ```

  → fix with `initialize-schema: true` or manually create the table.

---

## 📈 Performance Tips

* Use `nomic-embed-text` if `mxbai-embed-large-v1` is too slow
* Add `@Transactional` to embedding code for batch inserts
* Use `hnsw` index type for faster vector similarity
* Tune JVM heap size in Docker

---

## 📋 Roadmap

* [ ] Chat history persistence
* [ ] Drag-and-drop file upload
* [ ] File name and upload status list
* [ ] Markdown rendering for answers
* [ ] Authentication and user profiles

---


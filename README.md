
# 🧠 Spring AI RAG PDF Application

This is a containerized Spring Boot 3.5 REST API that implements a **Retrieval-Augmented Generation (RAG)** workflow using [Spring AI](https://docs.spring.io/spring-ai/reference/) and [Ollama](https://ollama.com/). It supports uploading PDFs (text + images), parsing them into chunks, embedding those chunks with a local embedding model, storing them in a **PostgreSQL + pgvector** vector database, and then using those vectors to enhance prompts sent to a local LLM.

---

## 📦 Features

- ✅ Upload PDFs (images + text parsed using Apache PDFBox)
- ✅ Parse and chunk documents
- ✅ Generate embeddings using a local model (`mxbai-embed-large-v1` or `nomic-embed-text`)
- ✅ Store and retrieve embeddings using **pgvector**
- ✅ Ask questions and retrieve context from stored documents
- ✅ Ask enhanced questions to local LLMs like `llama3` or `phi3:mini`
- ✅ Exposed via REST API (Angular UI integration coming soon)

---

## 🧰 Tech Stack

- **Spring Boot 3.5** + **Spring AI**
- **Ollama** (runs local models)
- **PostgreSQL** + **pgvector**
- **Apache PDFBox** (PDF parsing)
- **Docker Compose** (easy local setup)

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/your-org/spring-ai-rag-pdf.git
cd spring-ai-rag-pdf
````

### 2. Install prerequisites

* 🐳 [Docker](https://www.docker.com/)
* 🧠 [Ollama CLI](https://ollama.com/download)

### 3. Pull required models

```bash
ollama pull phi3:mini
ollama pull mxbai-embed-large-v1
# Optional: ollama pull llama3
```

### 4. Start the full stack

```bash
docker compose up --build
```

This will start:

* `Spring Boot` app on [http://localhost:8080](http://localhost:8080)
* `PostgreSQL + pgvector`
* `Ollama` with local models

---

## 📄 API Overview

### Upload PDF

```bash
curl -X POST http://localhost:8080/api/v1/rag/upload \
  -F file=@your-file.pdf
```

### Ask a Question

```bash
curl -X POST http://localhost:8080/api/v1/rag/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "What is the summary of the uploaded PDF?"}'
```

---

## 🔧 Configuration

### `application.yml`

Located at `src/main/resources/application.yml`:

```yaml
spring:
  ai:
    chat:
      options:
        model: phi3:mini
    embedding:
      ollama:
        model: mxbai-embed-large-v1
    vectorstore:
      pgvector:
        table-name: embeddings
        dimensions: 1024
        distance-type: cosine_distance
        initialize-schema: true
  datasource:
    url: jdbc:postgresql://postgres:5432/ragdb
    username: raguser
    password: #password
  servlet:
    multipart:
      max-file-size: 20MB
      max-request-size: 20MB
```

---

## 🐘 PostgreSQL Vector DB

* Exposed on port `5432`
* DB: `ragdb`
* User/pass: `raguser` / `ragpass`
* Vector schema/table: `embeddings`

---

## 🧪 Testing & Debugging

* Logs show retrieved context: `Context retrieved: [...]`
* Vector issues like `[relation "embeddings" does not exist]` mean table wasn't initialized. Set `initialize-schema: true`.

---

## 📈 Performance Tips

* Use `nomic-embed-text` if `mxbai-embed-large-v1` is too slow
* Increase JVM memory in `Dockerfile` with `JAVA_OPTS`
* Enable `hnsw` index in Postgres for faster vector search
* Use async processing to parallelize chunk embedding

---

## 📋 Roadmap

* [ ] Angular UI to upload files and submit prompts
* [ ] Multi-document context merging
* [ ] Role-based access control
* [ ] File versioning

---

## 👨‍💻 Authors

* Derek Taylor — *Solution Architect*

---

## 📜 License

MIT — free for personal and commercial use.

---


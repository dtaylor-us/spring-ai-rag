package us.dtaylor.springairag.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class VectorStoreService {

    private final PgVectorStore vectorStore;

    public VectorStoreService(PgVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void indexText(String docId, String content) {
        List<Document> chunks = splitIntoChunks(content, docId);
        vectorStore.add(chunks);
    }

    public List<String> findRelevant(String query) {
        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(5)
//                .similarityThreshold(0.7)
                .build();
        var results = vectorStore.similaritySearch(request);
        System.out.println("🔍 Retrieved " + results.size() + " results.");
        return results.stream().map(Document::getFormattedContent).toList();
    }

    private List<Document> splitIntoChunks(String text, String docId) {
        int chunkSize = 500;
        Map<String, Object> metadata = Map.of("docId", docId);

        return java.util.stream.IntStream.range(0, (text.length() + chunkSize - 1) / chunkSize)
                .mapToObj(i -> {
                    int start = i * chunkSize;
                    int end = Math.min(text.length(), start + chunkSize);
                    return new Document(text.substring(start, end), metadata);
                })
                .toList();
    }
}



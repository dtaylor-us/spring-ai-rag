
package us.dtaylor.springairag.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VectorStoreServiceTest {

    @Test
    void indexText_SplitsTextAndAddsToVectorStore() {
        PgVectorStore pgVectorStore = mock(PgVectorStore.class);
        VectorStoreService service = new VectorStoreService(pgVectorStore);

        String docId = "doc1";
        String content = "a".repeat(1200); // Will be split into 3 chunks (500, 500, 200)

        service.indexText(docId, content);

        ArgumentCaptor<List<Document>> captor = ArgumentCaptor.forClass(List.class);
        verify(pgVectorStore, times(1)).add(captor.capture());
        List<Document> chunks = captor.getValue();

        assertEquals(3, chunks.size());
        assertEquals(500, chunks.get(0).getText().length());
        assertEquals(500, chunks.get(1).getText().length());
        assertEquals(200, chunks.get(2).getText().length());
        assertEquals(Map.of("docId", docId), chunks.get(0).getMetadata());
    }

    @Test
    void findRelevant_ReturnsFormattedContentFromResults() {
        PgVectorStore pgVectorStore = mock(PgVectorStore.class);
        VectorStoreService service = new VectorStoreService(pgVectorStore);

        String query = "test";
        Document doc1 = new Document("content1", Map.of());
        Document doc2 = new Document("content2", Map.of());

        when(pgVectorStore.similaritySearch(any(SearchRequest.class)))
                .thenReturn(List.of(doc1, doc2));

        List<String> results = service.findRelevant(query);

        assertEquals(
                List.of("content1", "content2"),
                results.stream().map(String::trim).toList()
        );
        ArgumentCaptor<SearchRequest> captor = ArgumentCaptor.forClass(SearchRequest.class);
        verify(pgVectorStore).similaritySearch(captor.capture());
        assertEquals(query, captor.getValue().getQuery());
        assertEquals(5, captor.getValue().getTopK());
    }
}

package us.dtaylor.springairag.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.stereotype.Service;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
                .topK(10)
//                .similarityThreshold(0.7)
                .build();
        var results = vectorStore.similaritySearch(request);
        return results.stream().map(Document::getFormattedContent).toList();
    }

    public List<Document> splitIntoChunks(String text, String docId) {
        int chunkSize = 500;
        int chunkOverlap = 50;

        List<Document> chunks = new ArrayList<>();
        Map<String, Object> metadata = Map.of("docId", docId);

        List<String> sentences = splitIntoSentences(text);

        StringBuilder chunkBuilder = new StringBuilder();
        for (int i = 0; i < sentences.size(); i++) {
            String sentence = sentences.get(i);

            if (chunkBuilder.length() + sentence.length() > chunkSize) {
                // finalize chunk
                chunks.add(new Document(chunkBuilder.toString().trim(), metadata));

                // start new chunk with overlap
                chunkBuilder = new StringBuilder();
                int j = i;
                int overlapLength = 0;
                while (j >= 0 && overlapLength < chunkOverlap) {
                    String prevSentence = sentences.get(j);
                    overlapLength += prevSentence.length();
                    chunkBuilder.insert(0, prevSentence);
                    j--;
                }
            }

            chunkBuilder.append(sentence);
        }

        // add the last chunk
        if (!chunkBuilder.isEmpty()) {
            chunks.add(new Document(chunkBuilder.toString().trim(), metadata));
        }

        return chunks;
    }

    private List<String> splitIntoSentences(String text) {
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(text);
        List<String> sentences = new ArrayList<>();
        int start = iterator.first();
        int end = iterator.next();

        while (end != BreakIterator.DONE) {
            sentences.add(text.substring(start, end));
            start = end;
            end = iterator.next();
        }

        return sentences;
    }
}



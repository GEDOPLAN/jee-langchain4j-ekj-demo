package de.gedoplan.showcase.langchain4jdemo.jakartaee.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

@ApplicationScoped
public class RagInit {

  private Log log = LogFactory.getLog(RagInit.class);

  @Inject
  @ConfigProperty(name = "de.gedoplan.ai.rag.documentPath")
  String documentPath;

  @Inject
  EmbeddingStore<TextSegment> embeddingStore;

  @Inject
  EmbeddingModel embeddingModel;

  public void ingest() {
    if (!embeddingStore.search(
        new EmbeddingSearchRequest(embeddingModel.embed("any").content(), 100, 0.0, null)
    ).matches().isEmpty()) {
      log.info("Embedding store is not empty. Not ingesting new documents.");
      return;
    }

    List<Document> documents = FileSystemDocumentLoader.loadDocumentsRecursively(documentPath);

    EmbeddingStoreIngestor.builder()
        .embeddingModel(embeddingModel)
        .embeddingStore(embeddingStore)
//        .documentSplitter(new CourseTitleDocumentSplitter())
        .build()
        .ingest(documents);

    log.info("Ingested " + documents.size() + " documents");
  }

}

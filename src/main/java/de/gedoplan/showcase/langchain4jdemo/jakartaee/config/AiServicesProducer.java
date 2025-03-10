package de.gedoplan.showcase.langchain4jdemo.jakartaee.config;

import de.gedoplan.showcase.langchain4jdemo.jakartaee.service.RagAiService;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.service.MemoryAiService;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.service.SimpleAiService;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.service.ToolsAiService;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.tools.AiTools;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperties;

import java.time.Duration;
import java.util.List;

@ApplicationScoped
public class AiServicesProducer {

  @Produces
  public ChatLanguageModel getOllamaChatLanguageModel(
      @ConfigProperties OllamaProperties ollamaProperties
  ) {
    return OllamaChatModel.builder()
        .baseUrl(ollamaProperties.baseUrl)
        .modelName(ollamaProperties.modelName)
        .logRequests(ollamaProperties.logRequests)
        .logResponses(ollamaProperties.logResponses)
        .timeout(Duration.ofSeconds(ollamaProperties.timeout))
        .listeners(List.of(new LogSpeedChatModelListener()))
        .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
//        .temperature(0.1)
        .build();
  }

  @Produces
  public StreamingChatLanguageModel getOllamaStreamingChatLanguageModel(
      @ConfigProperties OllamaProperties ollamaProperties
  ) {
    return OllamaStreamingChatModel.builder()
        .baseUrl(ollamaProperties.baseUrl)
        .modelName(ollamaProperties.modelName)
        .logRequests(ollamaProperties.logRequests)
        .logResponses(ollamaProperties.logResponses)
        .timeout(Duration.ofSeconds(ollamaProperties.timeout))
        .listeners(List.of(new LogSpeedChatModelListener()))
        .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
        .build();
  }

  @Produces
  @ApplicationScoped
  public SimpleAiService getSimpleAiService(StreamingChatLanguageModel streamingChatModel, ChatLanguageModel chatLanguageModel) {
    return AiServices.builder(SimpleAiService.class)
        .streamingChatLanguageModel(streamingChatModel)
        .chatLanguageModel(chatLanguageModel)
        .build();
  }

  @Produces
  @ApplicationScoped
  public MemoryAiService getMemoryAiService(StreamingChatLanguageModel streamingChatModel, ChatLanguageModel chatLanguageModel) {
    return AiServices.builder(MemoryAiService.class)
        .streamingChatLanguageModel(streamingChatModel)
        .chatLanguageModel(chatLanguageModel)
        .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
        .build();
  }

  @Produces
  @ApplicationScoped
  public RagAiService getRagAiService(RetrievalAugmentor retrievalAugmentor, StreamingChatLanguageModel streamingChatModel, ChatLanguageModel chatLanguageModel) {
    return AiServices.builder(RagAiService.class)
        .streamingChatLanguageModel(streamingChatModel)
        .chatLanguageModel(chatLanguageModel)
        .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
        .retrievalAugmentor(retrievalAugmentor)
        .build();
  }

  @Produces
  @ApplicationScoped
  public EmbeddingStore<TextSegment> getEmbeddingStore() {
    return new InMemoryEmbeddingStore<>();
  }

  @Produces
  @ApplicationScoped
  public EmbeddingModel getEmbeddingModel(@ConfigProperties OllamaProperties ollamaProperties) {
    return OllamaEmbeddingModel.builder()
        .baseUrl(ollamaProperties.baseUrl)
        .modelName(ollamaProperties.embeddingModelName)
        .build();
  }

  @Produces
  @ApplicationScoped
  public RetrievalAugmentor getRetrievalAugmentor(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
    String promptTemplate = """
    Bitte beantworte die folgende Anfrage:

    ---
    {{userMessage}}
    ---

    Basiere deine Antwort nur auf die folgenden Informationen

    {{contents}}\
    """;

//    String promptTemplate = """
//    Bitte beantworte die folgende Anfrage:
//
//    ---
//    {{userMessage}}
//    ---
//
//    Basiere deine Antwort nur auf die folgenden Informationen
//    Jeder Informationsabschnitt ist mit '---' abgegrenzt.
//    Die erste Zeile in jedem Abschnitt ist der Kurstitel zu dem die Information gehört.
//    Stelle sicher keine Informationen von unterschiedlichen Kursen durcheinanderzubringen.
//
//    {{contents}}\
//    """;

    EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
        .embeddingModel(embeddingModel)
        .embeddingStore(embeddingStore)
        .minScore(0.8)
        .maxResults(3)
        .build();

    ContentInjector contentInjector = DefaultContentInjector.builder()
        .promptTemplate(new PromptTemplate(promptTemplate))
        .build();

    return DefaultRetrievalAugmentor.builder()
        .contentRetriever(contentRetriever)
        .contentInjector(contentInjector)
        .build();
  }

  @Produces
  @ApplicationScoped
  public ToolsAiService getToolsAiService(StreamingChatLanguageModel streamingChatModel, ChatLanguageModel chatLanguageModel) {
    return AiServices.builder(ToolsAiService.class)
        .streamingChatLanguageModel(streamingChatModel)
        .chatLanguageModel(chatLanguageModel)
        .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
        .tools(new AiTools())
        .build();
  }

}

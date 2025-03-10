package de.gedoplan.showcase.langchain4jdemo.jakartaee.api;

import de.gedoplan.showcase.langchain4jdemo.jakartaee.rag.RagInit;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.service.RagAiService;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.service.MemoryAiService;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.service.SimpleAiService;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.service.ToolsAiService;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import java.util.UUID;
import java.util.stream.Collectors;

@Path("/ai")
public class AiEndpoint {

    @Inject
    SimpleAiService simpleAiService;

    @Inject
    MemoryAiService memoryAiService;

    @Inject
    ToolsAiService toolsAiService;

    @Inject
    RagAiService ragAiService;

    @Inject
    EmbeddingStore<TextSegment> embeddingStore;

    @Inject
    EmbeddingModel embeddingModel;

    @Inject
    RagInit ragInit;

    @POST
    @Path("/simple")
    @Produces("text/plain")
    public String simpleAi(String userMessage) {
        return simpleAiService.chat(userMessage);
    }

    @POST
    @Path("/simpleCowboy")
    @Produces("text/plain")
    public String simpleAiCowboy(String userMessage) {
        return simpleAiService.chatCowboy(userMessage);
    }

    @POST
    @Path("/simpleAnfrage")
    @Produces("text/plain")
    public String simpleAiAnfrage(String userMessage) {
        return simpleAiService.chatAnfrage(userMessage);
    }

    @POST
    @Path("/simpleMethod")
    @Produces("text/plain")
    @Consumes("application/json")
    public String simpleAiMethod(MethodInProgLang methodInProgLang) {
        return simpleAiService.chatMethodInProgrammingLang(methodInProgLang.method(), methodInProgLang.progLang());
    }

    private record MethodInProgLang(String method, String progLang) {}

    @POST
    @Path("/simplePersonInformation")
    @Produces("text/plain")
    public SimpleAiService.PersonInformation simpleAiPersonInformation(String userMessage) {
        return simpleAiService.extractPersonInformation(userMessage);
    }

    @POST
    @Path("/memory/{id}")
    @Produces("text/plain")
    public String memoryAi(@PathParam("id") int id, String userMessage) {
        return memoryAiService.chatIntId(id, userMessage);
    }

    @POST
    @Path("/rag/init")
    public void ragInit() {
        ragInit.ingest();
    }

    @POST
    @Path("/rag")
    @Produces("text/plain")
    public String ragAi(String userMessage) {
        return ragAiService.chat(UUID.randomUUID(), userMessage);
    }

    @POST
    @Path("/rag/embeddingScore")
    @Produces("text/plain")
    public String ragEmbeddingScore(String userMessage) {
        EmbeddingSearchResult<TextSegment> embeddingSearchResults = embeddingStore.search(new EmbeddingSearchRequest(embeddingModel.embed(userMessage).content(), 100, 0.0, null));
        return embeddingSearchResults.matches().stream().map(match -> match.score() + ": " + match.embedded().text()).collect(Collectors.joining("\n---\n"));
    }

    @POST
    @Path("/tools")
    @Produces("text/plain")
    public String toolsAi(String userMessage) {
        return toolsAiService.chat(UUID.randomUUID(), userMessage);
    }
}
package de.gedoplan.showcase.langchain4jdemo.jakartaee.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import java.util.UUID;

public interface ToolsAiService {

  String SYSTEM_MESSAGE = "Du bist ein Assistenzsystem, das Fragen zum Wetter und zur Verkehrssituation beantwortet.";

  @SystemMessage(SYSTEM_MESSAGE)
  String chatNoMemory(@UserMessage String userMessage);

  @SystemMessage(SYSTEM_MESSAGE)
  String chat(@MemoryId UUID memoryId, @UserMessage String userMessage);

  // TODO: streaming response is not streamed, but sent in total (https://github.com/ollama/ollama/issues/7886)
  @SystemMessage(SYSTEM_MESSAGE)
  TokenStream chatStreaming(@MemoryId UUID memoryId, @UserMessage String userMessage);
}

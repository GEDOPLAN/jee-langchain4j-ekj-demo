package de.gedoplan.showcase.langchain4jdemo.jakartaee.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

import java.util.UUID;

public interface RagAiService {

  String SYSTEM_MESSAGE = "Du bist ein Assistenzsystem, das Fragen zum Wetter und zur Verkehrssituation beantwortet.";
//  String SYSTEM_MESSAGE = "Du bist ein Assistenzsystem, das Kunden dabei unterstützt die richtige Schulung für ihre Anforderungen zu finden";

  @SystemMessage(SYSTEM_MESSAGE)
  String chatNoMemory(@UserMessage String userMessage);

  @SystemMessage(SYSTEM_MESSAGE)
  String chat(@MemoryId UUID memoryId, @UserMessage String userMessage);

  @SystemMessage(SYSTEM_MESSAGE)
  TokenStream chatStreaming(@MemoryId UUID memoryId, @UserMessage String userMessage);
}

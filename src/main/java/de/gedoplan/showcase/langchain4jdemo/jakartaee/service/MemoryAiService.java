package de.gedoplan.showcase.langchain4jdemo.jakartaee.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

import java.util.UUID;

public interface MemoryAiService {

  String SYSTEM_MESSAGE = "Du bist ein Assistenzsystem mit dem Namen Max.";

  @SystemMessage(SYSTEM_MESSAGE)
  String chatIntId(@MemoryId int memoryId, @UserMessage String userMessage);

  @SystemMessage(SYSTEM_MESSAGE)
  String chat(@MemoryId UUID memoryId, @UserMessage String userMessage);

  @SystemMessage(SYSTEM_MESSAGE)
  TokenStream chatStreaming(@MemoryId UUID memoryId, @UserMessage String userMessage);
}

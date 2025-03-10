package de.gedoplan.showcase.langchain4jdemo.jakartaee.api;

import de.gedoplan.showcase.langchain4jdemo.jakartaee.service.MemoryAiService;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.service.RagAiService;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.service.SimpleAiService;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.service.ToolsAiService;
import dev.langchain4j.service.TokenStream;
import jakarta.inject.Inject;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.UUID;

@ServerEndpoint("/ws/chat")
public class ChatWebSocket {

  private Log log = LogFactory.getLog(ChatWebSocket.class);

  private UUID memoryId = UUID.randomUUID();

  @Inject
  SimpleAiService aiService;
//  MemoryAiService aiService;
//  RagAiService aiService;
//  ToolsAiService aiService;

  @OnOpen
  public void onOpen(Session session) throws IOException {
    log.info("WebSocket connection opened");
  }

  @OnMessage
  public void onMessage(Session session, String message) throws IOException {
    log.info("Message received: " + message);
    String trimmedMessage = message.strip();
    if (trimmedMessage.isEmpty()) return;

    TokenStream aiAnswer = aiService.chatStreaming(trimmedMessage);
//    TokenStream aiAnswer = aiService.chatStreaming(memoryId, trimmedMessage);
    sendWebSocketMessage("---message-start---", session);
    aiAnswer.onPartialResponse(
        token -> sendWebSocketMessage(token, session)
    ).onCompleteResponse(
        ignored -> sendWebSocketMessage("---message-end---", session)
    ).onError(log::error).start();
  }

  private void sendWebSocketMessage(String text, Session session) {
    try {
      session.getBasicRemote().sendText(text);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}

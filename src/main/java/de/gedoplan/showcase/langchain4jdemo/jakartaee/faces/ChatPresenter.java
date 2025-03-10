package de.gedoplan.showcase.langchain4jdemo.jakartaee.faces;

import de.gedoplan.showcase.langchain4jdemo.jakartaee.service.RagAiService;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.service.MemoryAiService;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.service.SimpleAiService;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.service.ToolsAiService;
import dev.langchain4j.service.TokenStream;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.UUID;

@ViewScoped
@Named
public class ChatPresenter implements Serializable {

  private Log log = LogFactory.getLog(ChatPresenter.class);

  private UUID memoryId = UUID.randomUUID();

  @Inject
  @Push
  PushContext aiChannel;

  @Inject
  SimpleAiService simpleAiService;

  @Inject
  MemoryAiService memoryAiService;

  @Inject
  ToolsAiService toolsAiService;

  @Inject
  RagAiService ragAiService;

  private AiServiceSelection aiServiceSelection = AiServiceSelection.SIMPLE_AI_SERVICE;
  private String aiMessage = "";

  public void onMessageListener() {
    String trimmedMessage = aiMessage.strip();
    if (trimmedMessage.isEmpty()) return;
    TokenStream aiAnswer = switch (aiServiceSelection) {
      case SIMPLE_AI_SERVICE -> simpleAiService.chatStreaming(trimmedMessage);
      case MEMORY_AI_SERVICE -> memoryAiService.chatStreaming(memoryId, trimmedMessage);
      case TOOLS_AI_SERVICE -> toolsAiService.chatStreaming(memoryId, trimmedMessage);
      case RAG_AI_SERVICE -> ragAiService.chatStreaming(memoryId, trimmedMessage);
    };

    aiChannel.send("---message-start---");
    aiAnswer.onPartialResponse(
        token -> aiChannel.send(token)
    ).onCompleteResponse(
        message -> aiChannel.send("---message-end---")
    ).onError(log::error).start();
  }

  public AiServiceSelection[] getAiServiceSelectionValues() {
    return AiServiceSelection.values();
  }

  public AiServiceSelection getAiServiceSelection() {
    return aiServiceSelection;
  }

  public void setAiServiceSelection(AiServiceSelection aiServiceSelection) {
    this.aiServiceSelection = aiServiceSelection;
  }

  public String getAiMessage() {
    return aiMessage;
  }

  public void setAiMessage(String aiMessage) {
    this.aiMessage = aiMessage;
  }

}

package de.gedoplan.showcase.langchain4jdemo.jakartaee.config;

import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class LogSpeedChatModelListener implements ChatModelListener {
  private Log log = LogFactory.getLog(LogSpeedChatModelListener.class);

  private LocalDateTime startTime;

  @Override
  public void onRequest(ChatModelRequestContext context) {
    startTime = LocalDateTime.now();
  }

  @Override
  public void onResponse(ChatModelResponseContext responseContext) {
    double runTimeSeconds = startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS) / 1000.0;
    log.info("tokens in answer: " + responseContext.chatResponse().tokenUsage().outputTokenCount());
    log.info("seconds: " + runTimeSeconds);
    log.info("token/s: " + responseContext.chatResponse().tokenUsage().outputTokenCount() / runTimeSeconds);
  }
}
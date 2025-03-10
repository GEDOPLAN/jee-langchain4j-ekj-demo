package de.gedoplan.showcase.langchain4jdemo.jakartaee.config;

import jakarta.enterprise.context.Dependent;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Dependent
@ConfigProperties(prefix = "de.gedoplan.ai.ollama")
public class OllamaProperties {
  public String baseUrl;
  public String modelName;
  public String embeddingModelName;
  @ConfigProperty(name = "log-requests")
  public boolean logRequests;
  @ConfigProperty(name = "log-responses")
  public boolean logResponses;
  public int timeout;
}

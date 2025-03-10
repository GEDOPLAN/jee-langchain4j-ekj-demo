package de.gedoplan.showcase.langchain4jdemo.jakartaee.faces;

public enum AiServiceSelection {
  SIMPLE_AI_SERVICE("Simple AI Service"),
  MEMORY_AI_SERVICE("Memory AI Service"),
  TOOLS_AI_SERVICE("Tools AI Service"),
  RAG_AI_SERVICE("Rag AI Service"),;

  private String uiValue;

  AiServiceSelection(String uiValue) {
    this.uiValue = uiValue;
  }

  public String getUiValue() {
    return uiValue;
  }
}

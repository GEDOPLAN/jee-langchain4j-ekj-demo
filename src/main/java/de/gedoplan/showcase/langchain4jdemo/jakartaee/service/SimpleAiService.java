package de.gedoplan.showcase.langchain4jdemo.jakartaee.service;

import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface SimpleAiService {

  String chat(String userMessage);

  TokenStream chatStreaming(String userMessage);

  @SystemMessage("Starte deine Antwort mit 'Howdy Partner'.")
  String chatCowboy(String userMessage);

  @UserMessage("{{it}} Beginne deine Antwort mit 'Vielen Dank für Ihre Anfrage.'.")
  String chatAnfrage(String userMessage);

  @UserMessage("Beschreibe die Methode {{method}} in {{programmingLanguage}} kurz in 2 Sätzen.")
  String chatMethodInProgrammingLang(@V("method") String method, @V("programmingLanguage") String programmingLanguage);

  @SystemMessage("""
    Die Anfrage enthält Informationen über eine Person.
    Bitte extrahiere die Informationen aus dem Text und gebe sie strukturiert zurück.
    Stelle dabei sicher, dass du keine Informationen vergisst, aber auch keine neuen Informationen hinzufügst die nicht vorhanden sind.
    Wenn eine Informationen nicht im Text vorkommt gebe keinen Wert zurück.""")
//  @UserMessage("""
//    Dieser Text enthält Informationen über eine Person.
//    Bitte extrahiere die Informationen aus dem Text und gebe sie strukturiert zurück.
//    Stelle dabei sicher, dass du keine Informationen vergisst, aber auch keine neuen Informationen hinzufügst die nicht vorhanden sind.
//    Wenn eine Informationen nicht im Text vorkommt gebe keinen Wert zurück.
//    ---
//    {{it}}
//    ---
//    """)
  PersonInformation extractPersonInformation(String input);

  @Description("Persönliche Informationen")
  record PersonInformation(
      @Description("Vorname der Person") String vorname,
      @Description("Nachname der Person") String nachname,
      @Description("Ob die Person verheiratet ist oder nicht") Boolean verheiratet,
      @Description("Wohnort der Person") String wohnort
  ) {
  }
}

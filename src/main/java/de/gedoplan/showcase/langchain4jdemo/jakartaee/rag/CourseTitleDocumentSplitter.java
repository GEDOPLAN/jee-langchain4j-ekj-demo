package de.gedoplan.showcase.langchain4jdemo.jakartaee.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class CourseTitleDocumentSplitter implements DocumentSplitter {

  private final Log log = LogFactory.getLog(CourseTitleDocumentSplitter.class);

  @Override
  public List<TextSegment> split(Document document) {
    String[] parts = document.text().split("\n", 2);
    if (parts.length != 2) {
      return List.of(TextSegment.textSegment(document.text(), document.metadata()));
    }

    String title = parts[0];
    String documentText = parts[1];
    DocumentSplitter paragraphSplitter = DocumentSplitters.recursive(1000, 200);
    List<TextSegment> splitSegments = paragraphSplitter.split(Document.document(documentText, document.metadata()));
    splitSegments = splitSegments.stream().map(textSegment ->
        TextSegment.textSegment(
            "---\n" + title + "\n" + textSegment.text(),
            textSegment.metadata())).toList();
    splitSegments.forEach(log::debug);
    return splitSegments;
  }
}

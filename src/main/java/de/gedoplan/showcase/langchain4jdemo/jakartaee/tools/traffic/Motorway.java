package de.gedoplan.showcase.langchain4jdemo.jakartaee.tools.traffic;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Motorway {
  A1, A2;

  @JsonCreator
  public static Motorway safeValueOf(String string) {
    try {
      return Motorway.valueOf(string);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}

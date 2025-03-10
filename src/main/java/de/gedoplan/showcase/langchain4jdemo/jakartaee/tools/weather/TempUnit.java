package de.gedoplan.showcase.langchain4jdemo.jakartaee.tools.weather;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TempUnit {
  CELSIUS, FAHRENHEIT;

  @JsonCreator
  public static TempUnit safeValueOf(String string) {
    try {
      return TempUnit.valueOf(string);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
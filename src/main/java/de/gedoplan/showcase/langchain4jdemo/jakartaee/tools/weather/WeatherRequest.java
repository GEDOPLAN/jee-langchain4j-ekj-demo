package de.gedoplan.showcase.langchain4jdemo.jakartaee.tools.weather;

public record WeatherRequest(String location, TempUnit unit) {
}

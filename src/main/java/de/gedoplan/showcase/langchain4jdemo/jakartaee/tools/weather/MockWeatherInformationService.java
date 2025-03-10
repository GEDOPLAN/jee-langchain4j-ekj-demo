package de.gedoplan.showcase.langchain4jdemo.jakartaee.tools.weather;

import java.util.Map;

public class MockWeatherInformationService {

  private final Map<String, WeatherResponse> temperatureMap = Map.ofEntries(
      Map.entry("Bielefeld", new WeatherResponse.Success("Bielefeld", 20.1, TempUnit.CELSIUS)),
      Map.entry("Berlin", new WeatherResponse.Success("Berlin", 25.6, TempUnit.CELSIUS)),
      Map.entry("Köln", new WeatherResponse.Success("Köln", 27.2, TempUnit.CELSIUS))
  );

  public WeatherResponse apply(WeatherRequest request) {
    if (request == null) return new WeatherResponse.Failure("Error: Could not parse request.");

    WeatherResponse weatherResponse = temperatureMap.getOrDefault(request.location(), null);
    if (weatherResponse == null) {
      return new WeatherResponse.Failure("Error: No weather information for location " + request.location() + " available.");
    } else {
      return ((WeatherResponse.Success) weatherResponse).withTempUnit(request.unit());
    }
  }
}

package de.gedoplan.showcase.langchain4jdemo.jakartaee.tools;

import de.gedoplan.showcase.langchain4jdemo.jakartaee.tools.traffic.*;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.tools.weather.MockWeatherInformationService;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.tools.weather.TempUnit;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.tools.weather.WeatherRequest;
import de.gedoplan.showcase.langchain4jdemo.jakartaee.tools.weather.WeatherResponse;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.util.List;

public class AiTools {
  @Tool("Get current weather information for location.")
  public WeatherResponse getCurrentWeather(@P("Request location") String location, @P(value = "Request unit", required = false) TempUnit unit) {
    return new MockWeatherInformationService().apply(new WeatherRequest(location, unit));
  }

  @Tool("Get traffic information for motorway.")
  public List<TrafficResponse> getTrafficInformation(@P("Request motorway") Motorway motorway,
                                                     @P(value = "Request length unit", required = false) LengthUnit unit) {
    return new MockTrafficInformationService().apply(new TrafficRequest(motorway, unit));
  }
}

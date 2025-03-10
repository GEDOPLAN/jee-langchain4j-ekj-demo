package de.gedoplan.showcase.langchain4jdemo.jakartaee.tools.weather;

public sealed interface WeatherResponse {
  record Success(String location, double temp, TempUnit unit) implements WeatherResponse {
    public Success withTempUnit(TempUnit unit) {
      return switch (unit) {
        case null -> this;
        case TempUnit u when u == this.unit -> this;
        case CELSIUS -> new Success(this.location, (this.temp - 32) * 5.0 / 9.0, unit);
        case FAHRENHEIT -> new Success(this.location, this.temp * 9.0 / 5.0 + 32, unit);
      };
    }
  }

  record Failure(String failureMessage) implements WeatherResponse {
  }
}

package de.gedoplan.showcase.langchain4jdemo.jakartaee.tools.traffic;

public record TrafficResponse(String from, String to, double trafficJamLength,
                              LengthUnit unit) {
  public TrafficResponse withLengthUnit(LengthUnit unit) {
    return switch (unit) {
      case null -> this;
      case LengthUnit u when this.unit == u -> this;
      case LengthUnit.MILES -> new TrafficResponse(this.from, this.to, this.trafficJamLength * 0.62, unit);
      case LengthUnit.KILOMETRES -> new TrafficResponse(this.from, this.to, this.trafficJamLength * 1.6, unit);
    };
  }
}

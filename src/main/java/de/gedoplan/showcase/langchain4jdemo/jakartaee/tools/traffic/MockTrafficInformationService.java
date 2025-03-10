package de.gedoplan.showcase.langchain4jdemo.jakartaee.tools.traffic;

import java.util.List;
import java.util.Map;

public class MockTrafficInformationService {

  private final Map<Motorway, List<TrafficResponse>> trafficJamMap = Map.ofEntries(
      Map.entry(Motorway.A1, List.of(
          new TrafficResponse("Dortmund", "Euskirchen", 2.0, LengthUnit.KILOMETRES),
          new TrafficResponse("Köln", "Dortmund", 4.0, LengthUnit.KILOMETRES)
      )),
      Map.entry(Motorway.A2, List.of(
          new TrafficResponse("Oberhausen", "Dortmund", 8.5, LengthUnit.KILOMETRES),
          new TrafficResponse("Bielefeld", "Hannover", 3.0, LengthUnit.KILOMETRES)
      ))
  );

  public List<TrafficResponse> apply(TrafficRequest request) {
    if (request.motorway() == null) return List.of();
    return trafficJamMap.getOrDefault(request.motorway(), List.of())
        .stream().map(response -> response.withLengthUnit(response.unit())).toList();
  }

}

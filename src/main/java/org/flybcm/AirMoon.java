package org.flybcm;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class AirMoon implements CarrierConnector {

  // TODO: this should be in a configuration file
  private static final String URL = "https://my.api.mockaroo.com/air-moon/flights";
  private static final String API_KEY = "dd764f40";

  private final Gson gson = new GsonBuilder().create();

  @Override
  public List<Flight> getFlights() {
    String jsonResponse;
    try {
      jsonResponse = HttpRequestHelper.makeStringHttpRequest(URL, API_KEY);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

    Type type = new TypeToken<List<AirMoonFlight>>(){}.getType();
    List<AirMoonFlight> payload = gson.fromJson(jsonResponse, type);

    return payload.stream()
      .map(airMoonFlight -> airMoonFlight.toFlight())
      .collect(Collectors.toList());
  }

  private static final BigDecimal HUNDRED = new BigDecimal(100);
  private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("h:m a");

  private static class AirMoonFlight {
    String id;
    // Using BigDecimal to avoid errors with floats which do not exist.
    BigDecimal price;
    String departure_time;
    String arrival_time;

    Flight toFlight() {
      return new Flight(
        Carrier.AIR_MOON,
        id,
        price.multiply(HUNDRED).intValue(),
        LocalTime.parse(departure_time, dateFormat),
        LocalTime.parse(arrival_time, dateFormat)
      );
    }
  }

}

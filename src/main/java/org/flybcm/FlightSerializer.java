package org.flybcm;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class FlightSerializer implements JsonSerializer<Flight> {

  @Override
  public JsonElement serialize(Flight flight, Type type, JsonSerializationContext context) {
    JsonObject element = new JsonObject();
    element.addProperty("provider", flight.getCarrier().toString());
    element.addProperty("price", flight.getPriceCents() / 100.0);
    element.addProperty("departure_time", flight.getDepartureTime().toString());
    element.addProperty("arrival_time", flight.getArrivalTime().toString());
    return element;
  }
}

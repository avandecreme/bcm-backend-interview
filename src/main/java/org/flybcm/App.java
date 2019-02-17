package org.flybcm;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import spark.Spark;

public class App {

  public static void main(String[] args) {
    new App().run();
  }

  void run() {
    Gson gson = new GsonBuilder()
      .registerTypeAdapter(Flight.class, new FlightSerializer())
      .create();

    Spark.port(4567);
    Spark.get("/api/flights", (req, res) -> {
      res.type("application/json");
      return gson.toJson(getFlights());
    });
  }

  private static final CarrierConnector[] CARRIER_CONNECTORS = {
    new AirJazz(),
    new AirMoon(),
    new AirBeam()
  };

  CarrierConnector[] getCarrierConnectors() {
    return CARRIER_CONNECTORS;
  }

  private static final int MAX_FLIGHTS = 50;

  List<Flight> getFlights() {
    return Arrays.stream(getCarrierConnectors())
      .parallel()
      .flatMap(carrierConnector -> getCarrierFlights(carrierConnector).stream())
      .sorted((flight1, flight2) -> flight1.getPriceCents() - flight2.getPriceCents())
      .limit(MAX_FLIGHTS)
      .collect(Collectors.toList());
  }

  // TODO: this method could handle the caching of the results
  List<Flight> getCarrierFlights(CarrierConnector carrierConnector) {
    try {
      return carrierConnector.getFlights();
    } catch(Exception ex) {
      // TODO: logging
      return Arrays.asList();
    } finally {
      // TODO: log execution duration
    }
  }
}

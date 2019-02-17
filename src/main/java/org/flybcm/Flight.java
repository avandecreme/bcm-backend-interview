package org.flybcm;

import java.time.LocalTime;
import java.util.Objects;

public class Flight {

  private final Carrier carrier;
  private final String id;
  private final int priceCents;
  private final LocalTime departureTime;
  private final LocalTime arrivalTime;

  public Flight(Carrier carrier, String id, int priceCents,
    LocalTime departureTime, LocalTime arrivalTime) {
    this.carrier = carrier;
    this.id = id;
    this.priceCents = priceCents;
    this.departureTime = departureTime;
    this.arrivalTime = arrivalTime;
  }

  public Carrier getCarrier() {
    return carrier;
  }

  public String getId() {
    return id;
  }

  public int getPriceCents() {
    return priceCents;
  }

  public LocalTime getDepartureTime() {
    return departureTime;
  }

  public LocalTime getArrivalTime() {
    return arrivalTime;
  }

  @Override
  public String toString() {
    return String.join(" ",
      carrier.toString(),
      id,
      Integer.toString(priceCents),
      departureTime.toString(),
      arrivalTime.toString());
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null) {
      return false;
    }
    if (getClass() != other.getClass()) {
      return false;
    }
    Flight flight = (Flight) other;
    return Objects.equals(carrier, flight.carrier) &&
      Objects.equals(id, flight.id) &&
      Objects.equals(priceCents, flight.priceCents) &&
      Objects.equals(departureTime, flight.departureTime) &&
      Objects.equals(arrivalTime, flight.arrivalTime);
  }
}

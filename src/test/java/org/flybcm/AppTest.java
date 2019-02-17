package org.flybcm;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

public class AppTest
{
  Flight airJazzFlight1 = new Flight(
    Carrier.AIR_JAZZ,
    "air-jazz-1",
    13000,
    LocalTime.of(12, 12),
    LocalTime.of(13, 12)
  );
  Flight airJazzFlight2 = new Flight(
    Carrier.AIR_JAZZ,
    "air-jazz-2",
    12000,
    LocalTime.of(15, 12),
    LocalTime.of(13, 12)
  );
  Flight airMoonFlight1 = new Flight(
    Carrier.AIR_MOON,
    "air-moon-1",
    11000,
    LocalTime.of(12, 12),
    LocalTime.of(13, 12)
  );
  Flight airBeamFlight1 = new Flight(
    Carrier.AIR_BEAM,
    "air-beam-1",
    15000,
    LocalTime.of(12, 12),
    LocalTime.of(13, 12)
  );

  @Test
  public void testGetFlightsIsSorted() throws Exception
  {
    AirJazz airJazz = mock(AirJazz.class);
    AirMoon airMoon = mock(AirMoon.class);
    AirBeam airBeam = mock(AirBeam.class);

    when(airJazz.getFlights()).thenReturn(
      Arrays.asList(airJazzFlight1, airJazzFlight2)
    );
    when(airMoon.getFlights()).thenReturn(Arrays.asList(airMoonFlight1));
    when(airBeam.getFlights()).thenReturn(Arrays.asList(airBeamFlight1));

    App app = spy(App.class);
    when(app.getCarrierConnectors())
      .thenReturn(new CarrierConnector[]{airJazz, airMoon, airBeam});
    List<Flight> actual = app.getFlights();
    Flight[] expected = {
      airMoonFlight1, airJazzFlight2, airJazzFlight1, airBeamFlight1
    };
    assertArrayEquals(expected, actual.toArray());
  }

  @Test
  public void testGetFlightsHasLimitedResults() throws Exception
  {
    AirJazz airJazz = mock(AirJazz.class);
    List<Flight> flights = IntStream.range(1, 100)
      .mapToObj(i -> new Flight(
        Carrier.AIR_JAZZ,
        Integer.toString(i),
        i * 100,
        LocalTime.of(12, 12),
        LocalTime.of(13, 12)
      ))
      .collect(Collectors.toList());

    when(airJazz.getFlights()).thenReturn(flights);
    App app = spy(App.class);
    when(app.getCarrierConnectors())
      .thenReturn(new CarrierConnector[]{airJazz});
    List<Flight> actual = app.getFlights();
    assertEquals(50, actual.size());
  }

  @Test
  public void testGetFlightsReturnEvenIfACarrierCrashed() throws Exception
  {

    CarrierConnector airCrash = new CarrierConnector(){

      @Override
      public List<Flight> getFlights() {
        throw new RuntimeException("Air crash!");
      }
    };

    AirJazz airJazz = mock(AirJazz.class);
    when(airJazz.getFlights()).thenReturn(Arrays.asList(airJazzFlight1));
    App app = spy(App.class);
    when(app.getCarrierConnectors())
      .thenReturn(new CarrierConnector[]{airJazz, airCrash});
    List<Flight> actual = app.getFlights();
    Flight[] expected = {airJazzFlight1};
    assertArrayEquals(expected, actual.toArray());
  }
}

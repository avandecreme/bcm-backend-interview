package org.flybcm;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import okhttp3.OkHttpClient;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpRequestHelper.class)
public class AirJazzTest {

  @Mock
  OkHttpClient okHttpClient;

  @Test
  public void testGetFlights() throws Exception
  {
    String response =
      "[" +
      "  {" +
      "    \"id\": \"e597f52b-02ad-40f5-8810-8aa7d8d8769c\"," +
      "    \"price\": 0.3," +
      "    \"dtime\": \"7:02 AM\"," +
      "    \"atime\": \"2:50 AM\"" +
      "  }" +
      "]";
    PowerMockito.whenNew(OkHttpClient.class).withNoArguments().thenReturn(okHttpClient);
    PowerMockito.mockStatic(HttpRequestHelper.class);
    when(HttpRequestHelper.makeStringHttpRequest(anyString(), anyString()))
      .thenReturn(response);
    List<Flight> actuals = new AirJazz().getFlights();
    Flight[] expected = {
      new Flight(
        Carrier.AIR_JAZZ,
        "e597f52b-02ad-40f5-8810-8aa7d8d8769c",
        30,
        LocalTime.of(7, 02),
        LocalTime.of(2, 50)),
    };
    assertArrayEquals(expected, actuals.toArray());
  }
}

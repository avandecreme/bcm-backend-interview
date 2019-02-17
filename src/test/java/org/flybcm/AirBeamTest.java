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
public class AirBeamTest {

  @Mock
  OkHttpClient okHttpClient;

  @Test
  public void testGetFlights() throws Exception
  {
    String response =
      "id,p,departure,arrival\n" +
      "14e6f085-b5b5-48f7-b3c5-6c6202d50f48,501.33,4:12 AM,5:02 AM\n" +
      "46ea7e60-c0a4-429a-8917-3917d903236d,497.0,7:22 PM,5:58 AM\n";
    PowerMockito.whenNew(OkHttpClient.class).withNoArguments().thenReturn(okHttpClient);
    PowerMockito.mockStatic(HttpRequestHelper.class);
    when(HttpRequestHelper.makeStringHttpRequest(anyString(), anyString()))
      .thenReturn(response);
    List<Flight> actuals = new AirBeam().getFlights();
    Flight[] expected = {
      new Flight(
        Carrier.AIR_BEAM,
        "14e6f085-b5b5-48f7-b3c5-6c6202d50f48",
        50133,
        LocalTime.of(4, 12),
        LocalTime.of(5, 02)),
      new Flight(
        Carrier.AIR_BEAM,
        "46ea7e60-c0a4-429a-8917-3917d903236d",
        49700,
        LocalTime.of(19, 22),
        LocalTime.of(5, 58)),
    };
    assertArrayEquals(expected, actuals.toArray());
  }
}

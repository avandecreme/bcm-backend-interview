package org.flybcm;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class AirBeam implements CarrierConnector {

  // TODO: this should be in a configuration file
  private static final String URL = "https://my.api.mockaroo.com/air-beam/flights";
  private static final String API_KEY = "dd764f40";

  @Override
  public List<Flight> getFlights() {
    try {
      String csvResponse = HttpRequestHelper.makeStringHttpRequest(URL, API_KEY);
      try (Reader reader = new StringReader(csvResponse)) {
        Iterable<CSVRecord> records =  CSVFormat.RFC4180
          .withFirstRecordAsHeader().parse(reader);
        return StreamSupport.stream(records.spliterator(), false)
          .map(AirBeam::csvRecordToFlight)
          .collect(Collectors.toList());
      }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  private static final BigDecimal HUNDRED = new BigDecimal(100);
  private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("h:m a");

  private static Flight csvRecordToFlight(CSVRecord record) {
    return new Flight(
      Carrier.AIR_BEAM,
      record.get("id"),
      new BigDecimal(record.get("p")).multiply(HUNDRED).intValue(),
      LocalTime.parse(record.get("departure"), dateFormat),
      LocalTime.parse(record.get("arrival"), dateFormat)
    );
  }

}

package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.Coord;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collections of coords in Json.
 *
 * @param coords the list of coords
 */
public record VolleyJson(
    @JsonProperty("coordinates") List<CoordJson> coords) {

  /**
   * Generates the volley as a list of coordinates.
   *
   * @return the coordinates inside the volley
   */
  public List<Coord> generateVolley() {
    List<Coord> volley = new ArrayList<>();
    for (CoordJson coord : coords) {
      volley.add(coord.generateCoord());
    }
    return volley;
  }
}

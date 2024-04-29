package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.Coord;

/**
 * Represents a JsonCoordinate.
 *
 * @param x the X Value.
 * @param y the Y Value.
 */
public record CoordJson(
    @JsonProperty("x") int x,
    @JsonProperty("y") int y) {

  /**
   * Generates a coord from a given json
   *
   * @return the coord corresponding with the Json.
   */
  public Coord generateCoord() {
    return new Coord(x, y);
  }
}

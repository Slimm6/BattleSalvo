package cs3500.pa04.model;

import cs3500.pa04.json.CoordJson;

/**
 * Represents a coordinate on the board.
 *
 * @param x the x coordinate
 * @param y the y coordinate
 */
public record Coord(int x, int y) {

  /**
   * Converts a coord to json.
   *
   * @return The coord as a CoordJson
   */
  public CoordJson toJson() {
    return new CoordJson(x, y);
  }
}

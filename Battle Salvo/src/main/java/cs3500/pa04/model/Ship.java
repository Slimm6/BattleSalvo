package cs3500.pa04.model;

import cs3500.pa04.json.CoordJson;
import cs3500.pa04.json.ShipJson;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Ship on the board.
 */
public class Ship {
  private final ShipType shipType;
  private int shipHealth;
  private List<Coord> shipPlacement;
  private Coord start;
  private Orientation orientation;

  /**
   * Creates an instant of a ship.
   *
   * @param ship the type of ship it is
   */
  public Ship(ShipType ship) {
    shipType = ship;
    shipHealth = ship.getShipSize();
    shipPlacement = new ArrayList<>();
  }

  /**
   * Gets the ship type.
   *
   * @return the ship type
   */
  public ShipType getShipType() {
    return shipType;
  }

  /**
   * gives the ship a set of coordinates that it occupies.
   *
   * @param shipCoords the coordinates the ship occupies
   */
  public void placeShip(List<Coord> shipCoords) {
    shipPlacement = shipCoords;
  }

  /**
   * Sets the start coordinate of the ship.
   *
   * @param c the start coordinate
   */
  public void setStartCoord(Coord c) {
    start = c;
  }

  /**
   * Sets the orientation of the ship.
   *
   * @param o the orientation
   */
  public void setOrientation(Orientation o) {
    orientation = o;
  }

  /**
   * updates a ship if hit by an attack.
   *
   * @param attack the coordinate the ship is being attacked
   */
  public void updateShip(Coord attack) {
    if (shipPlacement.contains(attack)) {
      shipHealth--;
      shipPlacement.remove(attack);
    }
  }

  /**
   * Checks if the ship is sunk.
   *
   * @return true if the ship isn't sunk, false otherwise
   */
  public boolean isNotSunk() {
    return shipHealth != 0;
  }

  /**
   * Gets the ships placement on the board

   * @return the ships placement
   */
  public List<Coord> getShipPlacement() {
    return shipPlacement;
  }

  /**
   * Converts the ship to Json.
   *
   * @return The ship as a ShipJson
   */
  public ShipJson toJson() {
    CoordJson coord = new CoordJson(start.x(), start.y());
    return new ShipJson(coord, shipPlacement.size(), orientation);
  }
}

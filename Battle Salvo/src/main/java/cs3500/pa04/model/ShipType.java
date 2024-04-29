package cs3500.pa04.model;

/**
 * Represents the different kinds of ships
 */
public enum ShipType {
  CARRIER(6, 'C'),
  BATTLESHIP(5, 'B'),
  DESTROYER(4, 'D'),
  SUBMARINE(3, 'S');

  private final int shipSize;
  private final char symbol;

  /**
   * Creates instance of a ShipType.
   *
   * @param shipSize the size of the ship
   * @param symbol the symbol that represents the ship
   */
  ShipType(int shipSize, char symbol) {
    this.symbol = symbol;
    this.shipSize = shipSize;
  }

  /**
   * Gets the ship size.
   *
   * @return the ship size
   */
  public int getShipSize() {
    return shipSize;
  }

  /**
   * Gets the symbol.
   *
   * @return the symbol
   */
  public char getSymbol() {
    return symbol;
  }
}

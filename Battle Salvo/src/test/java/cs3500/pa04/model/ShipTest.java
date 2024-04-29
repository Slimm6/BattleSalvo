package cs3500.pa04.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Test class for the Ship class.
 */
class ShipTest {
  Ship shipBattleship = new Ship(ShipType.BATTLESHIP);
  Ship shipCarrier = new Ship(ShipType.CARRIER);
  Ship shipSubmarine = new Ship(ShipType.SUBMARINE);
  Ship shipDestroyer = new Ship(ShipType.DESTROYER);

  /**
   * Tests the getShipType function
   */
  @Test
  void getShipTypeTest() {
    assertEquals(shipBattleship.getShipType(), ShipType.BATTLESHIP);
    assertEquals(shipCarrier.getShipType(), ShipType.CARRIER);
    assertEquals(shipSubmarine.getShipType(), ShipType.SUBMARINE);
    assertEquals(shipDestroyer.getShipType(), ShipType.DESTROYER);
  }

  /**
   * Tests the placeShip function.
   */
  @Test
  void placeShipTest() {
    List<Coord> test = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      test.add(new Coord(i, 0));
    }
    shipBattleship.placeShip(test);
    assertEquals(shipBattleship.getShipPlacement().toString(), test.toString());
  }

  /**
   * Tests the updateShip function.
   */
  @Test
  void updateShipTest() {
    List<Coord> test = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      test.add(new Coord(i, 0));
    }
    shipBattleship.placeShip(test);
    shipBattleship.updateShip(new Coord(0, 0));
    test.remove(new Coord(0, 0));
    assertEquals(test.toString(), shipBattleship.getShipPlacement().toString());
  }

  /**
   * Tests the isNotSunk function.
   */
  @Test
  void isNotSunkTest() {
    List<Coord> test = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      test.add(new Coord(i, 0));
    }
    assertTrue(shipBattleship.isNotSunk());
    shipBattleship.placeShip(test);
    for (int i = 0; i < 5; i++) {
      shipBattleship.updateShip(new Coord(i, 0));
    }
    assertFalse(shipBattleship.isNotSunk());
  }
}
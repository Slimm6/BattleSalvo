package cs3500.pa04.model;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the AiPlayer class.
 */
class AiPlayerTest {
  private AiPlayer ai;
  private List<Ship> setUpTest;
  private Board aiShotBoard;

  /**
   * Sets up the test for AiPlayer.
   */
  @BeforeEach
  void setUp() {
    aiShotBoard = new Board(new Random(0));
    Board aiGameBoard = new Board(new Random(0));
    ai = new AiPlayer("Test", 0, aiShotBoard, aiGameBoard);
    Map<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    int[] boardCoordinates = new int[]{6, 6};
    setUpTest = ai.setup(boardCoordinates[0], boardCoordinates[1], specifications);
  }

  /**
   * Tests the name function.
   */
  @Test
  void nameTest() {
    assertEquals(ai.name(), "Test");
  }

  /**
   * Tests the setup function.
   */
  @Test
  void setupTest() {
    Map<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    for (int i = 0; i < setUpTest.size(); i++) {
      assertEquals(setUpTest.get(i).getShipType(), getShips(specifications).get(i).getShipType());
    }
  }

  /**
   * Gets the ships from a given map
   *
   * @param specifications a map of shiptype and how many of that ship exist
   * @return a list of ships
   */
  private List<Ship> getShips(Map<ShipType, Integer> specifications) {
    List<Ship> ships = new ArrayList<>();
    List<ShipType> shipTypes = new ArrayList<>(specifications.keySet().stream().toList());
    shipTypes.sort(Comparator.comparingInt(ShipType::getShipSize));
    for (ShipType shipType : shipTypes) {
      for (int i = 0; i < specifications.get(shipType); i++) {
        ships.add(new Ship(shipType));
      }
    }
    return ships;
  }

  /**
   * Tests the takeShots function.
   */
  @Test
  void takeShots() {
    testPrep();
    List<Coord> coordsShot = new ArrayList<>();
    coordsShot.add(new Coord(1, 3));
    coordsShot.add(new Coord(0, 1));
    coordsShot.add(new Coord(5, 5));
    coordsShot.add(new Coord(4, 5));
    List<Coord> takeShots = ai.takeShots();
    assertEquals(coordsShot, takeShots);

    coordsShot = new ArrayList<>();
    coordsShot.add(new Coord(2, 0));
    coordsShot.add(new Coord(3, 4));
    coordsShot.add(new Coord(4, 2));
    coordsShot.add(new Coord(5, 2));
    takeShots = ai.takeShots();
    assertEquals(coordsShot, takeShots);

    coordsShot = new ArrayList<>();
    coordsShot.add(new Coord(4, 4));
    coordsShot.add(new Coord(3, 5));
    coordsShot.add(new Coord(0, 5));
    coordsShot.add(new Coord(2, 2));
    takeShots = ai.takeShots();
    assertEquals(coordsShot, takeShots);

  }

  /**
   * Prepares a test by getting parameters for the board.
   */
  private void testPrep() {
    Map<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    int[] boardCoordinates = new int[]{6, 6};
    ai.setup(boardCoordinates[0], boardCoordinates[1], specifications);
  }

  /**
   * tests the reportDamage function.
   */
  @Test
  void reportDamage() {
    testPrep();
    List<Coord> coordsShot = new ArrayList<>();
    coordsShot.add(new Coord(1, 3));
    coordsShot.add(new Coord(0, 1));
    coordsShot.add(new Coord(5, 5));
    coordsShot.add(new Coord(4, 5));
    List<Coord> successfulHits = ai.reportDamage(coordsShot);
    coordsShot.remove(new Coord(5, 5));
    coordsShot.remove(new Coord(4, 5));
    assertEquals(successfulHits, coordsShot);
  }

  /**
   * Tests the successfulHits function.
   */
  @Test
  void successfulHits() {
    List<Coord> coordsShot = new ArrayList<>();
    char[][] board = aiShotBoard.getBoard();
    board[1][3] = 'H';
    board[5][5] = 'H';
    coordsShot.add(new Coord(1, 3));
    coordsShot.add(new Coord(0, 1));
    coordsShot.add(new Coord(5, 5));
    coordsShot.add(new Coord(4, 5));
    List<Coord> successfulHits = ai.reportDamage(coordsShot);
    ai.successfulHits(successfulHits);
    assertEquals(Arrays.deepToString(aiShotBoard.getBoard()), Arrays.deepToString(board));
  }
}
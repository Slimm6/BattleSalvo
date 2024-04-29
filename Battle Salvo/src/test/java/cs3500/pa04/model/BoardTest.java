package cs3500.pa04.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the Board class.
 */
class BoardTest {
  private Board test;
  private char[][] testBoard;

  /**
   * Sets up the tests for Board.
   */
  @BeforeEach
  void setUp() {
    List<Ship> testShips = new ArrayList<>();
    testShips.add(new Ship(ShipType.BATTLESHIP));
    testShips.add(new Ship(ShipType.CARRIER));
    testShips.add(new Ship(ShipType.SUBMARINE));
    testShips.add(new Ship(ShipType.DESTROYER));
    Random randomizer = new Random(0);
    test = new Board(randomizer);
    test.generateBoard(10, 10, testShips);
    testBoard = test.getBoard();
  }

  /**
   * Tests the possibleShots function
   */
  @Test
  void possibleShotsTest() {
    assertEquals(test.possibleShots().size(), 100);
    test.updateBoard(0, 0, 'M');
    assertEquals(test.possibleShots().size(), 99);
    test.updateBoard(0, 1, 'H');
    assertEquals(test.possibleShots().size(), 98);
  }

  /**
   * tests the updateBoard function.
   */
  @Test
  void updateBoardTest() {
    assertEquals(test.getPositionOnBoard(4, 5), '0');
    test.updateBoard(4, 5, 'H');
    assertEquals(test.getPositionOnBoard(4, 5), 'H');
  }

  /**
   * Tests the getPositionOnBoard function.
   */
  @Test
  void getPositionOnBoardTest() {
    assertEquals(test.getPositionOnBoard(1, 1), 'C');
    assertEquals(test.getPositionOnBoard(4, 2), 'D');
    assertEquals(test.getPositionOnBoard(0, 0), '0');
  }

  /**
   * Tests the getBoard function.
   */
  @Test
  void getBoardTest() {
    assertEquals(test.getBoard(), testBoard);
  }

  /**
   * tests the getRemainingShips method.
   */
  @Test
  void getRemainingShipsTest() {
    assertEquals(test.getRemainingShips(), 4);
  }

  /**
   * Tests the updateShips method.
   */
  @Test
  void updateShips() {
    test.updateShips(new Coord(3, 2));
    test.updateShips(new Coord(4, 2));
    test.updateShips(new Coord(5, 2));
    test.updateShips(new Coord(6, 2));
    assertEquals(test.getRemainingShips(), 3);
  }
}
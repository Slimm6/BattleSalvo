package cs3500.pa04.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;
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
 * Tests the UserPlayer Class.
 */
class UserPlayerTest {

  private UserPlayer user;
  private Board userShotBoard;


  /**
   * Prepares the test
   */
  @BeforeEach
  public void setUp() {
    userShotBoard = new Board(new Random(0));
    Map<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    String input = "0 0 0 1 0 2 0 3";
    Board userGameBoard = new Board(new Random(0));
    user = new UserPlayer("Test", new StringReader(input), userShotBoard, userGameBoard);
    int[] boardCoordinates = new int[] {6, 6};
    user.setup(boardCoordinates[0], boardCoordinates[1], specifications);
  }

  /**
   * Tests the name function.
   */
  @Test
  void nameTest() {
    assertEquals(user.name(), "Test");
  }

  /**
   * Tests the setup function.
   */
  @Test
  void setupTest() {
    int[] boardCoordinates = new int[] {6, 6};
    Map<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    List<Ship> setUpTest = user.setup(boardCoordinates[0], boardCoordinates[1], specifications);
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
  void takeShotsTest() {
    List<Coord> test = new ArrayList<>();
    test.add(new Coord(0, 0));
    test.add(new Coord(0, 1));
    test.add(new Coord(0, 2));
    test.add(new Coord(0, 3));
    List<Coord> shots = user.takeShots();
    assertEquals(test, shots);
  }

  /**
   * Tests the reportDamage function.
   */
  @Test
  void reportDamageTest() {
    List<Coord> coordsShot = new ArrayList<>();
    coordsShot.add(new Coord(1, 3));
    coordsShot.add(new Coord(0, 1));
    coordsShot.add(new Coord(3, 3));
    coordsShot.add(new Coord(4, 5));
    List<Coord> successfulHits = user.reportDamage(coordsShot);
    assertEquals(successfulHits, new ArrayList<>());
  }

  /**
   * Tests the successfulHits function
   */
  @Test
  void successfulHitsTest() {
    List<Coord> coordsShot = new ArrayList<>();
    char[][] board = userShotBoard.getBoard();
    board[1][3] = 'H';
    board[5][5] = 'H';
    coordsShot.add(new Coord(1, 3));
    coordsShot.add(new Coord(0, 1));
    coordsShot.add(new Coord(5, 5));
    coordsShot.add(new Coord(4, 5));
    List<Coord> successfulHits = user.reportDamage(coordsShot);
    user.successfulHits(successfulHits);
    coordsShot.remove(new Coord(4, 5));
    assertEquals(Arrays.deepToString(userShotBoard.getBoard()), Arrays.deepToString(board));
  }
}

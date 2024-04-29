package cs3500.pa04.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the BattleSalvoModel class.
 */
class BattleSalvoModelTest {

  private BattleSalvoModel testModel;
  private UserPlayer user;
  private AiPlayer ai;
  private Board aiGameBoard;
  private Board userShotBoard;
  private Board userGameBoard;
  private static final String INPUT = "0 0 0 1 0 2 0 3";

  /**
   * Sets up tests for the BattleSalvoModel class.
   */
  @BeforeEach
  void setUp() {
    Map<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    userShotBoard = new Board(new Random(0));
    userGameBoard = new Board(new Random(0));
    user = new UserPlayer("Player",  new StringReader(INPUT), userShotBoard,
        userGameBoard);
    user.setup(8, 8, specifications);
    Board aiShotBoard = new Board(new Random(0));
    aiGameBoard = new Board(new Random(0));
    ai = new AiPlayer("Ai", 0, aiShotBoard, aiGameBoard);
    ai.setup(8, 8, specifications);
    testModel = new BattleSalvoModel(user, ai, aiGameBoard, userGameBoard);
  }

  /**
   * Tests the attack method
   */
  @Test
  void attackTest() {
    testModel.attack();
    assertEquals(userShotBoard.getPositionOnBoard(0, 0), 'H');
    assertEquals(userShotBoard.getPositionOnBoard(0, 1), 'H');
    assertEquals(userShotBoard.getPositionOnBoard(0, 2), 'H');
    assertEquals(userShotBoard.getPositionOnBoard(0, 3), 'M');

    System.out.println(Arrays.deepToString(userGameBoard.getBoard()));
    assertEquals(userGameBoard.getPositionOnBoard(0, 5), 'M');
    assertEquals(userGameBoard.getPositionOnBoard(0, 6), 'M');
    assertEquals(userGameBoard.getPositionOnBoard(6, 0), 'M');
    assertEquals(userGameBoard.getPositionOnBoard(6, 6), 'M');
  }

  /**
   * Tests the isGameOver method.
   */
  @Test
  void isGameOverTest() {
    Map<ShipType, Integer> specificationsNotEmpty = new HashMap<>();
    specificationsNotEmpty.put(ShipType.CARRIER, 1);
    Map<ShipType, Integer> specificationsEmpty = new HashMap<>();
    testModel = new BattleSalvoModel(user, ai, aiGameBoard, userGameBoard);
    user.setup(8, 8, specificationsEmpty);
    ai.setup(8, 8, specificationsNotEmpty);
    assertTrue(testModel.isGameOver());

    testModel = new BattleSalvoModel(user, ai, aiGameBoard, userGameBoard);
    user.setup(8, 8, specificationsEmpty);
    ai.setup(8, 8, specificationsEmpty);
    assertTrue(testModel.isGameOver());

    testModel = new BattleSalvoModel(user, ai, aiGameBoard, userGameBoard);
    user.setup(8, 8, specificationsNotEmpty);
    ai.setup(8, 8, specificationsNotEmpty);
    assertFalse(testModel.isGameOver());
  }

  /**
   * Tests the getGameResults method.
   */
  @Test
  void getGameResultsTest() {
    Map<ShipType, Integer> specificationsNotEmpty = new HashMap<>();
    specificationsNotEmpty.put(ShipType.CARRIER, 1);
    Map<ShipType, Integer> specificationsEmpty = new HashMap<>();
    user.setup(8, 8, specificationsEmpty);
    ai.setup(8, 8, specificationsNotEmpty);
    testModel = new BattleSalvoModel(user, ai, aiGameBoard, userGameBoard);
    assertEquals(GameResult.LOSE, testModel.getGameResults());

    user.setup(8, 8, specificationsEmpty);
    ai.setup(8, 8, specificationsEmpty);
    testModel = new BattleSalvoModel(user, ai, aiGameBoard, userGameBoard);
    assertEquals(GameResult.DRAW, testModel.getGameResults());

    user.setup(8, 8, specificationsNotEmpty);
    ai.setup(8, 8, specificationsEmpty);
    testModel = new BattleSalvoModel(user, ai, aiGameBoard, userGameBoard);
    assertEquals(GameResult.WIN, testModel.getGameResults());
  }
}
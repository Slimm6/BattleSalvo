package cs3500.pa04.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa04.model.GameResult;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the class BattleSalvoTerminalView.
 */
class BattleSalvoTerminalViewTest {
  private ByteArrayOutputStream output;
  private BattleSalvoTerminalView view;

  /**
   * Sets the tests for the View Class.
   */
  @BeforeEach
  void setUp() {
    output = new ByteArrayOutputStream();
    PrintStream print = new PrintStream(output);
    view = new BattleSalvoTerminalView(print, new StringReader("test"));
  }

  /**
   * tests the greetPlayer function.
   */
  @Test
  void greetPlayerTest() {
    view.greetPlayer();
    assertEquals("Welcome to BattleSalvo!",
        output.toString().replaceAll("\\R+", ""));
  }

  /**
   * tests the greetPlayer function.
   */
  @Test
  void invalidActionTest() {
    view.invalidAction();
    assertEquals("Invalid Input.",
        output.toString().replaceAll("\\R+", ""));
  }

  /**
   * tests the displayBoard function.
   */
  @Test
  void displayBoardTest() {
    char[][] test = new char[2][2];
    for (int i = 0; i < test.length; i++) {
      for (int j = 0; j < test.length; j++) {
        test[i][j] = '0';
      }
    }
    view.displayBoard(test);
    assertEquals(
        """
        0 0
        0 0
        -----------------------
        """, output.toString());
  }

  /**
   * Tests the outputResult function when given win.
   */
  @Test
  void outputResultsWinTest() {
    GameResult test = GameResult.WIN;
    view.outputResults(test);
    assertEquals("You Win!", output.toString());
  }

  /**
   * Tests the outputResult function when given lose.
   */
  @Test
  void outputResultsLoseTest() {
    GameResult test = GameResult.LOSE;
    view.outputResults(test);
    assertEquals("You Lost!", output.toString());
  }

  /**
   * Tests the outputResult function when given tied.
   */
  @Test
  void outputResultsTiedTest() {
    GameResult test = GameResult.DRAW;
    view.outputResults(test);
    assertEquals("You Tied!", output.toString());
  }

  /**
   * Tests the aksBoard function.
   */
  @Test
  void askBoardSizeTest() {
    String input = "8 8";
    view = new BattleSalvoTerminalView(
        new PrintStream(output), new StringReader(input));
    view.askBoardSize();
    assertEquals(output.toString(), """
      Please enter a valid height and width (6-15):
      -----------------------
        """);
  }


  /**
   * Tests the askFleet function.
   */
  @Test
  void askFleetTest() {
    String input = "1 1 1 1";
    view = new BattleSalvoTerminalView(
        new PrintStream(output), new StringReader(input));
    view.askFleet(8);
    assertEquals(output.toString(), """
        Please enter how many of each ship you want
        maximum of 8 ships. must have atleast 1 of each ship
        -----------------------
        """);
  }

  /**
   * Tests the askShot function
   */
  @Test
  void askShotsTest() {
    String input = """
        0 0
        0 1
        0 2
        0 3""";
    view = new BattleSalvoTerminalView(
        new PrintStream(output), new StringReader(input));
    view.askShots(4);
    assertEquals(output.toString(), """
        please enter 4 Shots.
        You cannot fire in the same place twice.
        -----------------------
        """);
  }
}
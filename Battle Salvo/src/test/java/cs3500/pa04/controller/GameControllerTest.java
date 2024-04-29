package cs3500.pa04.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests the GameController class.
 */
class GameControllerTest {

  /**
   * Tests the run function.
   */
  @Test
  void testRun() {
    String input = controllerInput();
    ByteArrayOutputStream op = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(op);
    StringReader terminalInput = new StringReader(input);
    GameController test = new GameController(output, terminalInput);
    assertEquals(controllerOutput(), op.toString());
    try {
      test.run();
    } catch (Exception e) {
      //intentionally ignored
    }
  }

  /**
   * Gets the output of the controller
   *
   * @return the expected output
   */
  private String controllerOutput() {
    return """
        Welcome to BattleSalvo!
        Please enter a valid height and width (6-15):
        -----------------------
        Invalid Input.
        Please enter a valid height and width (6-15):
        -----------------------
        Invalid Input.
        Please enter a valid height and width (6-15):
        -----------------------
        Invalid Input.
        Please enter a valid height and width (6-15):
        -----------------------
        Invalid Input.
        Please enter a valid height and width (6-15):
        -----------------------
        Invalid Input.
        Please enter a valid height and width (6-15):
        -----------------------
        Please enter how many of each ship you want
        maximum of 6 ships. must have atleast 1 of each ship
        -----------------------
        Invalid Input.
        Please enter how many of each ship you want
        maximum of 6 ships. must have atleast 1 of each ship
        -----------------------
        Invalid Input.
        Please enter how many of each ship you want
        maximum of 6 ships. must have atleast 1 of each ship
        -----------------------
        Invalid Input.
        Please enter how many of each ship you want
        maximum of 6 ships. must have atleast 1 of each ship
        -----------------------
        """;
  }

  /**
   * The input of the controller
   *
   * @return controller input.
   */
  private String controllerInput() {
    return """
    3 17
    3 10
    10 3
    10 17
    17 10
    6 6
    1 1 1 a
    0 0 0 0
    10 10 10 10
    1 1 1 1
      """;
  }
}
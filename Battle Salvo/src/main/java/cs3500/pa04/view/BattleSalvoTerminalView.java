package cs3500.pa04.view;

import cs3500.pa04.model.GameResult;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Represents the Terminal View for Battle Salvo.
 */
public class BattleSalvoTerminalView implements View {
  private final Appendable output;
  private final Scanner reader;

  /**
   * Creates an instance of BattleSalvoTerminalView.
   *
   * @param out the output of the view
   * @param read the input of the view
   */
  public BattleSalvoTerminalView(Appendable out, Readable read) {
    output = out;
    reader = new Scanner(read);
  }

  @Override
  public void greetPlayer() {
    displayMessage("Welcome to BattleSalvo!\n");
  }

  @Override
  public void displayBoard(char[][] board) {
    StringBuilder build = new StringBuilder();
    for (char[] chars : board) {
      for (int j = 0; j < board[0].length; j++) {
        if (j < board[0].length - 1) {
          build.append(chars[j]).append(" ");
        } else {
          build.append(chars[j]);
        }
      }
      build.append("\n");
    }
    build.append("-----------------------\n");
    displayMessage(build.toString());
  }

  @Override
  public int[] askBoardSize() {
    displayMessage("""
      Please enter a valid height and width (6-15):
      -----------------------
        """);
    try {
      String x = reader.next();
      String y = reader.next();
      return new int[]{Integer.parseInt(x), Integer.parseInt(y)};
    } catch (NumberFormatException | InputMismatchException e) {
      invalidAction();
      return askBoardSize();
    }
  }

  @Override
  public void invalidAction() {
    displayMessage("Invalid Input.\n");
  }

  @Override
  public int[] askFleet(int maxShips) {
    String build = "Please enter how many of each ship you want\n"
        + "maximum of "
        + maxShips
        + " ships. must have atleast 1 of each ship\n"
        + "-----------------------\n";
    displayMessage(build);
    try {
      String numCarrier = reader.next();
      String numBattleship = reader.next();
      String numDestroyer = reader.next();
      String numSubmarines = reader.next();
      return new int[]{Integer.parseInt(numCarrier), Integer.parseInt(numBattleship),
          Integer.parseInt(numDestroyer), Integer.parseInt(numSubmarines)};
    } catch (NumberFormatException | InputMismatchException e) {
      invalidAction();
      return askFleet(maxShips);
    }
  }

  @Override
  public int[][] askShots(int numShips) {
    String build = "please enter " + numShips + " Shots.\n"
        + "You cannot fire in the same place twice.\n"
        + "-----------------------\n";
    displayMessage(build);
    try {
      int[][] output = new int[numShips][2];
      for (int i = 0; i < numShips; i++) {
        String x = reader.next();
        String y = reader.next();
        output[i] = new int[]{Integer.parseInt(x), Integer.parseInt(y)};
      }
      return output;
    } catch (NumberFormatException | InputMismatchException e) {
      invalidAction();
      return askShots(numShips);
    }
  }

  @Override
  public void outputResults(GameResult result) {
    if (result == GameResult.WIN) {
      displayMessage("You Win!");
    } else if (result == GameResult.LOSE) {
      displayMessage("You Lost!");
    } else {
      displayMessage("You Tied!");
    }
  }

  /**
   * Adds a message to the output
   *
   * @param message the message being displayed
   */
  private void displayMessage(String message) {
    try {
      output.append(message);
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to use input");
    }
  }
}

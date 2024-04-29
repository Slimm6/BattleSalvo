package cs3500.pa04.view;

import cs3500.pa04.model.GameResult;

/**
 * Represents the view of the program
 */
public interface View {

  /**
   * Greets the player.
   */
  void greetPlayer();

  /**
   * Displays the board for the user to see.
   *
   * @param board the board
   */
  void displayBoard(char[][] board);

  /**
   * asks the user for the board size.
   *
   * @return the board size
   */
  int[] askBoardSize();

  /**
   * Tells the user their action is invalid.
   */
  void invalidAction();

  /**
   * Asks the user for the number of boats they want.
   *
   * @param maxShips the maximum number of ships the user can input.
   * @return the number of each kind of boat.
   */
  int[] askFleet(int maxShips);

  /**
   * asks the user to input their shots.
   *
   * @param numShips the number of ships
   * @return the number of shots fired and where they are fired
   */
  int[][] askShots(int numShips);

  /**
   * Outputs the results of the game.
   *
   * @param result the game results.
   */
  void outputResults(GameResult result);
}

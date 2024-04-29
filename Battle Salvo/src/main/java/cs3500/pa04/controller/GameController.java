package cs3500.pa04.controller;

import cs3500.pa04.model.AiPlayer;
import cs3500.pa04.model.BattleSalvoModel;
import cs3500.pa04.model.Board;
import cs3500.pa04.model.GameResult;
import cs3500.pa04.model.Player;
import cs3500.pa04.model.ShipType;
import cs3500.pa04.model.UserPlayer;
import cs3500.pa04.view.BattleSalvoTerminalView;
import cs3500.pa04.view.View;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A Controller that runs the game
 */
public class GameController implements Controller {
  private final View view;
  private Player user;
  private Player ai;
  private final Readable input;
  private final Board shotboardai;
  private final Board gameboardai;
  private final Board gameboarduser;
  private final Board shotboarduser;
  private final BattleSalvoModel model;

  /**
   * Creates an instance of Game Controller.
   *
   * @param output the output of the program.
   * @param in the input of the program.
   */
  public GameController(Appendable output, Readable in) {
    input = in;
    view = new BattleSalvoTerminalView(output, input);
    view.greetPlayer();
    shotboardai = new Board(new Random());
    gameboardai = new Board(new Random());
    shotboarduser = new Board(new Random());
    gameboarduser = new Board(new Random());
    getParameters();
    model = new BattleSalvoModel(user, ai, gameboardai, gameboarduser);
  }

  /**
   * Gets the parameters for player creation.
   */
  private void getParameters() {
    int[] boardSize = getBoardSize();
    int numShips = Math.min(boardSize[0], boardSize[1]);
    Map<ShipType, Integer> specifications = getFleet(numShips);
    ai = new AiPlayer("AI", shotboardai, gameboardai);
    ai.setup(boardSize[0], boardSize[1], specifications);
    user = new UserPlayer("Player", input, shotboarduser, gameboarduser);
    user.setup(boardSize[0], boardSize[1], specifications);
  }

  /**
   * Gets a valid board size.
   *
   * @return the board size
   */
  private int[] getBoardSize() {
    boolean actionCompleted = false;
    int[] boardSize = new int[2];
    while (!actionCompleted) {
      boardSize = view.askBoardSize();
      if (boardSize[0] >= 6 && boardSize[0] <= 15) {
        if (boardSize[1] >= 6 && boardSize[1] <= 15) {
          actionCompleted = true;
        } else {
          view.invalidAction();
        }
      } else {
        view.invalidAction();
      }
    }
    return boardSize;
  }

  /**
   * gets the valid fleet the user wants.
   *
   * @param maxShips the maximum amount of ships the user can input.
   * @return a map of a ship type and the number of how many of that shiptype the fleet contains
   */
  private Map<ShipType, Integer> getFleet(int maxShips) {
    boolean actionCompleted = false;
    int[] fleet = new int[4];
    while (!actionCompleted) {
      int count = 0;
      fleet = view.askFleet(maxShips);
      boolean atleastOneBoat = true;
      for (int j : fleet) {
        count += j;
        if (j == 0) {
          atleastOneBoat = false;
        }
      }
      if (count < maxShips && atleastOneBoat) {
        actionCompleted = true;
      } else {
        view.invalidAction();
      }
    }
    Map<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, fleet[0]);
    specifications.put(ShipType.BATTLESHIP, fleet[1]);
    specifications.put(ShipType.DESTROYER, fleet[2]);
    specifications.put(ShipType.SUBMARINE, fleet[3]);
    return specifications;
  }

  @Override
  public void run() {
    while (!model.isGameOver()) {
      view.displayBoard(shotboarduser.getBoard());
      view.displayBoard(gameboarduser.getBoard());
      model.attack();
    }
    endGame();
  }

  /**
   * Ends the game.
   */
  private void endGame() {
    GameResult result = model.getGameResults();
    view.outputResults(result);
  }
}


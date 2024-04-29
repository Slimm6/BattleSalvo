package cs3500.pa04.model;

import cs3500.pa04.view.BattleSalvoTerminalView;
import cs3500.pa04.view.View;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Represents a User Player.
 */
public class UserPlayer implements Player {
  private final String name;
  private final Board gameBoard;
  private final Board shotBoard;
  private final View accessShots;

  /**
   * Creates an instance of User Player.
   *
   * @param n the name of the player
   * @param i the readable for the player's inputs
   * @param shotBoard the shotBoard for the user
   * @param gameBoard the gameBoard for the user
   */
  public UserPlayer(String n, Readable i, Board shotBoard, Board gameBoard) {
    name = n;
    this.gameBoard = gameBoard;
    this.shotBoard = shotBoard;
    accessShots = new BattleSalvoTerminalView(new PrintStream(System.out), i);
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    List<Ship> ships = getShips(specifications);
    gameBoard.generateBoard(height, width, ships);
    shotBoard.generateBoard(height, width, new ArrayList<>());
    return ships;
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

  @Override
  public List<Coord> takeShots() {
    List<Coord> possibleShots = shotBoard.possibleShots();
    int[][] shots = getShots(accessShots, possibleShots, gameBoard.getRemainingShips());
    List<Coord> shotsFired = new ArrayList<>();
    for (int[] coords : shots) {
      shotsFired.add(new Coord(coords[0], coords[1]));
      shotBoard.updateBoard(coords[0], coords[1], 'M');
    }
    return shotsFired;
  }

  /**
   * Gets valid shots the player can make.
   *
   * @param accessShots The view to ask for shots
   * @param validShots a list of what shots haven't been done
   * @param size the number of ships the player has
   * @return the shots the player made
   */
  private int[][] getShots(View accessShots, List<Coord> validShots, int size) {
    boolean actionCompleted = false;
    int[][] shots = null;
    int numShots = size;
    if (numShots > validShots.size()) {
      numShots = validShots.size();
    }
    while (!actionCompleted) {
      boolean areShotsValid = true;
      List<Coord> updatedShots = new ArrayList<>(validShots);
      shots = accessShots.askShots(numShots);
      for (int[] coord : shots) {
        Coord current = new Coord(coord[0], coord[1]);
        if (!updatedShots.contains(current)) {
          areShotsValid = false;
        } else {
          updatedShots.remove(current);
        }
      }
      if (areShotsValid) {
        actionCompleted = true;
      } else {
        accessShots.invalidAction();
      }
    }
    return shots;
  }

  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    List<Coord> successfulHits = new ArrayList<>();
    for (Coord c : opponentShotsOnBoard) {
      if (gameBoard.getPositionOnBoard(c.x(), c.y()) == '0') {
        gameBoard.updateBoard(c.x(), c.y(), 'M');
      } else {
        gameBoard.updateBoard(c.x(), c.y(), 'H');
        successfulHits.add(c);
      }
      gameBoard.updateShips(c);
    }
    return successfulHits;
  }

  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    for (Coord c : shotsThatHitOpponentShips) {
      shotBoard.updateBoard(c.x(), c.y(), 'H');
    }
  }

  @Override
  public void endGame(GameResult result, String reason) {
    //Mentioned in Lecture that can be left blank. Applicable to PA04.
  }
}

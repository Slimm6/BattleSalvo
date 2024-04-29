package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents an AI Player.
 */
public class AiPlayer implements Player {
  private final String name;
  private final Board gameBoard;
  private final Board shotBoard;
  private final Random randomizer;

  /**
   * Creates an instance of AI Player.
   *
   * @param n the name of the player
   * @param shotBoard the shotBoard for the AI
   * @param gameBoard the gameBoard for the AI
   */
  public AiPlayer(String n, Board shotBoard, Board gameBoard) {
    name = n;
    randomizer = new Random();
    this.shotBoard = shotBoard;
    this.gameBoard = gameBoard;
  }

  /**
   * Creates an instance of AI Player.
   *
   * @param n the name of the player
   * @param seed the seed for the randomizer
   * @param shotBoard the shotBoard for the AI
   * @param gameBoard the gameBoard for the AI
   */
  public AiPlayer(String n, long seed, Board shotBoard, Board gameBoard) {
    name = n;
    randomizer = new Random(seed);
    this.shotBoard = shotBoard;
    this.gameBoard = gameBoard;
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
   * Gets the ships based off the specifications.
   *
   * @param specifications the specifications (How many ships of each ship type)
   * @return the ships
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
    Collections.shuffle(possibleShots, randomizer);
    int numShots = gameBoard.getRemainingShips();
    if (possibleShots.size() < numShots) {
      numShots = possibleShots.size();
    }
    List<Coord> priorityShots = getPriorityShots(possibleShots);
    possibleShots.removeAll(priorityShots);
    List<Coord> toShoot = new ArrayList<>();
    int count = 0;
    while (count < numShots && priorityShots.size() > 0) {
      toShoot.add(priorityShots.get(0));
      priorityShots.remove(0);
      count++;
    }
    toShoot.addAll(possibleShots.subList(0, numShots - count));
    for (Coord c : toShoot) {
      shotBoard.updateBoard(c.x(), c.y(), 'M');
    }
    return toShoot;
  }

  /**
   * Gets coordinates of shots that are most likely to be hits.
   *
   * @param possibleShots all possible shots
   * @return a list of high priority shots
   */
  private List<Coord> getPriorityShots(List<Coord> possibleShots) {
    List<Coord> priorityShots = new ArrayList<>();
    for (Coord c : possibleShots) {
      if (checkPriority(c)) {
        priorityShots.add(c);
      }
    }
    return priorityShots;
  }

  /**
   * Checks to see if a shot is high priority.
   *
   * @param coordToCheck the coordinate that is being checked.
   * @return true if the coordinate is high priority, false otherwise.
   */
  private boolean checkPriority(Coord coordToCheck) {
    int x = coordToCheck.x();
    int y = coordToCheck.y();
    int height = shotBoard.getBoard().length;
    if (x - 1 >= 0 && shotBoard.getPositionOnBoard(x - 1, y) == 'H') {
      return true;
    }
    if (x + 1 < height && shotBoard.getPositionOnBoard(x + 1, y) == 'H') {
      return true;
    }
    if (y - 1 >= 0 && shotBoard.getPositionOnBoard(x, y - 1) == 'H') {
      return true;
    }
    int width = shotBoard.getBoard()[0].length;
    return y + 1 < width && shotBoard.getPositionOnBoard(x, y + 1) == 'H';
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
    //N/A
  }
}
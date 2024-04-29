package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the board of an AI Player.
 */
public class Board {
  private char[][] board;
  private final Random randomizer;
  private List<Ship> ship;

  /**
   * creates an instance of Board.
   *
   * @param random the randomizer for the board.
   */
  public Board(Random random) {
    randomizer = random;
    ship = new ArrayList<>();
  }

  /**
   * Generates the board, placing ships in random locations.
   */
  public void generateBoard(int h, int w, List<Ship> ships) {
    board = new char[h][w];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        board[i][j] = '0';
      }
    }
    ship = ships;
    for (Ship current : ship) {
      Coord startLocation = null;
      Orientation direction = null;
      int shipSize;
      while (direction == null) {
        shipSize = current.getShipType().getShipSize();
        startLocation = getRandomValidCoord(board.length, board[0].length);
        direction = getValidOrientation(startLocation, shipSize);
      }
      placeShips(direction, startLocation, current);
    }
  }

  /**
   * Generates a Random Starting Coordinate for Ship Placement.
   *
   * @return The Random Coordinate
   */
  private Coord getRandomValidCoord(int sizeX, int sizeY) {
    int currentX = randomizer.nextInt(0, sizeX);
    int currentY = randomizer.nextInt(0, sizeY);
    while (!validPlacement(currentX, currentY)) {
      currentX = randomizer.nextInt(0, sizeX);
      currentY = randomizer.nextInt(0, sizeY);
    }
    return new Coord(currentX, currentY);
  }

  /**
   * Checks to see if the ship placement is valid.
   *
   * @param x the x coordinate of the placement
   * @param y the y coordinate of the placement
   * @return true if the placement is valid, false otherwise
   */
  private boolean validPlacement(int x, int y) {
    try {
      return board[x][y] == '0';
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
  }

  /**
   * Gets the direction the ship will be placed in.
   *
   * @param start the starting coordinates
   * @param size the size of the ship
   * @return the proper orientation, null if there is none.
   */
  private Orientation getValidOrientation(Coord start, int size) {
    Orientation direction = null;
    boolean isHorizontal = randomizer.nextBoolean();
    if (isHorizontal) {
      for (int i = 0; i < size; i++) {
        if (validPlacement(start.x() + i, start.y())) {
          direction = Orientation.HORIZONTAL;
        } else {
          direction = null;
          break;
        }
      }
    } else {
      for (int i = 0; i < size; i++) {
        if (validPlacement(start.x(), start.y() + i)) {
          direction = Orientation.VERTICAL;
        } else {
          direction = null;
          break;
        }
      }
    }
    return direction;
  }

  /**
   * places the ships on the board.
   *
   * @param direction the direction the ship is in
   * @param startLocation the starting location of the ship
   * @param ship what kind of ship the boat is
   */
  private void placeShips(Orientation direction, Coord startLocation, Ship ship) {
    List<Coord> allCoordinates = new ArrayList<>();
    Coord current;
    ship.setStartCoord(startLocation);
    ship.setOrientation(direction);
    for (int i = 0; i < ship.getShipType().getShipSize(); i++) {
      if (direction == Orientation.HORIZONTAL) {
        current = new Coord(startLocation.x() + i, startLocation.y());
        board[startLocation.x() + i][startLocation.y()] = ship.getShipType().getSymbol();
        allCoordinates.add(current);
      } else {
        current = new Coord(startLocation.x(), startLocation.y() + i);
        board[startLocation.x()][startLocation.y() + i] = ship.getShipType().getSymbol();
        allCoordinates.add(current);
      }
    }
    ship.placeShip(allCoordinates);
  }

  /**
   * Determines the possible shots that can be made on this board.
   *
   * @return a list of possible shots
   */
  public List<Coord> possibleShots() {
    List<Coord> possibleShots = new ArrayList<>();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        char current = board[i][j];
        if (current != 'M' && current != 'H') {
          possibleShots.add(new Coord(i, j));
        }
      }
    }
    return possibleShots;
  }

  /**
   * Updates the board using the given info.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param symbol the symbol that will be placed
   */
  public void updateBoard(int x, int y, char symbol) {
    board[x][y] = symbol;
  }

  /**
   * Gets a specific value on the board.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @return what is at the specific x, y coordinate
   */
  public char getPositionOnBoard(int x, int y) {
    return board[x][y];
  }

  /**
   * Gets the 2d array containing the board.
   *
   * @return the board
   */
  public char[][] getBoard() {
    return board;
  }

  /**
   * Gets how many ships remain.
   *
   * @return the number of ships remaining
   */
  public int getRemainingShips() {
    int count = 0;
    for (Ship s : ship) {
      if (s.isNotSunk()) {
        count++;
      }
    }
    return count;
  }

  /**
   * updates the ships using a given attack.
   *
   * @param attack the coordinate being attacked
   */
  public void updateShips(Coord attack) {
    for (Ship s : ship) {
      s.updateShip(attack);
    }
  }
}

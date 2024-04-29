package cs3500.pa04.model;

import java.util.List;

/**
 * The Model for the BattleSalvo game.
 */
public class BattleSalvoModel {
  private final Board aiGameBoard;
  private final Board userGameBoard;
  private final Player playerUser;
  private final Player playerAi;


  /**
   * makes an instance of BattleSalvoModel.
   *
   * @param user the user player
   * @param ai the AI player
   * @param gameai the shotboard for the AI
   * @param gameuser the shotboard for the AI
   */
  public BattleSalvoModel(Player user, Player ai, Board gameai, Board gameuser) {
    aiGameBoard = gameai;
    userGameBoard = gameuser;
    playerUser = user;
    playerAi = ai;
  }

  /**
   * Runs a round of the gameplay loop, where each player attacks eachother.
   */
  public void attack() {
    List<Coord> playerShots = playerUser.takeShots();
    List<Coord> aiShots = playerAi.takeShots();
    List<Coord> damageToPlayer = playerUser.reportDamage(aiShots);
    List<Coord> damageToAi = playerAi.reportDamage(playerShots);
    playerUser.successfulHits(damageToAi);
    playerAi.successfulHits(damageToPlayer);
  }

  /**
   * Determines if the game is over.
   *
   * @return true if the game is over, false otherwise.
   */
  public boolean isGameOver() {
    return userGameBoard.getRemainingShips() == 0
        || aiGameBoard.getRemainingShips() == 0;
  }

  /**
   * Gets the results of the game.
   *
   * @return the result of the game
   */
  public GameResult getGameResults() {
    if (aiGameBoard.getRemainingShips()
        < userGameBoard.getRemainingShips()) {
      return GameResult.WIN;
    }
    if (aiGameBoard.getRemainingShips()
        > userGameBoard.getRemainingShips()) {
      return GameResult.LOSE;
    }
    return GameResult.DRAW;
  }
}

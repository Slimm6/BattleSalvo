package cs3500.pa04;

import cs3500.pa04.controller.Controller;
import cs3500.pa04.controller.GameController;
import cs3500.pa04.controller.ProxyController;
import cs3500.pa04.model.AiPlayer;
import cs3500.pa04.model.Board;
import cs3500.pa04.model.Player;
import cs3500.pa04.view.BattleSalvoTerminalView;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;

/**
 * The driver of the program
 */
public class Driver {

  /**
   * The entry point of the program
   *
   * @param args the arguments of the program
   */
  public static void main(String[] args) {
    if (args.length == 2) {
      String host = args[0];
      int port = Integer.parseInt(args[1]);
      runClient(host, port);
    } else {
      runLocal();
    }
  }

  /**
   * Runs the program on the server.
   *
   * @param host the host for the server
   * @param port the port for the server
   */
  private static void runClient(String host, int port) {
    try {
      Socket connection = new Socket(host, port);
      Board game = new Board(new Random());
      Board shot = new Board(new Random());
      Player player = new AiPlayer("Slimm6", shot, game);
      BattleSalvoTerminalView view = new BattleSalvoTerminalView(System.out,
          new InputStreamReader(System.in));
      ProxyController controller = new ProxyController(connection, player, view);
      controller.run();
    } catch (IOException e) {
      System.out.println("The server has closed");
    }
  }

  /**
   * Runs the program locally.
   */
  private static void runLocal() {
    Appendable append = System.out;
    Readable reader = new InputStreamReader(System.in);
    Controller battleSalvo = new GameController(append, reader);
    battleSalvo.run();
  }
}

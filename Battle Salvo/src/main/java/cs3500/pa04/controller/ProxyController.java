package cs3500.pa04.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa04.json.CoordJson;
import cs3500.pa04.json.FleetJson;
import cs3500.pa04.json.GameType;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.ShipJson;
import cs3500.pa04.json.VolleyJson;
import cs3500.pa04.model.Coord;
import cs3500.pa04.model.GameResult;
import cs3500.pa04.model.Player;
import cs3500.pa04.model.Ship;
import cs3500.pa04.model.ShipType;
import cs3500.pa04.view.BattleSalvoTerminalView;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Controller that runs the game against the server.
 */
public class ProxyController implements Controller {
  private final Socket server;
  private final InputStream in;
  private final PrintStream out;
  private final Player player;
  private final BattleSalvoTerminalView view;
  private final ObjectMapper mapper = new ObjectMapper();
  private static final String NAME = "Slimm6";
  private static final GameType GAME_TYPE = GameType.SINGLE;

  /**
   * Creates instance of ProxyController.
   *
   * @param connection The connection to the server
   * @param p The player the server is fighting
   * @param v The view of the terminal
   * @throws IOException if disconnected from server
   */
  public ProxyController(Socket connection, Player p, BattleSalvoTerminalView v)
      throws IOException {
    server = connection;
    player = p;
    view = v;
    in = server.getInputStream();
    out = new PrintStream(server.getOutputStream());
  }

  @Override
  public void run() {
    try {
      JsonParser parser = mapper.getFactory().createParser(this.in);
      while (!server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);
        delegateMessage(message);
      }
    } catch (IOException e) {
      System.out.println("disconnected from server");
    }
  }

  /**
   * Determines what the server wants to controller to do
   *
   * @param message The input of the server.
   */
  private void delegateMessage(MessageJson message) {
    String name = message.messageName();
    JsonNode arguments = message.arguments();
    if ("join".equals(name)) {
      handleJoin();
    } else if ("setup".equals(name)) {
      handleSetup(arguments);
    } else if ("take-shots".equals(name)) {
      handleShots();
    } else if ("report-damage".equals(name)) {
      handleDamage(arguments);
    } else if ("successful-hits".equals(name)) {
      handleHits(arguments);
    } else if ("end-game".equals(name)) {
      handleGameEnd(arguments);
    } else {
      throw new IllegalStateException("Invalid message name");
    }
  }

  /**
   * Handles the join functionality of the server
   */
  private void handleJoin() {
    MessageJson response = new MessageJson("join",
        mapper.createObjectNode().put("name", NAME)
            .put("game-type", String.valueOf(GAME_TYPE)));
    JsonNode output = serializeRecord(response);
    view.greetPlayer();
    out.println(output);
  }

  /**
   * Handles the setup functionality of the server
   *
   * @param arguments the input from the server
   */
  private void handleSetup(JsonNode arguments) {
    int height = arguments.get("width").asInt();
    int weight = arguments.get("height").asInt();
    JsonNode fleet = arguments.get("fleet-spec");
    Map<ShipType, Integer> specs = new HashMap<>();
    for (ShipType shipType : ShipType.values()) {
      specs.put(shipType, fleet.get(shipType.name()).asInt(0));
    }
    List<Ship> shipList = player.setup(height, weight, specs);
    List<ShipJson> shipJsonList = new ArrayList<>();
    for (Ship ship : shipList) {
      shipJsonList.add(ship.toJson());
    }
    MessageJson response = new MessageJson("setup",
        serializeRecord(new FleetJson(shipJsonList)));
    JsonNode output = serializeRecord(response);
    out.println(output);
  }

  /**
   * Handles the takeShots functionality of the server
   */
  private void handleShots() {
    List<Coord> shots = player.takeShots();
    List<CoordJson> jsonShots = new ArrayList<>();
    for (Coord c : shots) {
      jsonShots.add(c.toJson());
    }
    MessageJson response = new MessageJson("take-shots",
        serializeRecord(new VolleyJson(jsonShots)));
    JsonNode output = serializeRecord(response);
    out.println(output);
  }

  /**
   * Handles the report damage functionality of the server
   *
   * @param arguments the input from the server
   */
  private void handleDamage(JsonNode arguments) {
    VolleyJson shotsFired = mapper.convertValue(arguments, VolleyJson.class);
    List<Coord> volley = shotsFired.generateVolley();
    List<Coord> successful = player.reportDamage(volley);
    List<CoordJson> successfulHitJson = new ArrayList<>();
    for (Coord c : successful) {
      successfulHitJson.add(c.toJson());
    }
    MessageJson response = new MessageJson("report-damage",
        serializeRecord(new VolleyJson(successfulHitJson)));
    JsonNode output = serializeRecord(response);
    out.println(output);
  }

  /**
   * Handles the successful hits functionality of the server
   *
   * @param arguments the input from the server
   */
  private void handleHits(JsonNode arguments) {
    VolleyJson shotsFired = mapper.convertValue(arguments, VolleyJson.class);
    List<Coord> volley = shotsFired.generateVolley();
    player.successfulHits(volley);
    MessageJson response = new MessageJson("successful-hits",
        mapper.createObjectNode());
    JsonNode output = serializeRecord(response);
    out.println(output);
  }

  /**
   * Handles the end game functionality of the server
   *
   * @param arguments the input from the server
   */
  private void handleGameEnd(JsonNode arguments) {
    try {
      GameResult result = GameResult.valueOf(arguments.get("result").asText());
      String reason = arguments.get("reason").asText();
      view.outputResults(result);
      player.endGame(result, reason);
      MessageJson response = new MessageJson("end-game",
          mapper.createObjectNode());
      JsonNode output = serializeRecord(response);
      out.println(output);
      server.close();
    } catch (IOException e) {
      System.out.println("There was an error ending the game");
    }
  }

  /**
   * Converts a given record object to a JsonNode.
   *
   * @param record the record to convert
   * @return the JsonNode representation of the given record
   * @throws IllegalArgumentException if the record could not be converted correctly
   */
  private JsonNode serializeRecord(Record record) throws IllegalArgumentException {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.convertValue(record, JsonNode.class);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Given record cannot be serialized");
    }
  }
}

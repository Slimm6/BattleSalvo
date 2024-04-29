package cs3500.pa04.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa04.Mocket;
import cs3500.pa04.json.CoordJson;
import cs3500.pa04.json.FleetJson;
import cs3500.pa04.json.GameType;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.ShipJson;
import cs3500.pa04.json.VolleyJson;
import cs3500.pa04.model.AiPlayer;
import cs3500.pa04.model.Board;
import cs3500.pa04.model.Coord;
import cs3500.pa04.model.GameResult;
import cs3500.pa04.model.Orientation;
import cs3500.pa04.model.Player;
import cs3500.pa04.view.BattleSalvoTerminalView;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the ProxyController Class
 */
class ProxyControllerTest {
  private Controller proxy;
  private Mocket connection;
  private final ObjectMapper mapper = new ObjectMapper();


  /**
   * Sets up tests for ProxyController.
   */
  @BeforeEach
  void setUp() {
    try {
      connection = new Mocket();
      Board game = new Board(new Random(0));
      Board shot = new Board(new Random(0));
      Player player = new AiPlayer("Slimm6", 0, shot, game);
      BattleSalvoTerminalView view = new BattleSalvoTerminalView(System.out,
          new InputStreamReader(System.in));
      proxy = new ProxyController(connection, player, view);
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Tests the join functionality of the ProxyController.
   */
  @Test
  void joinTest() {
    MessageJson response = new MessageJson("join",
        mapper.createObjectNode().put("name", "Slimm6")
            .put("game-type", String.valueOf(GameType.SINGLE)));
    JsonNode testJoin = serializeRecord(response);
    connection.input(serializeRecord(new MessageJson("join",
        mapper.createObjectNode())));
    proxy.run();
    assertEquals(testJoin, connection.recieved().get(0));
  }


  /**
   * Tests the setup functionality of the ProxyController.
   */
  @Test
  void setupTest() {
    List<ShipJson> input = new ArrayList<>();
    input.add(new ShipJson(new Coord(7, 5).toJson(), 3, Orientation.VERTICAL));
    input.add(new ShipJson(new Coord(1, 1).toJson(), 4, Orientation.HORIZONTAL));
    input.add(new ShipJson(new Coord(3, 2).toJson(), 5, Orientation.HORIZONTAL));
    input.add(new ShipJson(new Coord(4, 4).toJson(), 6, Orientation.VERTICAL));
    FleetJson testFleet = new FleetJson(input);
    JsonNode testSetup = serializeRecord(new MessageJson("setup",
        serializeRecord(testFleet)));
    prepareBoard();
    proxy.run();
    assertEquals(testSetup, connection.recieved().get(0));
  }

  /**
   * Tests the take shots functionality of the ProxyController.
   */
  @Test
  void takeShotsTest() {
    prepareBoard();
    List<CoordJson> shot = new ArrayList<>();
    shot.add(new CoordJson(2, 4));
    shot.add(new CoordJson(5, 4));
    shot.add(new CoordJson(9, 4));
    shot.add(new CoordJson(8, 0));
    JsonNode testShots = serializeRecord(new MessageJson("take-shots",
        serializeRecord(new VolleyJson(shot))));
    connection.input(serializeRecord(new MessageJson("take-shots",
        mapper.createObjectNode())));
    proxy.run();
    assertEquals(testShots, connection.recieved().get(1));
  }

  /**
   * Tests the report damage functionality of the ProxyController.
   */
  @Test
  void damageTest() {
    prepareBoard();
    List<CoordJson> shot = new ArrayList<>();
    shot.add(new CoordJson(2, 4));
    shot.add(new CoordJson(5, 4));
    shot.add(new CoordJson(9, 4));
    shot.add(new CoordJson(8, 0));
    JsonNode testDamage = serializeRecord(new MessageJson("report-damage",
        serializeRecord(new VolleyJson(new ArrayList<>()))));
    connection.input(serializeRecord(new MessageJson("report-damage",
        serializeRecord(new VolleyJson(shot)))));
    proxy.run();
    assertEquals(testDamage, connection.recieved().get(1));
  }

  /**
   * Tests the successful hits functionality of the ProxyController.
   */
  @Test
  void hitTest() {
    prepareBoard();
    List<CoordJson> shot = new ArrayList<>();
    shot.add(new CoordJson(2, 4));
    shot.add(new CoordJson(5, 4));
    JsonNode testHits = serializeRecord(new MessageJson("successful-hits",
        mapper.createObjectNode()));
    connection.input(serializeRecord(new MessageJson("successful-hits",
        serializeRecord(new VolleyJson(shot)))));
    proxy.run();
    assertEquals(testHits, connection.recieved().get(1));
  }

  /**
   * Tests the end game functionality of the ProxyController.
   */
  @Test
  void endGameTest() {
    JsonNode gameEnd = serializeRecord(new MessageJson("end-game",
        mapper.createObjectNode()
            .put("result", String.valueOf(GameResult.WIN))
            .put("reason", "you lost!")));
    connection.input(gameEnd);
    JsonNode testEnd = serializeRecord(new MessageJson("end-game",
        mapper.createObjectNode()));
    proxy.run();
    assertEquals(testEnd, connection.recieved().get(0));
  }

  /**
   * Tests what occurs when the server is given an incorrect message.
   */
  @Test
  void failedTest() {
    JsonNode gameEnd = serializeRecord(new MessageJson("failed",
        mapper.createObjectNode()));
    connection.input(gameEnd);
    assertThrows(IllegalStateException.class, () -> proxy.run());
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

  /**
   * Prepares the board using setup.
   */
  private void prepareBoard() {
    JsonNode setup = serializeRecord(new MessageJson("setup",
        mapper.createObjectNode()
            .put("height", 10)
            .put("width", 10)
            .set("fleet-spec", mapper.createObjectNode()
                .put("CARRIER", 1)
                .put("BATTLESHIP", 1)
                .put("DESTROYER", 1)
                .put("SUBMARINE", 1))));
    connection.input(setup);
  }
}
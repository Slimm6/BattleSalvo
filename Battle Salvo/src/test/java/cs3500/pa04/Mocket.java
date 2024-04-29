package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * A mock socket. Only deals with Jsons.
 */
public class Mocket extends Socket {
  private static final ObjectMapper MAPPER = new ObjectMapper();
  private final MockInput in;
  private final MockOutput out;

  /**
   * Makes a new mock socket.
   */
  public Mocket() {
    in = new MockInput();
    this.out = new MockOutput();
  }

  /**
   * Adds the given json to the input stream.
   *
   * @param json json to add
   */
  public void input(JsonNode json) {
    in.input(json.toString());
  }

  @Override
  public InputStream getInputStream() {
    return in;
  }

  @Override
  public OutputStream getOutputStream() {
    return out;
  }

  /**
   * Gets a list of all messages the mock server has received as jsons.
   *
   * @return list of json messages
   */
  public List<JsonNode> recieved() {
    String[] received = out.toString().split(System.lineSeparator());
    List<JsonNode> parsed = new ArrayList<>();
    for (String entry : received) {
      try {
        parsed.add(MAPPER.readValue(entry, JsonNode.class));
      } catch (JsonProcessingException e) {
        fail(String.format("failed parsing the received input '%s'.", entry));
      }
    }
    return parsed;
  }
}
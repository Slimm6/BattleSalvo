package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cs3500.pa04.model.Orientation;

/**
 * Represents a Ship in Json
 *
 * @param coord The coordinate of the ship
 * @param length The length of the ship
 * @param orientation The orientation of the ship
 */
@JsonPropertyOrder({ "coord", "length", "direction" })
public record ShipJson(
    @JsonProperty("coord") CoordJson coord,
    @JsonProperty("length") int length,
    @JsonProperty("direction") Orientation orientation) {


}

package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents a Fleet of ShipJson.
 *
 * @param fleet The ships in the fleet.
 */
public record FleetJson(
    @JsonProperty ("fleet") List<ShipJson> fleet) {

}

package at.fhv.sem4.gatewayservice.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BattleShipDTO {
    private int posX;
    private int posY;
    private int playerId;
    private int gameFieldId;
}


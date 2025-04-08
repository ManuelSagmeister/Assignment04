package at.fhv.sem4.battleship.Controller;

import at.fhv.sem4.battleship.Entity.BattleShip;
import at.fhv.sem4.battleship.Service.BattleShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BattleShipController {
    private final BattleShipService battleShipService;

    @Autowired
    public BattleShipController(BattleShipService battleShipService) {
        this.battleShipService = battleShipService;
    }

    @PostMapping("/createBattleShip")
    public void createBattleShip(@RequestBody BattleShip battleShip) {
        battleShipService.createBattleShip(battleShip.getPosX(), battleShip.getPosY(), battleShip.getPlayerId(), battleShip.getGameFieldId());
    }

    @GetMapping("/getBattleShipsByGameFieldId/{gameFieldId}")
    public List<BattleShip> getBattleShip(@PathVariable int gameFieldId){
        return battleShipService.getBattleShipsByGameFieldId(gameFieldId);
    }

    @PostMapping("/guess")
    public ResponseEntity<String> guess(@RequestBody BattleShip battleShip) {
        return battleShipService.guess(battleShip.getPosX(), battleShip.getPosY(), battleShip.getGameFieldId(), battleShip.getPlayerId());
    }
}

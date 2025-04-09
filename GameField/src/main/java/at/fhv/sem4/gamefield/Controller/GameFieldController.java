package at.fhv.sem4.gamefield.Controller;

import at.fhv.sem4.gamefield.Service.GameFieldService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

import java.util.List;

@RestController
public class GameFieldController {
    private final GameFieldService gameFieldService;

    @Autowired
    public GameFieldController(GameFieldService gameFieldService) {
        this.gameFieldService = gameFieldService;
    }

    @PostMapping("/createGameField")
    public void createGameField(@RequestParam int fieldX, @RequestParam int fieldY, @RequestParam int p1Id, @RequestParam int p2Id) {
        gameFieldService.createGameField(fieldX, fieldY, p1Id, p2Id);
    }

    @GetMapping("/displayGameField")
    public String displayGameField(@RequestParam int gameFieldId) {
        return gameFieldService.displayGameField(gameFieldId);
    }

    @GetMapping("/getBattleShipsByGameFieldId")
    public List<Object> getBattleShips(@RequestParam int gameFieldId){
        return gameFieldService.getBattleShipFromMicroservice(gameFieldId);
    }

    @PostMapping("/postBattleShip")
    public void postBattleShip(@RequestParam int posX, @RequestParam int posY, @RequestParam int playerId , @RequestParam int gameFieldId) {
        gameFieldService.createBattleShip(posX, posY, playerId, gameFieldId);
    }

    @PostMapping("/postPlayer")
    public void postPlayer(@RequestParam String name){
        gameFieldService.postPlayer(name);
    }

    @PostMapping("/guess")
    public String postGuess(@RequestParam int posX, @RequestParam int posY, @RequestParam int gameFieldId , @RequestParam int playerId){
        return gameFieldService.guess(posX, posY, gameFieldId, playerId);
    }


    @ExceptionHandler(CallNotPermittedException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleCircuitBreaker() {
        return "Circuit breaker is open";
    }
}
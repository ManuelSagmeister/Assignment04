package at.fhv.sem4.gamefield.Service;

import at.fhv.sem4.gamefield.Entity.BattleShipDTO;
import at.fhv.sem4.gamefield.Entity.PlayerDTO;
import jakarta.transaction.Transactional;
import at.fhv.sem4.gamefield.Entity.GameField;
import at.fhv.sem4.gamefield.Repository.GameFieldRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class GameFieldService {
    private final GameFieldRepository gameFieldRepository;
    private final RestTemplate restTemplate;
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public GameFieldService(GameFieldRepository gameFieldRepository, RestTemplate restTemplate, Resilience4JCircuitBreakerFactory circuitBreakerFactory) {
        this.gameFieldRepository = gameFieldRepository;
        this.restTemplate = restTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void postPlayer(String name) {
        rabbitTemplate.convertAndSend("player.exchange", "player.created", name);
    }

    public List<Object> getBattleShipFromMicroservice(int gameFieldId) {
        String battleShipServiceUrl = "http://Battleship/getBattleShipsByGameFieldId/" + gameFieldId;
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("battleShipService");

        return circuitBreaker.run(() -> restTemplate.getForObject(battleShipServiceUrl, List.class),
                throwable -> Collections.emptyList());
    }

    public BattleShipDTO createBattleShip(int posX, int posY, int playerId, int gameFieldId) {
        String battleShipServiceUrl = "http://Battleship/createBattleShip";
        BattleShipDTO battleShipDTO = new BattleShipDTO(posX, posY, playerId, gameFieldId);
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("battleShipService");

        return circuitBreaker.run(() -> restTemplate.postForObject(battleShipServiceUrl, battleShipDTO, BattleShipDTO.class),
                throwable -> new BattleShipDTO(-1, -1, -1, -1));
    }

    @Transactional
    public void createGameField(int fieldX, int fieldY, int p1Id, int p2Id) {
        GameField gameField = new GameField();
        gameField.setFieldX(fieldX);
        gameField.setFieldY(fieldY);
        gameField.setPlayer1Id(p1Id);
        gameField.setPlayer2Id(p2Id);
        gameFieldRepository.save(gameField);
    }

    public String displayGameField(int gameFieldId) {
        Optional<GameField> optionalGameField = gameFieldRepository.findById(gameFieldId);
        if (optionalGameField.isEmpty()) {
            return "Spielfeld nicht gefunden!";
        }

        GameField gameField = optionalGameField.get();
        int fieldX = gameField.getFieldX();
        int fieldY = gameField.getFieldY();
        char[][] display = new char[fieldX][fieldY];

        for (int i = 0; i < fieldX; i++) {
            for (int j = 0; j < fieldY; j++) {
                display[i][j] = '~';
            }
        }

        List<Object> battleShips = getBattleShipFromMicroservice(gameFieldId);
        for (Object obj : battleShips) {
            LinkedHashMap<String, Object> battleShip = (LinkedHashMap<String, Object>) obj;
            int posX = (int) battleShip.get("posX");
            int posY = (int) battleShip.get("posY");
            int playerId = (int) battleShip.get("playerId");

            if (posX >= 0 && posX < fieldX && posY >= 0 && posY < fieldY) {
                display[posX - 1][posY - 1] = (playerId == 1) ? '1' : '2';
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fieldX; i++) {
            for (int j = 0; j < fieldY; j++) {
                sb.append(display[i][j]).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public String guess(int posX, int posY, int gameFieldId, int playerId) {
        String battleShipServiceUrl = "http://Battleship/guess";
        BattleShipDTO battleShipDTO = new BattleShipDTO(posX, posY, playerId, gameFieldId);
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("battleShipService");

        return circuitBreaker.run(() -> restTemplate.postForObject(battleShipServiceUrl, battleShipDTO, String.class),
                throwable -> "Error: Could not process guess, please try again later.");
    }
}
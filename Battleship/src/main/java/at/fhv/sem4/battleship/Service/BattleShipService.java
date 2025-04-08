package at.fhv.sem4.battleship.Service;

import at.fhv.sem4.battleship.Entity.BattleShip;
import at.fhv.sem4.battleship.Repository.BattleShipRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class BattleShipService{
    private final BattleShipRepository battleShipRepository;

    public BattleShipService(BattleShipRepository battleShipRepository){
        this.battleShipRepository = battleShipRepository;
    }

    @Transactional
    public void createBattleShip(int posX, int posY, int playerId, int gameFieldId){

        BattleShip battleShip = new BattleShip();
        battleShip.setPosX(posX);
        battleShip.setPosY(posY);
        battleShip.setGameFieldId(gameFieldId);
        battleShip.setPlayerId(playerId);

        battleShipRepository.save(battleShip);
    }

    public List<BattleShip> getBattleShipsByGameFieldId(int gameFieldId) {
        return battleShipRepository.findByGameFieldId(gameFieldId);
    }


    @Transactional
    public ResponseEntity guess(int posX, int posY, int gameFieldId, int playerId) {

        Optional<BattleShip> optionalBattleShip = battleShipRepository.findByPosXAndPosYAndGameFieldId(posX, posY, gameFieldId);

        if (optionalBattleShip.isPresent()) {
            BattleShip battleShip = optionalBattleShip.get();

            if (battleShip.getPlayerId() == playerId) {
                return ResponseEntity.ok(Map.of("result", "Du kannst dein eigenes Schiff nicht angreifen"));
            }

            battleShipRepository.delete(battleShip);
            return ResponseEntity.ok(Map.of("result", "Treffer"));
        } else {
            return ResponseEntity.ok(Map.of("result", "Daneben"));
        }
    }
}


package at.fhv.sem4.battleship.Repository;

import at.fhv.sem4.battleship.Entity.BattleShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BattleShipRepository extends JpaRepository<BattleShip, Integer> {
    Optional<BattleShip> findByPosXAndPosYAndGameFieldId(int posX, int posY, int gameFieldId);
    List<BattleShip> findByGameFieldId(int gameFieldId);
}

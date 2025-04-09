package at.fhv.sem4.gamefield.Repository;

import at.fhv.sem4.gamefield.Entity.GameField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameFieldRepository extends JpaRepository<GameField, Integer> {
}

package at.fhv.sem4.player.Service;

import at.fhv.sem4.player.Entity.Player;
import at.fhv.sem4.player.Repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService{
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }

    public void createPlayer(String name){
        Player p1 = new Player(0, name);
        playerRepository.save(p1);
    }

    public Player getPlayerInfo(int id) {
        Optional<Player> playerOptional = playerRepository.findById(id);
        if (playerOptional.isPresent()) {
            return playerOptional.get();
        } else {
            throw new RuntimeException("Player not found");
        }
    }
}


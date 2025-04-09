package at.fhv.sem4.player.Controller;

import at.fhv.sem4.player.Entity.Player;
import at.fhv.sem4.player.Repository.PlayerRepository;
import at.fhv.sem4.player.Service.PlayerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PlayerController {
    private final PlayerService playerService;
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerController(PlayerService playerService, PlayerRepository playerRepository) {
        this.playerService = playerService;
        this.playerRepository = playerRepository;
    }

//    @PostMapping("/createPlayer")
//    public void createPlayer(@RequestBody Player player) {
//        playerService.createPlayer(player.getName());
//    }

    @RabbitListener(queues = "player.queue")
    public void createPlayer(String name){
        //System.out.println(player.getName());

        playerService.createPlayer(name);
        System.out.println("Player created: " + name);
    }

    @PostMapping("/createPlayer2")
    public void createPlayer2(@RequestParam String name) {
        playerService.createPlayer(name);
    }

    @GetMapping("/getPlayerInfo/{playerId}")
    public Player getPlayerInfo(@PathVariable int playerId) {
        Player player = playerService.getPlayerInfo(playerId);
        if (player != null){
            return player;
        } else {
            throw new RuntimeException("Player not found");
        }
    }
}

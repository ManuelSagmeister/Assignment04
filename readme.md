# Setup

### Docker

docker run -d --name battlepostgres  -p 5432:5432 -e POSTGRES_USER=root -e POSTGRES_PASSWORD=root postgres
### DB Setup

CREATE DATABASE gamefield_db;
CREATE DATABASE player_db;
CREATE DATABASE board_db;

### !Use the "RunAll" config to start all microservices!

## Port-Mapping:

#### Mapping:
- Port 8080: Player
- Port 8081: GameField
- Port 8082: BattleShip

## Swagger UI for the game
http://localhost:8081/swagger-ui/index.html

## Gameplay
1) Create 2 players
2) Create Gamefield, assign players
3) Create Battleships and assign player and Gamefield
4) guess Battleship position
5) Display game state
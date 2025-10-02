# # TicTacToe REST API

A simple **TicTacToe game** implemented with **Spring Boot**.  
This project provides REST endpoints to:
- Create a new game
- Make moves
- Get the game state
- Delete a game

It also includes a minimal `index.html` frontend to play the game directly in a browser.

## Requirements
- Java 17+ (or compatible JDK installed)

## Build and Run

### Option 1 : Using the JAR file 
1. Copy the file from `target/2025-DEV1-010-TicTacToe-0.0.1-SNAPSHOT.jar`
2. In the directory where the file is located, run:
```bash
java -jar 2025-DEV1-010-TicTacToe-0.0.1-SNAPSHOT.jar
```

### Option 2 : Using Maven
#### Extra Requirements
- Maven
- Spring Boot 3.x
- Git

Clone the repository :

```bash
git clone https://github.com/beatrizmariamendezhernandez-lab/2025-DEV1-010-TicTacToe.git
```

Move inside to the project.
Build and run the project :

```bash
mvn clean install
mvn spring-boot:run
```
The application will start at:
👉 http://localhost:8080

## REST API Endpoints
### Create a new game

`POST /tictactoe`

Response
```bash
{
  "id": "uuid",
  "board": [[null, null, null], [null, null, null], [null, null, null]],
  "currentPlayer": "X",
  "status": "ONGOING"
}
```

### Make a move

`POST /tictactoe/{id}/moves`

Request body:
```bash
{
  "row": 0,
  "col": 1,
  "player": "X"
}
```

Response:
```bash
{
  "id": "uuid",
  "board": [[null, "X", null], [null, null, null], [null, null, null]],
  "currentPlayer": "O",
  "status": "ONGOING"
}
```

### Get game state

`GET /tictactoe/{id}`

Response:
```bash
{
  "id": "uuid",
  "board": [["O", "X", null], [null, "O", null], ["X", "X", "O"]],
  "currentPlayer": "O",
  "status": "O_WINS"
}
```

### Delete a game

`DELETE /tictactoe/{id}`

### Example curl Commands

- Create a new game:
```bash 
curl -X POST http://localhost:8080/tictactoe
```

- Make a move:
```bash
curl -X POST http://localhost:8080/tictactoe/d8cd9b98-e8fd-4457-9bb2-6b5cef29ef11/moves \
     -H "Content-Type: application/json" \
     -d '{"row":0,"col":1,"player":"X"}'
```

- Get game state:
```bash
curl http://localhost:8080/tictactoe/{id}
```

- Delete a game:
```bash
curl -X DELETE http://localhost:8080/tictactoe/{id}
```

### Run all tests with Maven:
```bash 
mvn test
```

Tests cover:
- Board → game rules 
- InMemoryGameRepository → storage logic
- GameService → game logic, moves, win/draw detection
- GameManager → concurrency and locks
- GameRestController → REST endpoints

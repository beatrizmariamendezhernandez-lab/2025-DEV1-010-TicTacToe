package com.game.__DEV1_010_TicTacToe.controller;

import com.game.__DEV1_010_TicTacToe.controller.dto.GameDto;
import com.game.__DEV1_010_TicTacToe.controller.dto.MoveRequestDto;
import com.game.__DEV1_010_TicTacToe.manager.GameManager;
import com.game.__DEV1_010_TicTacToe.model.Game;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tictactoe")
public class GameRestController
{
    private final GameManager gameManager;

    public GameRestController(GameManager gameManager)
    {
        this.gameManager = gameManager;
    }

    @PostMapping
    public GameDto createGame()
    {
        Game game = gameManager.createGame();
        return GameDto.fromGame(game);
    }

    @PostMapping("/{id}/moves")
    public GameDto move(@PathVariable String id, @RequestBody MoveRequestDto move)
    {
        Game updatedGame = gameManager.makeMove(
                id,
                move.player(),
                move.row(),
                move.col()
        );
        return GameDto.fromGame(updatedGame);
    }

    @GetMapping("/{id}")
    public GameDto getGameState(@PathVariable String id)
    {
        Game g = gameManager.getGame(id);
        return GameDto.fromGame(g);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable String id)
    {
        gameManager.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}

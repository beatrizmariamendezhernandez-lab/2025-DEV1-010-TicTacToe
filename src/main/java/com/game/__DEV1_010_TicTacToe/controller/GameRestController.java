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

    /**
     * POST /tictactoe : Creates a new Tic-Tac-Toe game.
     *
     * @return DTO with initial board, game id, game status, current player.
     *
     */
    @PostMapping
    public GameDto createGame()
    {
        Game game = gameManager.createGame();
        return GameDto.fromGame(game);
    }

    /**
     * POST /tictactoe/{gameId}/move : Makes a move in an existing game.
     *
     * @param id Game ID.
     * @param move DTO containing the player and coordinates (row, col).
     *
     * @return The updated state of the game after the move.
     */
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

    /**
     * GET /tictactoe/{gameId} : Retrieves the state of an existing game.
     *
     * @param id Game ID.
     *
     * @return The current state of the game.
     */
    @GetMapping("/{id}")
    public GameDto getGameState(@PathVariable String id)
    {
        Game g = gameManager.getGame(id);
        return GameDto.fromGame(g);
    }

    /**
     * DELETE /tictactoe/{gameId} : Deletes a game.
     *
     * @param id Game ID.
     * @return HTTP 204 No Content on successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable String id)
    {
        gameManager.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}

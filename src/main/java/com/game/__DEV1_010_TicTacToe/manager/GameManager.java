package com.game.__DEV1_010_TicTacToe.manager;

import com.game.__DEV1_010_TicTacToe.exception.GameNotFoundException;
import com.game.__DEV1_010_TicTacToe.model.Game;
import com.game.__DEV1_010_TicTacToe.model.Player;
import com.game.__DEV1_010_TicTacToe.repository.GameRepository;
import com.game.__DEV1_010_TicTacToe.service.GameService;
import org.springframework.stereotype.Service;


@Service
public class GameManager
{
    private final GameService gameService;
    private final GameRepository gameRepository;

    public GameManager(GameService gameService, GameRepository gameRepository)
    {
        this.gameService = gameService;
        this.gameRepository = gameRepository;
    }

    public Game createGame()
    {
        Game newGame = gameService.createNewGame(Player.X);
        gameRepository.save(newGame);
        return newGame;
    }

    public Game getGame(String gameId)
    {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game with ID '" + gameId + "' not found."));
    }

    public Game makeMove(String gameId, Player player, int row, int col) throws GameNotFoundException
    {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game with ID '" + gameId + "' not found."));
        gameService.makeMove(game, row, col, player);
        // Persist the new state
        return gameRepository.save(game);

    }

    public void deleteGame(String gameId) throws GameNotFoundException
    {
            gameRepository.delete(gameId);
    }
}

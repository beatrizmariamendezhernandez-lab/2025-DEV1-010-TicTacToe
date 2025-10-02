package com.game.__DEV1_010_TicTacToe.manager;

import com.game.__DEV1_010_TicTacToe.exception.GameNotFoundException;
import com.game.__DEV1_010_TicTacToe.model.Game;
import com.game.__DEV1_010_TicTacToe.model.Player;
import com.game.__DEV1_010_TicTacToe.repository.GameRepository;
import com.game.__DEV1_010_TicTacToe.service.GameService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;


@Service
public class GameManager
{
    private final GameService gameService;
    private final GameRepository gameRepository;

    private final ConcurrentHashMap<String, ReentrantReadWriteLock> gameLocks = new ConcurrentHashMap<>();

    public GameManager(GameService gameService, GameRepository gameRepository)
    {
        this.gameService = gameService;
        this.gameRepository = gameRepository;
    }

    public Game createGame()
    {
        Game newGame = gameService.createNewGame(Player.X);
        gameRepository.save(newGame);
        gameLocks.computeIfAbsent(newGame.getId(), k -> new ReentrantReadWriteLock());
        return newGame;
    }

    public Game getGame(String gameId)
    {
        return readGameLock(gameId, game -> game);
    }

    public Game makeMove(String gameId, Player player, int row, int col) throws GameNotFoundException
    {
        return writeGameLock(gameId, game ->
        {
            gameService.makeMove(game, row, col, player);
            // Persist the new state
            return gameRepository.save(game);
        });

    }

    public void deleteGame(String gameId) throws GameNotFoundException
    {
        writeGameLock(gameId, game ->
        {
            gameRepository.delete(gameId);
            // Important: Clean up the lock from the map to prevent memory leaks.
            gameLocks.remove(gameId);
            return null;
        });
    }

    private <R> R writeGameLock(String gameId, Function<Game, R> action)
    {
        ReentrantReadWriteLock lock = gameLocks.computeIfAbsent(gameId, k -> new ReentrantReadWriteLock());
        lock.writeLock().lock();
        try
        {
            Game game = gameRepository.findById(gameId)
                    .orElseThrow(() -> new GameNotFoundException("Game with ID '" + gameId + "' not found."));
            return action.apply(game);
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }

    private <R> R readGameLock(String gameId, Function<Game, R> action)
    {
        // Atomically get or create a lock for the given gameId.
        ReentrantReadWriteLock lock = gameLocks.computeIfAbsent(gameId, k -> new ReentrantReadWriteLock());
        lock.readLock().lock();
        try
        {
            Game game = gameRepository.findById(gameId)
                    .orElseThrow(() -> new GameNotFoundException("Game with ID '" + gameId + "' not found."));
            return action.apply(game);
        }
        finally
        {
            lock.readLock().unlock();
        }
    }
}

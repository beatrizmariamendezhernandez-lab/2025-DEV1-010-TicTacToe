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

    // A map to hold a specific lock for each game instance.
    // This provides granular locking, allowing concurrent operations on different games.
    private final ConcurrentHashMap<String, ReentrantReadWriteLock> gameLocks = new ConcurrentHashMap<>();

    public GameManager(GameService gameService, GameRepository gameRepository)
    {
        this.gameService = gameService;
        this.gameRepository = gameRepository;
    }

    /**
     * Creates a new game. This operation does not require a lock as the game
     * does not exist yet.
     *
     * @return The newly created Game.
     */
    public Game createGame()
    {
        Game newGame = gameService.createNewGame(Player.X);
        gameRepository.save(newGame);
        gameLocks.computeIfAbsent(newGame.getId(), k -> new ReentrantReadWriteLock());
        return newGame;
    }

    /**
     * Retrieves the current state of a game.
     * Uses a lock to ensure a consistent read.
     *
     * @param gameId Game ID.
     *
     * @return The current Game state.
     */
    public Game getGame(String gameId)
    {
        return readGameLock(gameId, game -> game);
    }

    /**
     * Executes a move in a specific game. This is a critical section that must be
     * locked to prevent race conditions where two players try to move simultaneously.
     *
     * @param gameId Game ID.
     * @param player The player making the move.
     * @param row The row of the move.
     * @param col The column of the move.
     *
     * @return The updated Game state after the move.
     *
     * @throws GameNotFoundException if the game does not exist.
     */
    public Game makeMove(String gameId, Player player, int row, int col) throws GameNotFoundException
    {
        return writeGameLock(gameId, game ->
        {
            gameService.makeMove(game, row, col, player);
            // Persist the new state
            return gameRepository.save(game);
        });

    }

    /**
     * Deletes a game and its associated lock.
     *
     * @param gameId Game ID.
     *
     * @throws GameNotFoundException if the game does not exist.
     */
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

    /**
     * A utility method to safely perform an action on a game with proper write locking.
     *
     * @param gameId The ID of the game to lock.
     * @param action The function to execute on the locked game.
     * @param <R> The return type of the action.
     *
     * @return The result of the action.
     */
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

    /**
     * A utility method to safely perform an action on a game with proper read locking.
     *
     * @param gameId The ID of the game to lock.
     * @param action The function to execute on the locked game.
     * @param <R> The return type of the action.
     *
     * @return The result of the action.
     */
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

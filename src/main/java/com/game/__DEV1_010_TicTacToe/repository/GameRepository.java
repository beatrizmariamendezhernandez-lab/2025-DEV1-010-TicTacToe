package com.game.__DEV1_010_TicTacToe.repository;

import com.game.__DEV1_010_TicTacToe.model.Game;

import java.util.Optional;

public interface GameRepository
{
    /**
     * Finds a game by its unique identifier.
     *
     * @param id the unique ID of the game
     *
     * @return an Optional containing the Game if found, or an empty Optional if not found
     */
    Optional<Game> findById (String id);

    /**
     * Delete a game by id
     *
     * @param id unique game ID
     */
    void delete (String id);

    /**
     * Save or update a game.
     *
     * @param game Game to save or update.
     *
     * @return The saved game.
     */
    Game save (Game game);
}

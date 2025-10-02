package com.game.__DEV1_010_TicTacToe.repository;

import com.game.__DEV1_010_TicTacToe.model.Game;
import com.game.__DEV1_010_TicTacToe.model.Player;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryGameRepositoryTest
{
    @Test
    void shouldSaveAndFindGame()
    {
        GameRepository repo = new InMemoryGameRepository();
        Game game = new Game(Player.X);
        repo.save(game);

        Game found = repo.findById(game.getId()).get();
        assertNotNull(found);
        assertEquals(game, found);
    }

    @Test
    void shouldDeleteGame()
    {
        GameRepository repo = new InMemoryGameRepository();
        Game game = new Game(Player.X);

        repo.save(game);
        repo.delete(game.getId());

        assertEquals(Optional.empty(), repo.findById(game.getId()));
    }
}

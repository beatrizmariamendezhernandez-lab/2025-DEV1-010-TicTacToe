package com.game.__DEV1_010_TicTacToe.manager;

import com.game.__DEV1_010_TicTacToe.exception.GameNotFoundException;
import com.game.__DEV1_010_TicTacToe.model.Game;
import com.game.__DEV1_010_TicTacToe.model.Player;
import com.game.__DEV1_010_TicTacToe.repository.GameRepository;
import com.game.__DEV1_010_TicTacToe.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GameManagerTest
{
    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameManager gameManager;

    @Test
    void createGameSavesAndReturnsGame()
    {
        Game g = new Game(Player.X);
        when(gameService.createNewGame(Player.X)).thenReturn(g);
        when(gameRepository.save(any(Game.class))).thenReturn(g);

        Game created = gameManager.createGame();

        assertNotNull(created);
        assertEquals(Player.X, created.getCurrentPlayer());
        verify(gameRepository).save(g);
    }

    @Test
    void getNonExistingGameThrows()
    {
        when(gameRepository.findById("id123")).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class,
                () -> gameManager.getGame("id123"));
    }
}

package com.game.__DEV1_010_TicTacToe.service;

import com.game.__DEV1_010_TicTacToe.exception.InvalidMoveException;
import com.game.__DEV1_010_TicTacToe.model.Game;
import com.game.__DEV1_010_TicTacToe.model.GameStatus;
import com.game.__DEV1_010_TicTacToe.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameServiceTest
{
    private final GameService service = new GameService();

    @Test
    void newGameStartsWithX()
    {
        Game g = service.createNewGame(Player.X);
        assertEquals(Player.X, g.getCurrentPlayer());
        assertEquals(GameStatus.ONGOING, g.getStatus());
    }

    @Test
    void makeMoveSwitchesPlayer()
    {
        Game game = service.createNewGame(Player.X);
        service.makeMove(game, 0, 0, Player.X);
        assertEquals(Player.O, game.getCurrentPlayer());
    }

    @Test
    void invalidMoveThrowsException()
    {
        Game game = service.createNewGame(Player.X);
        assertThrows(InvalidMoveException.class, () -> service.makeMove(game, 3, 3, Player.X));
    }

    @Test
    void winConditionIsDetected()
    {
        Game game = service.createNewGame(Player.X);
        service.makeMove(game, 0, 0, Player.X);
        service.makeMove(game, 1, 0, Player.O);
        service.makeMove(game, 0, 1, Player.X);
        service.makeMove(game, 1, 1, Player.O);
        service.makeMove(game, 0, 2, Player.X);
        assertEquals(GameStatus.X_WINS, game.getStatus());
    }

    @Test
    void drawConditionIsDetected()
    {
        Game g = service.createNewGame(Player.X);
        // Fill the board with no winner
        service.makeMove(g,0,0, Player.X);
        service.makeMove(g,0,1, Player.O);
        service.makeMove(g,0,2, Player.X);
        service.makeMove(g,1,1, Player.O);
        service.makeMove(g,1,0, Player.X);
        service.makeMove(g,1,2, Player.O);
        service.makeMove(g,2,1, Player.X);
        service.makeMove(g,2,0, Player.O);
        service.makeMove(g,2,2, Player.X);
        assertEquals(GameStatus.DRAW, g.getStatus());
    }

    @Test
    void placingOnOccupiedCellThrows()
    {
        Game game = service.createNewGame(Player.X);
        service.makeMove(game, 0, 0, Player.X);
        assertThrows(InvalidMoveException.class, () -> service.makeMove(game, 0, 0, Player.O));
    }

    @Test
    void wrongPlayerMoveThrows() {
        Game game = service.createNewGame(Player.X);
        assertThrows(InvalidMoveException.class, () -> service.makeMove(game, 0, 0, Player.O));
    }

    @Test
    void noMovesAfterWin() {
        Game game = service.createNewGame(Player.X);
        service.makeMove(game, 0,0, Player.X);
        service.makeMove(game, 1,0, Player.O);
        service.makeMove(game, 0,1, Player.X);
        service.makeMove(game, 1,1, Player.O);
        service.makeMove(game, 0,2, Player.X); // X wins

        assertThrows(IllegalStateException.class, () -> service.makeMove(game, 2,2, Player.O));
    }

}

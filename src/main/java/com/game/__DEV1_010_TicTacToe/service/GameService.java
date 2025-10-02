package com.game.__DEV1_010_TicTacToe.service;

import com.game.__DEV1_010_TicTacToe.constant.WinningLines;
import com.game.__DEV1_010_TicTacToe.exception.InvalidMoveException;
import com.game.__DEV1_010_TicTacToe.model.Game;
import com.game.__DEV1_010_TicTacToe.model.GameStatus;
import com.game.__DEV1_010_TicTacToe.model.Player;
import org.springframework.stereotype.Service;

@Service
public class GameService
{
    /**
     * Create a new game.
     *
     * @param startingPlayer Player to use during game creation.
     *
     * @return The created game.
     */
    public Game createNewGame(Player startingPlayer)
    {
        return new Game(startingPlayer);
    }

    /**
     * Updates the board with the player's symbol at the specified row and column,
     * validates the move, and updates the game state (e.g., next player, win, or draw).
     *
     * @param game   The current game instance.
     * @param row    The row index (0–2) where the move is made.
     * @param col    The column index (0–2) where the move is made.
     * @param player The player making the move (X or O).
     *
     * @throws InvalidMoveException If the move is invalid (e.g., cell already occupied).
     * @throws IllegalStateException If the game finished.
     */
    public void makeMove(Game game, int row, int col, Player player)
    {
        if (game.getStatus() != GameStatus.ONGOING)
        {
            throw new IllegalStateException("Game is already finished");
        }

        if (row < 0 || row > 2 || col < 0 || col > 2)
        {
            throw new InvalidMoveException("Invalid coordinates");
        }

        if (game.getCurrentPlayer() != player)
        {
            throw new InvalidMoveException("It is not your turn, should play " + game.getCurrentPlayer());
        }

        if (!game.getBoard().isCellEmpty(row, col))
        {
            throw new InvalidMoveException("Cell occupied");
        }

        game.getBoard().placeMark(row, col, player);
        updateStatus(game);

        if (game.getStatus() == GameStatus.ONGOING)
        {
            game.setCurrentPlayer(game.getCurrentPlayer().next());
        }
    }

    /**
     * Check the board after a move and update the game state if needed.
     *
     * @param game The current game instance.
     */
    private void updateStatus(Game game)
    {
        for (int[][] line : WinningLines.LINES)
        {
            Player a = game.getBoard().get(line[0][0], line[0][1]);
            Player b = game.getBoard().get(line[1][0], line[1][1]);
            Player c = game.getBoard().get(line[2][0], line[2][1]);
            if (a != null && a == b && b == c)
            {
                game.setStatus(a == Player.X ? GameStatus.X_WINS : GameStatus.O_WINS);
                return;
            }
        }
        if (game.getBoard().isFull())
        {
            game.setStatus(GameStatus.DRAW);
        }
    }
}

package com.game.__DEV1_010_TicTacToe.controller.dto;

import com.game.__DEV1_010_TicTacToe.model.Game;
import com.game.__DEV1_010_TicTacToe.model.GameStatus;
import com.game.__DEV1_010_TicTacToe.model.Player;

public record GameDto(String id, Player[][] board, Player currentPlayer, GameStatus status)
{
    public static GameDto fromGame(Game game)
    {
        return new GameDto(game.getId(), game.getBoard().snapshot(), game.getCurrentPlayer(), game.getStatus());
    }
}

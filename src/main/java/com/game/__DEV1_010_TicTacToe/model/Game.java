package com.game.__DEV1_010_TicTacToe.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class Game
{
    private String id;
    private final Board board = new Board();
    @Setter
    private Player currentPlayer;
    @Setter
    private GameStatus status;

    public Game(Player firstPlayer)
    {
        this.id = UUID.randomUUID().toString();
        this.currentPlayer = firstPlayer;
        this.status = GameStatus.ONGOING;
    }
}

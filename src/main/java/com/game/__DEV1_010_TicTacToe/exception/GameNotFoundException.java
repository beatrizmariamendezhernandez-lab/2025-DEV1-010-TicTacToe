package com.game.__DEV1_010_TicTacToe.exception;

public class GameNotFoundException extends RuntimeException
{
    public GameNotFoundException(String id)
    {
        super("Game with id " + id + " not found");
    }
}
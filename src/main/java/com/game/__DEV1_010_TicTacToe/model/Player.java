package com.game.__DEV1_010_TicTacToe.model;

public enum Player
{
    X, O;

    public Player next()
    {
        return this == X ? O : X;
    }
}
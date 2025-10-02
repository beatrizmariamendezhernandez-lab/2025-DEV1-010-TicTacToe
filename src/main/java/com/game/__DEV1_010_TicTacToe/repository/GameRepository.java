package com.game.__DEV1_010_TicTacToe.repository;

import com.game.__DEV1_010_TicTacToe.model.Game;

import java.util.Optional;

public interface GameRepository
{
    Optional<Game> findById (String id);

    void delete (String id);

    Game save (Game game);
}

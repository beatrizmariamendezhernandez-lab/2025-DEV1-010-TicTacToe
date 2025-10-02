package com.game.__DEV1_010_TicTacToe.repository;

import com.game.__DEV1_010_TicTacToe.model.Game;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryGameRepository implements GameRepository
{
    private final Map<String, Game> games = new ConcurrentHashMap<>();

    @Override
    public Optional<Game> findById(String id)
    {
        return Optional.ofNullable(games.get(id));
    }

    @Override
    public void delete(String id)
    {
        games.remove(id);
    }

    @Override
    public Game save(Game game)
    {
        games.put(game.getId(), game);
        return game;
    }
}

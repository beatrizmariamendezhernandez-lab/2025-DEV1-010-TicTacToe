package com.game.__DEV1_010_TicTacToe.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.__DEV1_010_TicTacToe.controller.dto.MoveRequestDto;
import com.game.__DEV1_010_TicTacToe.model.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class GameRestControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createGameAndPlayMove() throws Exception {
        // Create game
        String gameJson = mockMvc.perform(post("/tictactoe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        String gameId = objectMapper.readTree(gameJson).get("id").asText();

        // Play move
        MoveRequestDto move = new MoveRequestDto(0,0, Player.X);
        mockMvc.perform(post("/tictactoe/" + gameId + "/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.board[0][0]", notNullValue()));
    }

    @Test
    void getNonExistingGameReturns404() throws Exception {
        mockMvc.perform(get("/tictactoe/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteGameRemovesIt() throws Exception {
        String gameJson = mockMvc.perform(post("/tictactoe"))
                .andReturn().getResponse().getContentAsString();
        String gameId = objectMapper.readTree(gameJson).get("id").asText();

        mockMvc.perform(delete("/tictactoe/" + gameId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/tictactoe/" + gameId))
                .andExpect(status().isNotFound());
    }
    @Test
    void concurrentMovesOneFails() throws Exception {
        String gameJson = mockMvc.perform(post("/tictactoe"))
                .andReturn().getResponse().getContentAsString();
        String gameId = objectMapper.readTree(gameJson).get("id").asText();

        MoveRequestDto move1 = new MoveRequestDto(0,0, Player.X);
        MoveRequestDto move2 = new MoveRequestDto(0,0, Player.X);

        Runnable task1 = () -> {
            try {
                mockMvc.perform(post("/tictactoe/" + gameId + "/moves")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(move1)))
                        .andReturn();
            } catch (Exception ignored) {}
        };
        Runnable task2 = () -> {
            try {
                mockMvc.perform(post("/tictactoe/" + gameId + "/moves")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(move2)))
                        .andReturn();
            } catch (Exception ignored) {}
        };

        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // only one of them should have succeeded
        mockMvc.perform(get("/tictactoe/" + gameId))
                .andExpect(jsonPath("$.board[0][0]", is("X")));
    }
}

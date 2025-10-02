package com.game.__DEV1_010_TicTacToe.controller.dto;

import com.game.__DEV1_010_TicTacToe.model.Player;

public record MoveRequestDto(int row, int col, Player player) { }

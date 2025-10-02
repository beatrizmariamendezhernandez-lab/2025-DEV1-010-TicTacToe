package com.game.__DEV1_010_TicTacToe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(InvalidMoveException.class)
    public ResponseEntity<?> handleInvalidMove(InvalidMoveException ex)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "error", "Invalid Move",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<?> handleNotFound(GameNotFoundException ex)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "error", "Game Not Found",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleStatusException(ResponseStatusException ex)
    {
        return ResponseEntity.status(ex.getStatusCode()).body(
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "error", ex.getStatusCode().toString(),
                        "message", ex.getReason() + " " + ex.getMessage()
                )
        );
    }
}

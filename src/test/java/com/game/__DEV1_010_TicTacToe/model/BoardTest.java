package com.game.__DEV1_010_TicTacToe.model;

import com.game.__DEV1_010_TicTacToe.exception.InvalidMoveException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest
{
    @Test
    void newBoardShouldBeEmpty()
    {
        Board board = new Board();
        for (int r = 0; r < 3; r++)
        {
            for (int c = 0; c < 3; c++)
            {
                assertTrue(board.isCellEmpty(r, c));
                assertNull(board.get(r, c));
            }
        }
        assertFalse(board.isFull());
    }

    @Test
    void shouldSetAndGetCell()
    {
        Board board = new Board();
        board.set(0, 0, Player.X);
        assertEquals(Player.X, board.get(0, 0));
        assertFalse(board.isCellEmpty(0, 0));
    }

    @Test
    void shouldDetectFullBoard()
    {
        Board board = new Board();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board.set(r, c, Player.O);
            }
        }
        assertTrue(board.isFull());
    }

    @Test
    void shouldReturnSnapshotCopy()
    {
        Board board = new Board();
        board.set(1, 1, Player.X);

        Player[][] snapshot = board.snapshot();

        assertEquals(Player.X, snapshot[1][1]);
        snapshot[1][1] = Player.O; // modify copy
        assertEquals(Player.X, board.get(1, 1)); // original unchanged
    }

    @Test
    void shouldPlaceMarkInEmptyCell()
    {
        Board board = new Board();
        board.placeMark(2, 2, Player.O);
        assertEquals(Player.O, board.get(2, 2));
    }

    @Test
    void shouldNotPlaceMarkInOccupiedCell()
    {
        Board board = new Board();
        board.placeMark(0, 0, Player.X);

        assertThrows(InvalidMoveException.class, () ->
                board.placeMark(0, 0, Player.O)
        );
    }
}

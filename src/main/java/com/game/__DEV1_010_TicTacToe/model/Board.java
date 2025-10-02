package com.game.__DEV1_010_TicTacToe.model;

import com.game.__DEV1_010_TicTacToe.exception.InvalidMoveException;

public class Board
{
    private final Player[][] cells = new Player[3][3];

    /**
     * Get the symbol in the position given by row and column.
     *
     * @param row Board row to check.
     * @param col Board column to check.
     *
     * @return The symbol in the position.
     */
    public Player get(int row, int col)
    {
        return cells[row][col];
    }

    /**
     * Set a symbol in the position indicated by row and column.
     *
     * @param row Board row to check.
     * @param col Board column to check.
     * @param player Symbol to write in the position.
     */
    public void set(int row, int col, Player player)
    {
        cells[row][col] = player;
    }

    /**
     * Check if the board is full.
     *
     * @return True if the board is full and false otherwise.
     */
    public boolean isFull()
    {
        for (Player[] row : cells)
        {
            for (Player player : row)
            {
                if (player == null)
                    return false;
            }
        }
        return true;
    }

    /**
     * Copy the Board in a 2D array to return the board in the API.
     *
     * @return A copy of the board but using a 2D array.
     */
    public Player[][] snapshot()
    {
        Player[][] copy = new Player[3][3];
        for (int r=0; r<3; r++)
        {
            System.arraycopy(cells[r], 0, copy[r], 0, 3);
        }
        return copy;
    }

    /**
     * Check if a cell is empty.
     *
     * @param row Board row to check.
     * @param col Board column to check.
     *
     * @return True if the cell is null or false otherwise.
     */
    public boolean isCellEmpty(int row, int col)
    {
        return cells[row][col] == null;
    }

    /**
     *
     * @param row Board row to mark.
     * @param col Board column to mark.
     * @param player Symbol to add.
     *
     * @throws InvalidMoveException If the position is occupied.
     */
    public void placeMark(int row, int col, Player player)
    {
        if (cells[row][col] != null)
        {
            throw new InvalidMoveException("Cell occupied");
        }
        cells[row][col] = player;
    }
}

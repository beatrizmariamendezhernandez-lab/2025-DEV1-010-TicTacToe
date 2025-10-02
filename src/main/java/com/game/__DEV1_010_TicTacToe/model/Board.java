package com.game.__DEV1_010_TicTacToe.model;

public class Board
{
    private final Player[][] cells = new Player[3][3];

    public Player get(int r, int c)
    {
        return cells[r][c];
    }

    public void set(int r, int c, Player p)
    {
        cells[r][c] = p;
    }

    public boolean isFull() {
        for (Player[] row : cells)
        {
            for (Player p : row)
            {
                if (p == null) return false;
            }
        }
        return true;
    }

    public Player[][] snapshot()
    {
        Player[][] copy = new Player[3][3];
        for (int r=0; r<3; r++)
        {
            System.arraycopy(cells[r], 0, copy[r], 0, 3);
        }
        return copy;
    }

    public boolean isCellEmpty(int r, int c)
    {
        return cells[r][c] == null;
    }

    public void placeMark(int r, int c, Player p)
    {
        if (cells[r][c] != null)
        {
            throw new IllegalStateException("Cell occupied");
        }
        cells[r][c] = p;
    }
}

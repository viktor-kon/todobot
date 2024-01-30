package ru.ifmo.tictactoe.bot.command;

import ru.ifmo.tictactoe.TicTacToe;

public class FillCommand implements Command {
    private int row;
    private int col;

    public FillCommand(final int row, final int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public void execute(final TicTacToe ticTacToe) {
        ticTacToe.fill(row, col);
    }
}

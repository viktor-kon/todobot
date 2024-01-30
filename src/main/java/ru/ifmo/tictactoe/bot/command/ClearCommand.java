package ru.ifmo.tictactoe.bot.command;

import ru.ifmo.tictactoe.TicTacToe;

public class ClearCommand implements Command {

    @Override
    public void execute(final TicTacToe ticTacToe) {
        ticTacToe.clear();
    }
}

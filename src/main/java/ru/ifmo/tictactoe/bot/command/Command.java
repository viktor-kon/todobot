package ru.ifmo.tictactoe.bot.command;

import ru.ifmo.tictactoe.TicTacToe;

public interface Command {
    void execute(TicTacToe ticTacToe);
}

package ru.ifmo.tictactoe;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TicTacToe {

    private final String X = "X";
    private final String O = "O";

    private String[][] table;
    private String lastValue;

    public TicTacToe() {
        clear();
    }

    public String fill(int x, int y) {
        if (!table[x][y].equals(" ")) {
            return table[x][y];
        }
        table[x][y] = (lastValue = lastValue.equals(X) ? O : X);
        return lastValue;
    }

    public void clear() {
        table = new String[][]{
                {" ", " ", " "},
                {" ", " ", " "},
                {" ", " ", " "}
        };
        lastValue = O;
    }

    @Override
    public String toString() {
        return Arrays.stream(table)
                .map(row -> String.join("|", row))
                .collect(Collectors.joining("\n-+-+-\n", "```\n", "```"));

    }
}

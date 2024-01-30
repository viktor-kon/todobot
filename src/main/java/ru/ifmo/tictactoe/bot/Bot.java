package ru.ifmo.tictactoe.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ifmo.tictactoe.TicTacToe;
import ru.ifmo.tictactoe.bot.command.ClearCommand;
import ru.ifmo.tictactoe.bot.command.Command;
import ru.ifmo.tictactoe.bot.command.FillCommand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class Bot extends TelegramLongPollingBot {
    private final ConcurrentMap<Long, TicTacToe> games;
    private final String name;
    private final String token;

    Map<String, Command> commands = new HashMap<>() {
        {
            put("lt", new FillCommand(0, 0));
            put("ct", new FillCommand(0, 1));
            put("rt", new FillCommand(0, 2));
            put("lc", new FillCommand(1, 0));
            put("cc", new FillCommand(1, 1));
            put("rc", new FillCommand(1, 2));
            put("lb", new FillCommand(2, 0));
            put("cb", new FillCommand(2, 1));
            put("rb", new FillCommand(2, 2));

            put("ng", new ClearCommand());
        }
    };

    public Bot(final String botName, final String botToken) {
        super();
        this.name = botName;
        this.token = botToken;
        games = new ConcurrentHashMap<>();
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(final Update update) {
        final Message message = update.getMessage();
        final Long chatId = message.getChatId();

        final TicTacToe game = games.computeIfAbsent(chatId, id -> new TicTacToe());

        commands.getOrDefault(
                message.getText(),
                ttt -> {}
        ).execute(game);

        SendMessage answer = new SendMessage();
        answer.enableMarkdown(true);
        answer.setText("**Fill cells:**\n\n" +
                "```\n" +
                "|lt|ct|rt|\n" +
                "|lc|cc|rc|\n" +
                "|lb|cb|rb|\n" +
                "```\n\n" +
                "**New game: _ng_**\n" +
                "**Current state:**\n" + game.toString()
        );
        answer.setChatId(chatId.toString());

        answer.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(collectRow("lt", "ct", "rt"))
                .keyboardRow(collectRow("lc", "cc", "rc"))
                .keyboardRow(collectRow("lb", "cb", "rb"))
                .keyboardRow(collectRow("ng"))
                .build());

        try {
            this.execute(answer);
            //for (Long cId : games.keySet()) {
            //    answer.setChatId(cId.toString());
            //    this.execute(answer);
            //}
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    private KeyboardRow collectRow(final String... tags) {
        return Arrays.stream(tags).map(text -> new KeyboardButton(text))
                .collect(KeyboardRow::new,
                        (row, button) -> row.add(button),
                        (r1, r2) -> {
                            throw new UnsupportedOperationException();
                        }
                );
    }


}

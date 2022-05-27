package com.company.logistbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppLifecycleEventListener {

    @EventListener
    public void onApplicationStarted(ApplicationStartedEvent event) {
        TelegramBot bot = new TelegramBot("5279221054:AAEGmgPBoa06fNOoyrwCcuUaEi8iLE4WKfc");

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update == null) continue;
                processUpdate(update, bot);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void processUpdate(Update update, TelegramBot bot) {
        if (update.message() != null && update.message().text() != null) {
            Long chatId = update.message().chat().id();
            if (update.message().text().equals("/start") || update.message().text().equals("\uD83D\uDC48\uD83C\uDFFBНазад")) {
                botStart(bot, chatId);
            }
            else if (update.callbackQuery()!=null) {
                String call_data = update.callbackQuery().data();
                long chat_id = update.callbackQuery().message().chat().id();

            }

            if (update.message().text().equals("Доставшик")) {
                SendMessage askDelevery = null;
                askDelevery = new SendMessage(chatId, "\uD83D\uDC64 Введите Ф.И.О.\n" +
                        "(пример: Иван Иванов Иванович)\n");

                KeyboardButton back =new KeyboardButton("\uD83D\uDC48\uD83C\uDFFBНазад");
                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(back);
                replyKeyboardMarkup.resizeKeyboard(true);
                askDelevery.replyMarkup(replyKeyboardMarkup);

                bot.execute(askDelevery);

                backButton(update, bot, chatId);
            }else if(update.message().text().equals("Слушатель")){
                SendMessage askCall = null;
                askCall = new SendMessage(chatId, "\uD83D\uDC64 Введите Ф.И.О.\n" +
                        "(пример: Иван Иванов Иванович)\n");

                KeyboardButton back =new KeyboardButton("\uD83D\uDC48\uD83C\uDFFBНазад");
                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(back);
                replyKeyboardMarkup.resizeKeyboard(true);
                askCall.replyMarkup(replyKeyboardMarkup);

                bot.execute(askCall);

                backButton(update, bot, chatId);
            }else if(update.message().text().equals("Контакты")){
                SendMessage phone = new SendMessage(chatId,"\uD83C\uDFE2Контакты\n"+"☎Телефон:\n"+"+998 99 477 9737\n");
                KeyboardButton back =new KeyboardButton("\uD83D\uDC48\uD83C\uDFFBНазад");
                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(back);
                replyKeyboardMarkup.resizeKeyboard(true);
                phone.replyMarkup(replyKeyboardMarkup);

                bot.execute(phone);

                backButton(update, bot, chatId);
            }

        }
    }

    private void backButton(Update update, TelegramBot bot, Long chatId) {
        if (update.message().text().equals("Назад")) {
            SendMessage back = new SendMessage(chatId, "\uD83D\uDC48\uD83C\uDFFBНазад");

            botStart(bot, chatId);

            bot.execute(back);
        }
    }

    private void botStart(TelegramBot bot, Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "*\uD83D\uDC4B\uD83C\uDFFBЗдраствуйте*\n");
        sendMessage.parseMode(ParseMode.Markdown);

        KeyboardButton delivery =new KeyboardButton("Доставшик");
        KeyboardButton call =new KeyboardButton("Слушатель");
        KeyboardButton phone =new KeyboardButton("Контакты");

        List<List<KeyboardButton>> list = new ArrayList<>();
        List<KeyboardButton> buttons = new ArrayList<>();
        buttons.add(delivery);
        buttons.add(call);
        List<KeyboardButton> buttons1 = new ArrayList<>();
        buttons1.add(phone);
        list.add(buttons);
        list.add(buttons1);

        KeyboardButton[][] keyboardButtons = new KeyboardButton[list.size()][];
        int i = 0;
        for (List<KeyboardButton> kbs : list) {
            keyboardButtons[i++] = kbs.toArray(new KeyboardButton[kbs.size()]);
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButtons);
        replyKeyboardMarkup.resizeKeyboard(true);
        sendMessage.replyMarkup(replyKeyboardMarkup);

        try {
            bot.execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
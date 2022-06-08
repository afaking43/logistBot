package com.company.logistbot.listener;

import com.company.logistbot.TgUserService;
import com.company.logistbot.entity.Question;
import com.company.logistbot.entity.TgUser;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import io.jmix.core.DataManager;
import io.jmix.core.security.SystemAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppLifecycleEventListener {

    @Autowired
    private DataManager dataManager;
    @Autowired
    private TgUserService tgUserService;
    @Autowired
    private SystemAuthenticator systemAuthenticator;

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
                tgUserService.saveOrGetTgUser(chatId, true);
                start(bot, chatId);
            }else if(update.message().text().equals("/restart") ){
                start(bot, chatId);
            }

            register(update,bot,chatId);

            contact(update,bot,chatId);

            backBtn(update, bot, chatId);
        }
    }

    private void backBtn(Update update, TelegramBot bot, Long chatId) {
        if (update.message().text().equals("Назад")) {
            SendMessage back = new SendMessage(chatId, "\uD83D\uDC48\uD83C\uDFFBНазад");

            bot.execute(back);

            start(bot, chatId);

        }
    }

    private void register(Update update, TelegramBot bot, Long chatId) {
        if (update.message().text().equals("Зарегестрироваться")){
            SendMessage change = new SendMessage(chatId,"Выберите одну из категориий");

            KeyboardButton delivery = new KeyboardButton("Водитель");
            KeyboardButton call = new KeyboardButton("Логист");
            KeyboardButton back = new KeyboardButton("Назад");

            List<List<KeyboardButton>> list = new ArrayList<>();
            List<KeyboardButton> buttons = new ArrayList<>();
            buttons.add(delivery);
            buttons.add(call);
            List<KeyboardButton> buttons1 = new ArrayList<>();
            buttons1.add(back);
            list.add(buttons);
            list.add(buttons1);

            KeyboardButton[][] keyboardButtons = new KeyboardButton[list.size()][];
            int i = 0;
            for (List<KeyboardButton> kbs : list) {
                keyboardButtons[i++] = kbs.toArray(new KeyboardButton[kbs.size()]);
            }

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButtons);
            replyKeyboardMarkup.resizeKeyboard(true);
            change.replyMarkup(replyKeyboardMarkup);

            bot.execute(change);

        }
    }
    private void contact(Update update, TelegramBot bot, Long chatId) {
        if (update.message().text().equals("Контакты")) {
            SendMessage sendInfo = new SendMessage(chatId, "\uD83C\uDFE2Контакты\n" +
                    "☎️Телефон:\n" +
                    "+998 123 45 67\n");
            bot.execute(sendInfo);

        }
    }

    private void start(TelegramBot bot, Long chatId) {
            SendMessage sendMessage = new SendMessage(chatId, "*\uD83D\uDC4B\uD83C\uDFFBЗдраствуйте*\n");
            sendMessage.parseMode(ParseMode.Markdown);
            KeyboardButton register = new KeyboardButton("Зарегестрироваться");
            KeyboardButton phone = new KeyboardButton("Контакты");

                List<List<KeyboardButton>> list = new ArrayList<>();
                List<KeyboardButton> buttons = new ArrayList<>();
                buttons.add(register);
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
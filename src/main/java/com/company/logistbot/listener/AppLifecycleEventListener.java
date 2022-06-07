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
            startFunction(update, bot, chatId);

            askFunction(update, bot, chatId);

            driverMesseng(update, bot, chatId);

            logistMesseng(update, bot, chatId);

            phoneMesseng(update, bot, chatId);
        }
    }



    private void askFunction(Update update, TelegramBot bot, Long chatId) {
        TgUser tgUser = tgUserService.saveOrGetTgUser(chatId, false);
        Question question = tgUser.getCurrentQuestion();
        if (Question.WHAT_NAME.equals(question)) {
            tgUser.setName(update.message().text());
            tgUser.setCurrentQuestion(Question.WHAT_PHONE);

            SendMessage askDelevery = new SendMessage(chatId, "Введите ваш номер телефона \n"+
                    "\uD83D\uDCF1  пример: +998931234567");

            backButtonText(update, bot, chatId, tgUser, askDelevery);
        }
        if (Question.WHAT_PHONE.equals(question)) {
            tgUser.setPhone(update.message().text());
            tgUser.setCurrentQuestion(Question.WHAT_CAR_DESCRIBE);

            SendMessage askDelevery = new SendMessage(chatId, "\uD83D\uDE9AОпишите груз:");

            backButtonText(update, bot, chatId, tgUser, askDelevery);
        }
        if (Question.WHAT_CAR_DESCRIBE.equals(question)) {
            tgUser.setPhone(update.message().text());
            tgUser.setCurrentQuestion(Question.WHAT_START_LOCATION);

            SendMessage askDelevery = new SendMessage(chatId, "\uD83D\uDCCDОтправьте локацию старта:");

            locationBtn(update, bot, chatId, tgUser, askDelevery);
        }
    }

    private void locationBtn(Update update, TelegramBot bot, Long chatId, TgUser tgUser, SendMessage askDelevery) {
        KeyboardButton locationButton = new KeyboardButton("Отправить геолокация\uD83D\uDCCD");
        KeyboardButton backButton = new KeyboardButton("\uD83D\uDC48\uD83C\uDFFBНазад");
        ReplyKeyboardMarkup locMarkup = new ReplyKeyboardMarkup(locationButton,backButton);
        locMarkup.resizeKeyboard(true);
        askDelevery.replyMarkup(locMarkup);

        bot.execute(askDelevery);
        systemAuthenticator.withSystem(() -> dataManager.save(tgUser));
    }

    private void phoneMesseng(Update update, TelegramBot bot, Long chatId) {
        if (update.message().text().equals("Контакты")) {
            SendMessage sendInfo = new SendMessage(chatId, "\uD83C\uDFE2Контакты\n" +
                    "☎️Телефон:\n" +
                    "+998 123 45 67\n");
            KeyboardButton backButton = new KeyboardButton("\uD83D\uDC48\uD83C\uDFFBНазад");
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(backButton);
            replyKeyboardMarkup.resizeKeyboard(true);
            sendInfo.replyMarkup(replyKeyboardMarkup);

            bot.execute(sendInfo);

            backFunction(update, bot, chatId);
        }
    }

    private void logistMesseng(Update update, TelegramBot bot, Long chatId) {
        if (update.message().text().equals("Логист")) {
            SendMessage askDelevery = new SendMessage(chatId, "\uD83D\uDC64 Введите Ф.И.О. логиста\n" +
                    "(пример: Иван Иванов Иванович)\n");
            KeyboardButton backButton = new KeyboardButton("\uD83D\uDC48\uD83C\uDFFBНазад");
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(backButton);
            replyKeyboardMarkup.resizeKeyboard(true);
            askDelevery.replyMarkup(replyKeyboardMarkup);

            bot.execute(askDelevery);

            backFunction(update, bot, chatId);
        }
    }

    private void driverMesseng(Update update, TelegramBot bot, Long chatId) {
        if (update.message().text().equals("Водитель")) {
            tgUserService.saveQuestion(Question.WHAT_NAME, chatId);
            SendMessage askDelevery = null;
            askDelevery = new SendMessage(chatId, "\uD83D\uDC64 Введите Ф.И.О.водителя\n" +
                    "(пример: Иван Иванов Иванович)\n");

            KeyboardButton backButton = new KeyboardButton("\uD83D\uDC48\uD83C\uDFFBНазад");
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(backButton);
            replyKeyboardMarkup.resizeKeyboard(true);
            askDelevery.replyMarkup(replyKeyboardMarkup);

            bot.execute(askDelevery);

            backFunction(update, bot, chatId);

        }
    }

    private void backButtonText(Update update, TelegramBot bot, Long chatId, TgUser tgUser, SendMessage askDelevery) {
        KeyboardButton backButton = new KeyboardButton("\uD83D\uDC48\uD83C\uDFFBНазад");
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(backButton);
        replyKeyboardMarkup.resizeKeyboard(true);
        askDelevery.replyMarkup(replyKeyboardMarkup);

        bot.execute(askDelevery);

        backFunction(update, bot, chatId);
        systemAuthenticator.withSystem(() -> dataManager.save(tgUser));
    }

    private void backFunction(Update update, TelegramBot bot, Long chatId) {
        if (update.message().text().equals("Назад")) {
            SendMessage back = new SendMessage(chatId, "\uD83D\uDC48\uD83C\uDFFBНазад");

            startFunction(update, bot, chatId);

            bot.execute(back);
        }
    }

    private void startFunction(Update update, TelegramBot bot, Long chatId) {
        if (update.message().text().equals("/start") || update.message().text().equals("/restart") || update.message().text().equals("\uD83D\uDC48\uD83C\uDFFBНазад")) {
            tgUserService.saveOrGetTgUser(chatId, true);
            SendMessage sendMessage = new SendMessage(chatId, "*\uD83D\uDC4B\uD83C\uDFFBЗдраствуйте*\n");
            sendMessage.parseMode(ParseMode.Markdown);

            KeyboardButton delivery = new KeyboardButton("Водитель");
            KeyboardButton call = new KeyboardButton("Логист");
            KeyboardButton phone = new KeyboardButton("Контакты");

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
}
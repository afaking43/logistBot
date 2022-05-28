package com.company.logistbot;

import com.company.logistbot.entity.Question;
import com.company.logistbot.entity.TgUser;
import io.jmix.core.DataManager;
import io.jmix.core.security.SystemAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TgUserService {

    @Autowired
    private DataManager dataManager;
    @Autowired
    private SystemAuthenticator systemAuthenticator;

    public TgUser saveOrGetTgUser(Long chatId) {
        TgUser tgUser = systemAuthenticator.withSystem(() -> dataManager.load(TgUser.class)
                .query("select e from tg_User e where e.chatId=:chatId")
                .parameter("chatId", chatId.toString())
                .optional().orElse(null));
        if (tgUser == null) {
            return systemAuthenticator.withSystem(() -> {
                        TgUser u = dataManager.create(TgUser.class);
                        u.setChatId(chatId.toString());
                        return dataManager.save(u);
                    }
            );
        } else {
            return tgUser;
        }
    }

    public void saveQuestion(Question whatName, Long chatId) {
        systemAuthenticator.withSystem(() -> {
                    TgUser tgUser = saveOrGetTgUser(chatId);
                    tgUser.setCurrentQuestion(whatName);
                    dataManager.save(tgUser);
                    return tgUser;
                }
        );
    }
}

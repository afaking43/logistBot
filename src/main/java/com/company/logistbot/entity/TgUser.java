package com.company.logistbot.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@JmixEntity
@Table(name = "TG_USER")
@Entity(name = "tg_User")
public class TgUser {

    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "USERNAME")
    private String username;

//    @Column(name = "CAR_DESCRIPTION")
//    private String carDescription;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "CURRENT_QUESTION")
    private String currentQuestion;

    @InstanceName
    @Column(name = "NAME")
    private String name;

    @Column(name = "CHAT_ID")
    private String chatId;

//    public String getCarDescription() {
//        return carDescription;
//    }
//
//    public void setCarDescription(String carDescription) {
//        this.carDescription = carDescription;
//    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Question getCurrentQuestion() {
        return currentQuestion == null ? null : Question.fromId(currentQuestion);
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion == null ? null : currentQuestion.getId();
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
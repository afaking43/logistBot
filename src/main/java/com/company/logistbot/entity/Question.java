package com.company.logistbot.entity;

import io.jmix.core.metamodel.datatype.impl.EnumClass;

import javax.annotation.Nullable;


public enum Question implements EnumClass<String> {

    WHAT_NAME("WHAT_NAME"),
    WHAT_PHONE("WHAT_PHONE"),
    WHAT_CAR_DESCRIBE("WHAT_CAR_DESCRIBE"),
    WHAT_START_LOCATION("WHAT_START_LOCATION"),
    WHAT_END_LOCATION("WHAT_END_LOCATION"),
    WHAT_ITEM_DESCRIBE("WHAT_ITEM_DESCRIBE"),
    START("START"),
    ASK_CHEK("ASK_CHEK");

    private String id;

    Question(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static Question fromId(String id) {
        for (Question at : Question.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
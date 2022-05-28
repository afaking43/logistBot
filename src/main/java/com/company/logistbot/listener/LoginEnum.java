package com.company.logistbot.listener;

import io.jmix.core.metamodel.datatype.impl.EnumClass;

import javax.annotation.Nullable;


public enum LoginEnum implements EnumClass<String> {

    USER_NAME("USER_NAME"),
    USER_PHONE("USER_PHONE"),
    USER_CAR_ID("USER_CAR_ID"),
    USER_START_LOCATION("USER_START_LOCATION"),
    USER_END_LOCATION("USER_END_LOCATION"),
    USER_ITEM_DESCRIBE("USER_ITEM_DESCRIBE")
    ;

    private String id;

    LoginEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static LoginEnum fromId(String id) {
        for (LoginEnum at : LoginEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
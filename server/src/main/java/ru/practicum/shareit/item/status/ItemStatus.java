package ru.practicum.shareit.item.status;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ItemStatus {
    TRUE(true), FALSE(false);

    private final boolean status;

    ItemStatus(boolean status) {
        this.status = status;
    }

    @JsonValue
    public boolean value() {
        return this.status;
    }

    @JsonCreator
    public static ItemStatus of(boolean status) {
        return status ? TRUE : FALSE;
    }
}
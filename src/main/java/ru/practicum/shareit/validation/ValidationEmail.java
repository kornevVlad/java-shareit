package ru.practicum.shareit.validation;

public class ValidationEmail extends RuntimeException {

    public ValidationEmail(String message) {
        super(message);
    }
}
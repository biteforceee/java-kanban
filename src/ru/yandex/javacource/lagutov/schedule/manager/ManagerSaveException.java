package ru.yandex.javacource.lagutov.schedule.manager;

import java.io.IOException;


public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final String massage) {
        super(massage);
    }
}

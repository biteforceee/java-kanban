package ru.yandex.javacource.lagutov.schedule.manager;


public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final String massage) {
        super(massage);
    }

    public ManagerSaveException(final String massage, Exception e) {
        super(massage, e);
    }
}

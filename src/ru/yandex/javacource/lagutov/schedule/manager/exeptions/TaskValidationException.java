package ru.yandex.javacource.lagutov.schedule.manager.exeptions;

public class TaskValidationException extends RuntimeException {
    public TaskValidationException(final String massage) {
        super(massage);
    }

    public TaskValidationException(final String massage, Exception e) {
        super(massage, e);
    }
}

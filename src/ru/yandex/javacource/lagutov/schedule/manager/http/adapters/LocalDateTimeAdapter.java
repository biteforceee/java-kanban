package ru.yandex.javacource.lagutov.schedule.manager.http.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
        if (localDate == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(localDate.format(dateFormat));
        }
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
//        if (jsonReader.nextString() != null) {
//            return LocalDateTime.parse(jsonReader.nextString(), dateFormat);
//        }
//        return null;
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull(); // пропускаем null-значение
            return null; // возвращаем null, если значение отсутствует
        }
        return LocalDateTime.parse(jsonReader.nextString(), dateFormat);
    }
}
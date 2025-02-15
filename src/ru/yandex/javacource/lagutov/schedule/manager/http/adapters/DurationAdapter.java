package ru.yandex.javacource.lagutov.schedule.manager.http.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        if (duration == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(duration.toMinutes());
        }
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        //return Duration.ofMinutes(Long.parseLong(jsonReader.nextString()));
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return Duration.ofMinutes(0);
        }
        return Duration.ofMinutes(jsonReader.nextLong());
    }
}
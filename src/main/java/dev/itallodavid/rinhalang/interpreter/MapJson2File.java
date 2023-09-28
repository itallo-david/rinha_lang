package dev.itallodavid.rinhalang.interpreter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.itallodavid.rinhalang.language.kernel.File;

import java.io.Reader;

public class MapJson2File {
    private static final GsonBuilder builder = new GsonBuilder();
    private static final Gson gson = builder.create();

    protected MapJson2File() {

    }

    public static File map(String json) {
        return gson.fromJson(json, File.class);
    }

    public static File map(Reader json) {
        return gson.fromJson(json, File.class);
    }
}

package dev.itallodavid.rinhalang.language.kernel;

import com.google.gson.annotations.JsonAdapter;
import dev.itallodavid.rinhalang.astjson.FileJsonDeserializer;

@JsonAdapter(FileJsonDeserializer.class)
public class File extends Node {
    private final String name;
    private final Term expression;

    public File(Location location, String name, Term expression) {
        super(location);
        this.name = name;
        this.expression = expression;
    }

    public String name() {
        return name;
    }

    public Term expression() {
        return expression;
    }
}

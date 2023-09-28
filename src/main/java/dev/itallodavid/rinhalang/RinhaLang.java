package dev.itallodavid.rinhalang;

import dev.itallodavid.rinhalang.interpreter.Interpreter;
import dev.itallodavid.rinhalang.interpreter.MapJson2File;
import dev.itallodavid.rinhalang.interpreter.RinhaLangInterpreter;
import dev.itallodavid.rinhalang.language.kernel.File;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class RinhaLang {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new java.io.File("/var/rinha/source.rinha.json")));
        String json = bufferedReader.lines().collect(Collectors.joining());
        File file = MapJson2File.map(json);
        Interpreter interpreter = new RinhaLangInterpreter(file);
        interpreter.perform();
    }
}
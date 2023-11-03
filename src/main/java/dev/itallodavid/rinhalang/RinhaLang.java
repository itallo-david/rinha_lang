package dev.itallodavid.rinhalang;

import dev.itallodavid.rinhalang.helpers.RinhaAst;
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
        //String file2 = RinhaLang.class.getClassLoader().getResource("teste.rl").getFile();
        //BufferedReader bufferedReader = new BufferedReader(new FileReader(new java.io.File(file2)));
        //String json = bufferedReader.lines().collect(Collectors.joining());
//        String json = """
//                {"name":"r.rt","expression":{"kind":"Let","name":{"text":"a","location":{"start":4,"end":5,"filename":"r.rt"}},"value":{"kind":"Function","parameters":[],"value":{"kind":"Let","name":{"text":"f","location":{"start":26,"end":27,"filename":"r.rt"}},"value":{"kind":"Int","value":90,"location":{"start":30,"end":32,"filename":"r.rt"}},"next":{"kind":"Function","parameters":[{"text":"u","location":{"start":41,"end":42,"filename":"r.rt"}}],"value":{"kind":"Print","value":{"kind":"Binary","lhs":{"kind":"Var","text":"f","location":{"start":63,"end":64,"filename":"r.rt"}},"op":"Add","rhs":{"kind":"Binary","lhs":{"kind":"Int","value":1,"location":{"start":67,"end":68,"filename":"r.rt"}},"op":"Add","rhs":{"kind":"Var","text":"u","location":{"start":71,"end":72,"filename":"r.rt"}},"location":{"start":67,"end":72,"filename":"r.rt"}},"location":{"start":63,"end":72,"filename":"r.rt"}},"location":{"start":57,"end":73,"filename":"r.rt"}},"location":{"start":38,"end":79,"filename":"r.rt"}},"location":{"start":22,"end":79,"filename":"r.rt"}},"location":{"start":8,"end":81,"filename":"r.rt"}},"next":{"kind":"Print","value":{"kind":"Call","callee":{"kind":"Call","callee":{"kind":"Var","text":"a","location":{"start":90,"end":91,"filename":"r.rt"}},"arguments":[],"location":{"start":90,"end":93,"filename":"r.rt"}},"arguments":[{"kind":"Int","value":10,"location":{"start":94,"end":96,"filename":"r.rt"}}],"location":{"start":90,"end":97,"filename":"r.rt"}},"location":{"start":84,"end":98,"filename":"r.rt"}},"location":{"start":0,"end":98,"filename":"r.rt"}},"location":{"start":0,"end":98,"filename":"r.rt"}}
//
//                """;
        String json = RinhaAst.from("scripts/r.rt");
        File file = MapJson2File.map(json);
        Interpreter interpreter = new RinhaLangInterpreter(file);
        interpreter.perform();
    }
}
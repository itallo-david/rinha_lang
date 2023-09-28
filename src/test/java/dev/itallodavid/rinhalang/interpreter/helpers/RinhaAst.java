package dev.itallodavid.rinhalang.interpreter.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class RinhaAst {
    private static String binPath = RinhaAst .class.getClassLoader().getResource("bin/rinha").getPath();
    private static Runtime runtime = Runtime.getRuntime();

    public static String from(String path2Script) {
        try {
            String script = Objects.requireNonNull(RinhaAst.class.getClassLoader().getResource(path2Script)).getPath();
            Process process = runtime.exec(new String[] { binPath, script });
            BufferedReader brInputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader brOutputError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String resultInputStream = brInputStream.readLine();

            if(Objects.nonNull(resultInputStream)) return resultInputStream;
            else {
                String line;

                while((line = brOutputError.readLine()) != null) {
                    System.out.println(line);
                }
            }

            return null;
        } catch (IOException exception) {
            return null;
        }
    }
}

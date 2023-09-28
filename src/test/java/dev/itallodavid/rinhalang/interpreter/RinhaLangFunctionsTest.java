package dev.itallodavid.rinhalang.interpreter;

import dev.itallodavid.rinhalang.errors.InvalidNumberOfFunctionParameters;
import dev.itallodavid.rinhalang.interpreter.helpers.RinhaAst;
import dev.itallodavid.rinhalang.language.kernel.File;
import dev.itallodavid.rinhalang.language.kernel.Term;
import dev.itallodavid.rinhalang.language.literals.LiteralInteger;
import dev.itallodavid.rinhalang.language.literals.LiteralString;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class RinhaLangFunctionsTest {

    private final PrintStream standardOutput = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOutput);
    }

    @Test
    void test1() {
        String ast = RinhaAst.from("scripts/functions-test/function-no-params.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);
        Term term = interpreter.perform();

        assertTrue(term instanceof LiteralString);
        assertEquals("Hello World!", ((LiteralString) term).value());
    }

    @Test
    void test2() {
        String ast = RinhaAst.from("scripts/functions-test/function-with-params.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);
        Term term = interpreter.perform();

        assertTrue(term instanceof LiteralString);
        assertEquals("Hello Itallo", ((LiteralString) term).value());
    }

    @Test
    void test3() {
        String ast = RinhaAst.from("scripts/functions-test/function-with-params-error.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);
        assertThrows(InvalidNumberOfFunctionParameters.class, interpreter::perform);
    }

    @Test
    void test4() {
        String ast = RinhaAst.from("scripts/functions-test/function-call.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);
        Term term = interpreter.perform();

        assertTrue(term instanceof LiteralString);
        assertEquals("Hello world", ((LiteralString) term).value());
        assertEquals("<#closure>\n", outputStreamCaptor.toString());
    }
}

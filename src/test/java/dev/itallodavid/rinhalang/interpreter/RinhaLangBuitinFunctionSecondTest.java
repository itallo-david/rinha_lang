package dev.itallodavid.rinhalang.interpreter;

import dev.itallodavid.rinhalang.errors.InvalidTupleFunctionArgument;
import dev.itallodavid.rinhalang.interpreter.helpers.RinhaAst;
import dev.itallodavid.rinhalang.language.kernel.File;
import dev.itallodavid.rinhalang.language.kernel.Literal;
import dev.itallodavid.rinhalang.language.kernel.Term;
import dev.itallodavid.rinhalang.language.literals.LiteralString;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
public class RinhaLangBuitinFunctionSecondTest {
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
    void testSecondCorrect() {
        String ast = RinhaAst.from("scripts/second-test/second-correct.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);
        String expectedMessage = "Hello world";

        Term term = interpreter.perform();
        assertEquals(String.format("%s\n", "Hello world"), outputStreamCaptor.toString());
        assertTrue(term instanceof LiteralString);
        assertEquals(expectedMessage, ((Literal<?>) term).value());
    }

    @Test
    void testSecondWrong() {
        String ast = RinhaAst.from("scripts/second-test/second-wrong.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);

        assertThrows(InvalidTupleFunctionArgument.class, interpreter::perform);
    }
}

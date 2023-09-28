package dev.itallodavid.rinhalang.interpreter;

import dev.itallodavid.rinhalang.errors.UninitializedVariable;
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

public class RinhaLangVariablesTest {
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
    void testVariables() {
        String ast = RinhaAst.from("scripts/variable-test/variable-definition.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);
        String expectedOutput = "Hello! 101";
        Term term = interpreter.perform();

        assertTrue(term instanceof LiteralString);
        assertEquals(expectedOutput, ((LiteralString) term).value());
        assertEquals(String.format("%s\n", expectedOutput), outputStreamCaptor.toString());
    }

    @Test
    void test() {
        String ast = RinhaAst.from("scripts/variable-test/variable-shadowing.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);
        Term term = interpreter.perform();

        assertTrue(term instanceof LiteralString);
        assertEquals("2022Hi 2023", ((Literal<?>) term).value());
        assertEquals("2022\n2022Hi 2023\n", outputStreamCaptor.toString());
    }

    @Test
    void test2() {
        String ast = RinhaAst.from("scripts/variable-test/variable-reference.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);
        Term term = interpreter.perform();

        assertTrue(term instanceof LiteralString);
        assertEquals("Hi world", ((Literal<?>) term).value());
        assertEquals("Hi world\n", outputStreamCaptor.toString());
    }

    @Test
    void test3() {
        String ast = RinhaAst.from("scripts/variable-test/variable-error.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);
        assertThrows(UninitializedVariable.class, interpreter::perform);
    }
}

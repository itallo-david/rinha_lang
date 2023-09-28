package dev.itallodavid.rinhalang.interpreter;

import dev.itallodavid.rinhalang.interpreter.helpers.RinhaAst;
import dev.itallodavid.rinhalang.language.expressions.ExpDefFunction;
import dev.itallodavid.rinhalang.language.expressions.ExpTuple;
import dev.itallodavid.rinhalang.language.kernel.File;
import dev.itallodavid.rinhalang.language.kernel.Literal;
import dev.itallodavid.rinhalang.language.kernel.Term;
import dev.itallodavid.rinhalang.language.literals.LiteralBoolean;
import dev.itallodavid.rinhalang.language.literals.LiteralInteger;
import dev.itallodavid.rinhalang.language.literals.LiteralString;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RinhaLangBuiltinFunctionPrintTest {
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
    void testPrintBoolean() {
        String ast = RinhaAst.from("scripts/print-test/print-boolean.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);

        Term term = interpreter.perform();
        assertEquals("true", outputStreamCaptor.toString().trim());
        assertTrue(term instanceof LiteralBoolean);
        assertEquals(true, ((Literal<?>) term).value());
    }

    @Test
    void testPrintString() {
        String ast = RinhaAst.from("scripts/print-test/print-string.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);

        Term term = interpreter.perform();
        String expectedMessage = "Hello world!";

        assertEquals(expectedMessage, outputStreamCaptor.toString().trim());
        assertTrue(term instanceof LiteralString);
        assertEquals(expectedMessage, ((Literal<?>) term).value());
    }

    @Test
    void testPrintInteger() {
        String ast = RinhaAst.from("scripts/print-test/print-integer.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);

        Term term = interpreter.perform();
        BigInteger expectedMessage = new BigInteger("100");

        assertEquals(expectedMessage.toString(), outputStreamCaptor.toString().trim());
        assertTrue(term instanceof LiteralInteger);
        assertEquals(expectedMessage, ((Literal<?>) term).value());
    }

    @Test
    void testPrintClosure() {
        String ast = RinhaAst.from("scripts/print-test/print-closure.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);

        Term term = interpreter.perform();
        String expectedMessage = "<#closure>";

        assertEquals(expectedMessage, outputStreamCaptor.toString().trim());
        assertTrue(term instanceof ExpDefFunction);
    }

    @Test
    void testPrintNumericTuple() {
        String ast = RinhaAst.from("scripts/print-test/print-numeric-tuple.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);

        Term term = interpreter.perform();
        String expectedMessage = "(100, 90)";

        assertEquals(expectedMessage, outputStreamCaptor.toString().trim());
        assertTrue(term instanceof ExpTuple);
    }

    @Test
    void testPrintMixedTuple() {
        String ast = RinhaAst.from("scripts/print-test/print-mixed-tuple.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);

        Term term = interpreter.perform();
        String expectedMessage = "(100, \"Hi everyone\")";

        assertEquals(expectedMessage, outputStreamCaptor.toString().trim());
        assertTrue(term instanceof ExpTuple);
    }

    @Test
    void testPrintOrder1() {
        String ast = RinhaAst.from("scripts/print-test/print-order-1.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);

        Term term = interpreter.perform();
        String expectedMessage = "1\n2\n";

        assertEquals(expectedMessage, outputStreamCaptor.toString());
        assertTrue(term instanceof LiteralInteger);
    }

    @Test
    void testPrintOrder2() {
        String ast = RinhaAst.from("scripts/print-test/print-order-2.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);

        Term term = interpreter.perform();
        String expectedMessage = "1\n2\n3\n";

        assertEquals(expectedMessage, outputStreamCaptor.toString());
        assertTrue(term instanceof LiteralInteger);
    }

    @Test
    void testPrintOrder3() {
        String ast = RinhaAst.from("scripts/print-test/print-order-3.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);

        Term term = interpreter.perform();
        String expectedMessage = "1\n2\n(1, 2)\n";

        assertEquals(expectedMessage, outputStreamCaptor.toString());
        assertTrue(term instanceof ExpTuple);
    }

    @Test
    void testPrintOrder4() {
        String ast = RinhaAst.from("scripts/print-test/print-order-4.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);

        Term term = interpreter.perform();
        String expectedMessage = "1\n2\n3\n";

        assertEquals(expectedMessage, outputStreamCaptor.toString());
        assertTrue(term instanceof LiteralInteger);
    }
}

package dev.itallodavid.rinhalang.interpreter;

import dev.itallodavid.rinhalang.errors.InvalidTupleFunctionArgument;
import dev.itallodavid.rinhalang.interpreter.helpers.RinhaAst;
import dev.itallodavid.rinhalang.language.kernel.File;
import dev.itallodavid.rinhalang.language.kernel.Literal;
import dev.itallodavid.rinhalang.language.kernel.Term;
import dev.itallodavid.rinhalang.language.literals.LiteralBoolean;
import dev.itallodavid.rinhalang.language.literals.LiteralInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class RinhaLangBuiltinFunctionFirstTest {
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
    void testFirstCorrect() {
        String ast = RinhaAst.from("scripts/first-test/first-correct.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);

        Term term = interpreter.perform();
        assertEquals("2\n", outputStreamCaptor.toString());
        assertTrue(term instanceof LiteralInteger);
        assertEquals(new BigInteger("2"), ((Literal<?>) term).value());
    }

    @Test
    void testFirstWrong() {
        String ast = RinhaAst.from("scripts/first-test/first-wrong.rinha");
        File file = MapJson2File.map(ast);
        RinhaLangInterpreter interpreter = new RinhaLangInterpreter(file);

        assertThrows(InvalidTupleFunctionArgument.class, interpreter::perform);
    }
}

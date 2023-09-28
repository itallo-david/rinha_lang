package dev.itallodavid.rinhalang.errors;

public class InvalidNumberOfFunctionParameters extends RuntimeException {
    public InvalidNumberOfFunctionParameters(String funcName, int numberExpectedParameters, int numberParametersReceived) {
        super(String.format(
                "Invalid number of function parameters: <Function:%s> expected: %s receiver: %s",
                funcName,
                numberExpectedParameters,
                numberParametersReceived));
    }
}
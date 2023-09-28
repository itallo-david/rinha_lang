package dev.itallodavid.rinhalang.astjson;

import com.google.gson.*;
import dev.itallodavid.rinhalang.language.expressions.*;
import dev.itallodavid.rinhalang.language.kernel.*;
import dev.itallodavid.rinhalang.language.literals.LiteralBoolean;
import dev.itallodavid.rinhalang.language.literals.LiteralInteger;
import dev.itallodavid.rinhalang.language.literals.LiteralString;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.List;

public class FileJsonDeserializer implements JsonDeserializer<File> {

    @Override
    public File deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        Location location = buildLocation(object.get("location").getAsJsonObject());
        return new File(location, object.get("name").getAsString(), buildTerm(object.get("expression").getAsJsonObject()));
    }

    private Term buildTerm(JsonObject object) {
        Kind kind = Kind.valueOf(object.get("kind").getAsString());
        Location location = buildLocation(object.get("location").getAsJsonObject());

        switch (kind) {
            /* Literals */
            case Str -> {
                return new LiteralString(location, object.get("value").getAsString());
            }
            case Int -> {
                return new LiteralInteger(location, new BigInteger(object.get("value").getAsString()));
            }
            case Bool -> {
                return new LiteralBoolean(location, object.get("value").getAsBoolean());
            }

            /* Built-in Functions */
            case Print -> {
                return new ExpBuiltinFuncPrint(location, buildTerm(object.get("value").getAsJsonObject()));
            }
            case First -> {
                Term term = buildTerm(object.get("value").getAsJsonObject());
                return new ExpBuiltinFuncFirst(location, term);
            }
            case Second -> {
                Term term = buildTerm(object.get("value").getAsJsonObject());
                return new ExpBuiltinFuncSecond(location, term);
            }

            /* Let | Var */
            case Let -> {
                return new ExpLet(
                        location,
                        buildParameter(object.get("name").getAsJsonObject()),
                        buildTerm(object.get("value").getAsJsonObject()),
                        buildTerm(object.get("next").getAsJsonObject()));
            }
            case Var -> {
                return new ExpVar(location, object.get("text").getAsString());
            }

            /* Def. Functions | Call */
            case Function -> {
                JsonArray array = object.get("parameters").getAsJsonArray();

                List<Parameter> parameters = array.asList().stream().map(json -> {
                    JsonObject jsonObject = json.getAsJsonObject();
                    String text = jsonObject.get("text").getAsString();
                    Location paramLocation = buildLocation(jsonObject.get("location").getAsJsonObject());
                    return new Parameter(paramLocation, text);
                }).toList();

                return new ExpDefFunction(
                        location,
                        parameters,
                        buildTerm(object.get("value").getAsJsonObject()));
            }
            case Call -> {
                Term callee = buildTerm(object.get("callee").getAsJsonObject());
                List<Term> arguments = object.get("arguments").getAsJsonArray().asList().stream()
                        .map(argument -> buildTerm(argument.getAsJsonObject()))
                        .toList();
                return new ExpCall(location, callee, arguments);
            }

            case Tuple -> {
                Term first = buildTerm(object.get("first").getAsJsonObject());
                Term second = buildTerm(object.get("second").getAsJsonObject());
                return new ExpTuple(location, first, second);
            }

            /* if | else */
            case If -> {
                Term condition = buildTerm(object.get("condition").getAsJsonObject());
                Term then = buildTerm(object.get("then").getAsJsonObject());
                Term otherwise = buildTerm(object.get("otherwise").getAsJsonObject());
                return new ExpIf(location, condition, then, otherwise);
            }

            /* binary */
            case Binary -> {
                Term lhs = buildTerm(object.get("lhs").getAsJsonObject());
                BinaryOperator op = BinaryOperator.valueOf(object.get("op").getAsString());
                Term rhs = buildTerm(object.get("rhs").getAsJsonObject());
                return new ExpBinary(location, lhs, op, rhs);
            }
        }

        return null;
    }

    private Location buildLocation(JsonObject object) {
        return new Location(
                object.get("filename").getAsString(),
                object.get("start").getAsInt(),
                object.get("end").getAsInt());
    }

    private Parameter buildParameter(JsonObject object) {
        return new Parameter(buildLocation(object.get("location").getAsJsonObject()), object.get("text").getAsString());
    }
}

package fr.awildelephant.rdbms.server.explain;

/**
 * Q: Why not use jackson or google json?
 * A: Ahah shitty custom serializer go brr
 */
public final class JsonBuilder {

    private final StringBuilder stringBuilder = new StringBuilder();
    private int indentation;

    // No indentation, we append the open brace on the same line
    public JsonBuilder startObject() {
        stringBuilder.append("{\n");
        indentation++;

        return this;
    }

    public JsonBuilder endObject() {
        indentation--;
        indent().append("}\n");

        return this;
    }

    public JsonBuilder startArray() {
        stringBuilder.append("[\n");
        indentation++;

        return this;
    }

    public JsonBuilder nextArrayElement() {
        stringBuilder.append(',');
        newline();

        return this;
    }

    public JsonBuilder endArray() {
        indentation--;
        indent().append("]\n");

        return this;
    }

    public JsonBuilder field(String key) {
        indent().append('"').append(key).append('"').append(": ");

        return this;
    }

    public JsonBuilder field(String key, char character) {
        field(key);
        stringBuilder.append('"').append(character).append('"');

        return this;
    }

    public JsonBuilder field(String key, boolean value) {
        field(key);
        stringBuilder.append(value);

        return this;
    }

    public JsonBuilder field(String key, Object value) {
        field(key);
        stringBuilder.append('"').append(value).append('"');

        return this;
    }

    public JsonBuilder nextField() {
        stringBuilder.append(',');
        newline();

        return this;
    }

    private void newline() {
        stringBuilder.append('\n');
    }

    private StringBuilder indent() {
        for (int i = 0; i < indentation; i++) {
            stringBuilder.append('\t');
        }

        return stringBuilder;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}

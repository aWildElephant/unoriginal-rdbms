package fr.awildelephant.rdbms.explain;

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

    public void endObject() {
        indentation--;
        indent().append("}\n");
    }

    public void startArray() {
        stringBuilder.append("[\n");
        indentation++;
    }

    public void nextArrayElement() {
        stringBuilder.append(',');
        newline();
    }

    public void endArray() {
        indentation--;
        indent().append("]\n");
    }

    public void field(String key) {
        indent().append('"').append(key).append('"').append(": ");
    }

    public void field(String key, char character) {
        field(key);
        stringBuilder.append('"').append(character).append('"');
    }

    public void field(String key, boolean value) {
        field(key);
        stringBuilder.append(value);
    }

    public JsonBuilder field(String key, Object value) {
        field(key);
        stringBuilder.append('"').append(value).append('"');

        return this;
    }

    public JsonBuilder type(NodeType type) {
        field("type", type.toString());

        return this;
    }

    public void nextField() {
        stringBuilder.append(',');
        newline();
    }

    private void newline() {
        stringBuilder.append('\n');
    }

    private StringBuilder indent() {
        stringBuilder.append("\t".repeat(Math.max(0, indentation)));

        return stringBuilder;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}

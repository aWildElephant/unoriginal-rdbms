package fr.awildelephant.rdbms.util.printer.table;

import java.util.List;

public class Row {

    private final List<String> content;

    public Row(List<String> content) {
        this.content = content;
    }

    public String get(int index) {
        return content.get(index);
    }

    public int size() {
        return content.size();
    }
}

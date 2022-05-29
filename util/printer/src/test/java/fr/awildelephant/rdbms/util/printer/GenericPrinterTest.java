package fr.awildelephant.rdbms.util.printer;

import fr.awildelephant.rdbms.util.printer.table.Table;
import fr.awildelephant.rdbms.util.printer.table.TableBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericPrinterTest {

    @Test
    void it_should_display_an_empty_table() {
        final Table table = TableBuilder.builder().build();

        assertPrint(table,

                "<empty>");
    }

    @Test
    void it_should_display_a_table_with_one_column() {
        final Table table = TableBuilder.builder()
                .addHeaderRow("column")
                .addRow("1")
                .addRow("2")
                .addRow("3")
                .build();

        assertPrint(table,

                """
                        ╭────────╮
                        │ column │
                        ├────────┤
                        │ 1      │
                        │ 2      │
                        │ 3      │
                        ╰────────╯
                        """);
    }

    @Test
    void it_should_display_a_table_with_two_columns() {
        final Table table = TableBuilder.builder()
                .addHeaderRow("index", "word")
                .addRow("1", "hello")
                .addRow("2", "world")
                .build();

        assertPrint(table,

                """
                        ╭───────┬───────╮
                        │ index │ word  │
                        ├───────┼───────┤
                        │ 1     │ hello │
                        │ 2     │ world │
                        ╰───────┴───────╯
                        """);
    }

    private void assertPrint(Table input, String expected) {
        assertEquals(expected, new GenericPrinter().print(input));
    }
}
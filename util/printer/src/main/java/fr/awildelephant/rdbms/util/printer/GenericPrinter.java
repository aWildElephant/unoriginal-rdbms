package fr.awildelephant.rdbms.util.printer;

import fr.awildelephant.rdbms.util.printer.table.Row;
import fr.awildelephant.rdbms.util.printer.table.Table;

import java.util.stream.Stream;

public class GenericPrinter {

    public String print(Table table) {
        final int[] columnLengths = getColumnLengths(table);

        final int numberOfColumns = columnLengths.length;

        if (numberOfColumns == 0) {
            return "<empty>";
        }

        final StringBuilder stringBuilder = new StringBuilder();
        drawTopBorder(stringBuilder, columnLengths);
        table.headerRows().forEach(row -> drawRow(stringBuilder, row, columnLengths));
        drawSeparator(stringBuilder, columnLengths);
        table.rows().forEach(row -> drawRow(stringBuilder, row, columnLengths));
        drawBottomBorder(stringBuilder, columnLengths);
        return stringBuilder.toString();
    }

    private void drawTopBorder(StringBuilder stringBuilder, int[] columnLengths) {
        drawLine(stringBuilder, columnLengths, '╭', '┬', '╮');
    }

    private void drawLine(StringBuilder stringBuilder, int[] columnLengths, char leftmost, char columnSeparator, char rightmost) {
        stringBuilder.append(leftmost);
        final int numberOfColumns = columnLengths.length;
        for (int i = 0; i < numberOfColumns; i++) {
            stringBuilder.append("─".repeat(Math.max(0, columnLengths[i] + 2)));
            if (i < numberOfColumns - 1) {
                stringBuilder.append(columnSeparator);
            }
        }
        stringBuilder.append(rightmost);
        drawNewLine(stringBuilder);
    }

    private void drawRow(StringBuilder stringBuilder, Row row, int[] columnLengths) {
        stringBuilder.append('│');

        final int size = row.size();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(' ');
            final String cellContent = row.get(i);
            stringBuilder.append(cellContent);
            final int cellLength = columnLengths[i];
            stringBuilder.append(" ".repeat(Math.max(0, cellLength + 1 - cellContent.length())));
            stringBuilder.append('│');
        }

        drawNewLine(stringBuilder);
    }

    private void drawSeparator(StringBuilder stringBuilder, int[] columnLengths) {
        drawLine(stringBuilder, columnLengths, '├', '┼', '┤');
    }

    private void drawBottomBorder(StringBuilder stringBuilder, int[] columnLengths) {
        drawLine(stringBuilder, columnLengths, '╰', '┴', '╯');
    }

    private void drawNewLine(StringBuilder stringBuilder) {
        stringBuilder.append('\n');
    }

    private int[] getColumnLengths(Table table) {
        final int numberOfColumns = Stream.concat(table.headerRows().stream(), table.rows().stream())
                .mapToInt(Row::size)
                .max()
                .orElse(0);

        final int[] columnLengths = new int[numberOfColumns];

        for (int index = 0; index < numberOfColumns; index++) {
            columnLengths[index] = 0;
        }

        for (Row row : table.headerRows()) {
            final int rowSize = row.size();
            for (int i = 0; i < rowSize; i++) {
                final int cellLength = row.get(i).length();
                if (cellLength > columnLengths[i]) {
                    columnLengths[i] = cellLength;
                }
            }
        }

        for (Row row : table.rows()) {
            final int rowSize = row.size();
            for (int i = 0; i < rowSize; i++) {
                final int cellLength = row.get(i).length();
                if (cellLength > columnLengths[i]) {
                    columnLengths[i] = cellLength;
                }
            }
        }

        return columnLengths;
    }
}

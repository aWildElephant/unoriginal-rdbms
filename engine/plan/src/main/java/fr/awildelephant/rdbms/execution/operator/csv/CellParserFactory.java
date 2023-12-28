package fr.awildelephant.rdbms.execution.operator.csv;

import fr.awildelephant.rdbms.schema.Domain;

public final class CellParserFactory {

    private static final BooleanCellParser BOOLEAN_CELL_PARSER = new BooleanCellParser();
    private static final DateCellParser DATE_CELL_PARSER = new DateCellParser();
    private static final DecimalCellParser DECIMAL_CELL_PARSER = new DecimalCellParser();
    private static final IntegerCellParser INTEGER_CELL_PARSER = new IntegerCellParser();
    private static final LongCellParser LONG_CELL_PARSER = new LongCellParser();
    private static final NullCellParser NULL_CELL_PARSER = new NullCellParser();
    private static final TextCellParser TEXT_CELL_PARSER = new TextCellParser();

    private CellParserFactory() {

    }

    public static CellParser build(Domain domain) {
        return switch (domain) {
            case BOOLEAN -> BOOLEAN_CELL_PARSER;
            case DATE -> DATE_CELL_PARSER;
            case DECIMAL -> DECIMAL_CELL_PARSER;
            case INTEGER -> INTEGER_CELL_PARSER;
            case LONG -> LONG_CELL_PARSER;
            case NULL -> NULL_CELL_PARSER;
            case TEXT -> TEXT_CELL_PARSER;
            default ->
                    throw new IllegalStateException(); // TODO: proper exception. VERSION and INTERVAL are not yet supported in CSV file
        };
    }
}

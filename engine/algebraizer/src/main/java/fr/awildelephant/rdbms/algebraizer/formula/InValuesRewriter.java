package fr.awildelephant.rdbms.algebraizer.formula;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.InValueList;
import fr.awildelephant.rdbms.ast.Row;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.Values.rows;

public final class InValuesRewriter extends DefaultASTVisitor<AST> {

    @Override
    public AST visit(InValueList inValueList) {
        final List<Row> rows = new ArrayList<>();

        for (AST value : inValueList.values()) {
            rows.add(row(value));
        }

        return rows(rows);
    }

    @Override
    public AST defaultVisit(AST node) {
        return node;
    }
}

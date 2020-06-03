package fr.awildelephant.rdbms.algebraizer.formula;

import fr.awildelephant.rdbms.algebraizer.FirstColumnNameResolver;
import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Exists;
import fr.awildelephant.rdbms.ast.InValueList;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import fr.awildelephant.rdbms.ast.value.In;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fr.awildelephant.rdbms.algebraizer.formula.SubqueryJoiner.cartesianProductJoiner;
import static fr.awildelephant.rdbms.algebraizer.formula.SubqueryJoiner.semiJoinJoiner;
import static fr.awildelephant.rdbms.ast.ColumnAlias.columnAlias;
import static fr.awildelephant.rdbms.ast.InValueList.inValueList;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.value.CountStar.countStar;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.ast.value.Greater.greater;
import static fr.awildelephant.rdbms.ast.value.In.in;
import static fr.awildelephant.rdbms.ast.value.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.ast.value.ScalarSubquery.scalarSubquery;

public final class SubqueryExtractor extends DefaultFormulaRewriter {

    private final InValuesRewriter inValuesRewriter = new InValuesRewriter();
    private final FirstColumnNameResolver firstColumnNameResolver = new FirstColumnNameResolver();

    private final List<SubqueryJoiner> subqueries = new ArrayList<>();

    public List<SubqueryJoiner> subqueries() {
        return subqueries;
    }

    @Override
    public AST visit(Exists exists) {
        final String id = UUID.randomUUID().toString();

        final Select subquery = select(List.of(columnAlias(countStar(), id)),
                exists.input(),
                null,
                null,
                null,
                null);

        subqueries.add(cartesianProductJoiner(subquery));

        return greater(unqualifiedColumnName(id), integerLiteral(0));
    }

    @Override
    public AST visit(In in) {
        final AST rewrittenInValue = inValuesRewriter.apply(in.value());

        final String columnName = firstColumnNameResolver.apply(rewrittenInValue);

        subqueries.add(semiJoinJoiner(rewrittenInValue, equal(in.input(), unqualifiedColumnName(columnName))));

        return BooleanLiteral.TRUE; // FIXME: not very clean to leave this useless true filter if we're in the where clause, not working in the output columns
    }

    @Override
    public AST visit(Select select) {
        final String id = UUID.randomUUID().toString();

        subqueries.add(cartesianProductJoiner(scalarSubquery(select, id)));

        return unqualifiedColumnName(id);
    }

    @Override
    public AST defaultVisit(AST node) {
        return node;
    }
}

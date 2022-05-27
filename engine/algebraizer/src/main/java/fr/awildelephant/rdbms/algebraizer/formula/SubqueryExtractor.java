package fr.awildelephant.rdbms.algebraizer.formula;

import fr.awildelephant.rdbms.algebraizer.ColumnNameResolver;
import fr.awildelephant.rdbms.algebraizer.FirstColumnNameResolver;
import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Exists;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.value.In;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fr.awildelephant.rdbms.algebraizer.formula.SubqueryJoiner.cartesianProductJoiner;
import static fr.awildelephant.rdbms.algebraizer.formula.SubqueryJoiner.semiJoinJoiner;
import static fr.awildelephant.rdbms.ast.ColumnAlias.columnAlias;
import static fr.awildelephant.rdbms.ast.QualifiedColumnName.qualifiedColumnName;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.TableAlias.tableAlias;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.ast.value.ScalarSubquery.scalarSubquery;

public final class SubqueryExtractor extends DefaultFormulaRewriter {

    private final InValuesRewriter inValuesRewriter = new InValuesRewriter();
    private final ColumnNameResolver columnNameResolver = new ColumnNameResolver();
    private final FirstColumnNameResolver firstColumnNameResolver = new FirstColumnNameResolver(columnNameResolver);

    private final List<SubqueryJoiner> subqueries = new ArrayList<>();

    public List<SubqueryJoiner> subqueries() {
        return subqueries;
    }

    @Override
    public AST visit(Exists exists) {
        final String id = UUID.randomUUID().toString();

        subqueries.add(semiJoinJoiner(exists.input(), id));

        return unqualifiedColumnName(id);
    }

    @Override
    public AST visit(In in) {
        final String inValuesColumnAlias = UUID.randomUUID().toString();

        final AST rewrittenInValue = inValuesRewriter.apply(in.value());

        final String columnName = firstColumnNameResolver.apply(rewrittenInValue);

        final Select aliasedRewrittenInValue = select(List.of(columnAlias(unqualifiedColumnName(columnName), inValuesColumnAlias)), rewrittenInValue, null, null, null, null);

        final String semiJoinIdentifier = columnNameResolver.apply(in);

        subqueries.add(semiJoinJoiner(aliasedRewrittenInValue, equal(in.input(), unqualifiedColumnName(inValuesColumnAlias)), semiJoinIdentifier));

        return unqualifiedColumnName(semiJoinIdentifier);
    }

    @Override
    public AST visit(Select select) {
        final String alias = UUID.randomUUID().toString();

        subqueries.add(cartesianProductJoiner(scalarSubquery(tableAlias(select, alias))));
        final String subqueryOutputColumnName = firstColumnNameResolver.apply(select);

        return qualifiedColumnName(alias, subqueryOutputColumnName);
    }

    @Override
    public AST defaultVisit(AST node) {
        return node;
    }
}

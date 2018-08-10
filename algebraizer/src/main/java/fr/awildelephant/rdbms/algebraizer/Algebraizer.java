package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnAlias;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.engine.Engine;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.plan.BaseTable;
import fr.awildelephant.rdbms.plan.DistinctNode;
import fr.awildelephant.rdbms.plan.Plan;
import fr.awildelephant.rdbms.plan.ProjectionNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static fr.awildelephant.rdbms.plan.AliasNode.aliasOperator;
import static fr.awildelephant.rdbms.schema.Alias.alias;

public final class Algebraizer extends DefaultASTVisitor<Plan> {

    private final Engine engine;

    public Algebraizer(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Plan visit(fr.awildelephant.rdbms.ast.Distinct distinct) {
        return new DistinctNode(apply(distinct.input()));
    }

    @Override
    public Plan visit(Select select) {
        final Plan fromClause = visit(select.inputTable());

        final ColumnNameResolver columnNameResolver = new ColumnNameResolver(fromClause.schema());

        final List<? extends AST> outputColumns = select.outputColumns();

        final ArrayList<String> transformedColumnNames = new ArrayList<>();
        final HashMap<String, String> aliasing = new HashMap<>();

        for (AST column : outputColumns) {
            if (column instanceof ColumnAlias) {
                final ColumnAlias aliasedColumn = (ColumnAlias) column;
                final String unaliasedColumnName = columnNameResolver.apply(aliasedColumn.input())
                                                                     .findFirst()
                                                                     .orElseThrow(IllegalStateException::new);

                aliasing.put(unaliasedColumnName, aliasedColumn.alias());

                transformedColumnNames.add(unaliasedColumnName);
            } else {
                columnNameResolver.apply(column).forEach(transformedColumnNames::add);
            }
        }

        final ProjectionNode projection = new ProjectionNode(transformedColumnNames, fromClause);

        if (aliasing.isEmpty()) {
            return aliasOperator(alias(aliasing), projection);
        }

        return projection;
    }

    @Override
    public Plan visit(TableName tableReference) {
        final String name = tableReference.name();
        final Table table = engine.get(name);

        return new BaseTable(name, table.schema());

    }

    @Override
    public Plan defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}

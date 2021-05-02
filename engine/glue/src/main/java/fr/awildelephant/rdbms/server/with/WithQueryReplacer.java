package fr.awildelephant.rdbms.server.with;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.ast.visitor.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.visitor.FullVisitor;

import java.util.Map;

public class WithQueryReplacer extends DefaultASTVisitor<AST> {

    private final FullVisitor fullVisitor;
    private final Map<String, AST> withQueries;

    public WithQueryReplacer(Map<String, AST> withQueries) {
        this.withQueries = withQueries;
        this.fullVisitor = new FullVisitor(this);
    }

    @Override
    public AST visit(TableName tableName) {
        final AST definition = withQueries.get(tableName.name());

        if (definition == null) {
            return tableName;
        }

        return definition;
    }

    @Override
    public AST defaultVisit(AST node) {
        return fullVisitor.apply(node);
    }
}

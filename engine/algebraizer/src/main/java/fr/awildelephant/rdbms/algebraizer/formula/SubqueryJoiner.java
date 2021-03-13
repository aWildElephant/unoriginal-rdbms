package fr.awildelephant.rdbms.algebraizer.formula;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import fr.awildelephant.rdbms.plan.join.JoinType;

import static fr.awildelephant.rdbms.plan.join.JoinType.INNER;
import static fr.awildelephant.rdbms.plan.join.JoinType.SEMI;

public final class SubqueryJoiner {

    private final AST subquery;
    private final AST predicate;
    private final JoinType joinType;
    private final String identifier;

    private SubqueryJoiner(AST subquery, AST predicate, JoinType joinType, String identifier) {
        this.subquery = subquery;
        this.predicate = predicate;
        this.joinType = joinType;
        this.identifier = identifier;
    }

    public static SubqueryJoiner cartesianProductJoiner(AST subquery) {
        return new SubqueryJoiner(subquery, null, INNER, null);
    }

    public static SubqueryJoiner semiJoinJoiner(AST subquery, String identifier) {
        return new SubqueryJoiner(subquery, BooleanLiteral.TRUE, SEMI, identifier);
    }

    public static SubqueryJoiner semiJoinJoiner(AST subquery, AST predicate, String identifier) {
        return new SubqueryJoiner(subquery, predicate, SEMI, identifier);
    }

    public AST subquery() {
        return subquery;
    }

    public AST predicate() {
        return predicate;
    }

    public JoinType type() {
        return joinType;
    }

    public String identifier() {
        return identifier;
    }
}

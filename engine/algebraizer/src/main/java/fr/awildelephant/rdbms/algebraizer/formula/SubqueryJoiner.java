package fr.awildelephant.rdbms.algebraizer.formula;

import fr.awildelephant.rdbms.ast.AST;

import static fr.awildelephant.rdbms.algebraizer.formula.SubqueryJoiner.JoinType.CARTESIAN;
import static fr.awildelephant.rdbms.algebraizer.formula.SubqueryJoiner.JoinType.SEMI_JOIN;

public final class SubqueryJoiner {

    private final AST subquery;
    private final AST predicate;
    private final JoinType joinType;

    private SubqueryJoiner(AST subquery, AST predicate, JoinType joinType) {
        this.subquery = subquery;
        this.predicate = predicate;
        this.joinType = joinType;
    }

    public static SubqueryJoiner cartesianProductJoiner(AST subquery) {
        return new SubqueryJoiner(subquery, null, CARTESIAN);
    }

    public static SubqueryJoiner semiJoinJoiner(AST subquery, AST predicate) {
        return new SubqueryJoiner(subquery, predicate, SEMI_JOIN);
    }

    public AST subquery() {
        return subquery;
    }

    public AST predicate() {
        return predicate;
    }

    public JoinType joinType() {
        return joinType;
    }

    public enum JoinType {
        CARTESIAN, SEMI_JOIN
    }
}

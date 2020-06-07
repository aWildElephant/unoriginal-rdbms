package fr.awildelephant.rdbms.algebraizer.formula;

import fr.awildelephant.rdbms.ast.AST;

import static fr.awildelephant.rdbms.algebraizer.formula.SubqueryJoiner.JoinType.CARTESIAN;
import static fr.awildelephant.rdbms.algebraizer.formula.SubqueryJoiner.JoinType.SEMI_JOIN;

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
        return new SubqueryJoiner(subquery, null, CARTESIAN, null);
    }

    public static SubqueryJoiner semiJoinJoiner(AST subquery, AST predicate, String identifier) {
        return new SubqueryJoiner(subquery, predicate, SEMI_JOIN, identifier);
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

    public String identifier() {
        return identifier;
    }

    public enum JoinType {
        CARTESIAN, SEMI_JOIN
    }
}

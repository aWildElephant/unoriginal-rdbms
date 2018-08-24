package fr.awildelephant.rdbms.evaluator;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public class Formula {

    private final AST tree;
    private final String outputName;

    public Formula(AST tree, String outputName) {
        this.tree = tree;
        this.outputName = outputName;
    }

    public DomainValue evaluate() {
        return new Evaluator().apply(tree);
    }

    public String outputName() {
        return outputName;
    }

    public Domain outputType() {
        return INTEGER;
    }
}

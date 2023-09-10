package fr.awildelephant.rdbms.explain;

import fr.awildelephant.rdbms.execution.LogicalOperator;
import fr.awildelephant.rdbms.explain.tree.Tree;
import fr.awildelephant.rdbms.storage.data.table.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public record LogicalPlanTableBuilder(ExplanationTreeBuilder explanationTreeBuilder, ExplanationTableBuilder explanationTableBuilder) {

    private static final String PLACEHOLDER = "";

    public Table explain(LogicalOperator plan) {
        final Tree<String> explanationTree = explanationTreeBuilder.apply(plan);

        final List<String> explanation = buildExplanationList(explanationTree);

        return explanationTableBuilder.build(explanation);
    }

    private List<String> buildExplanationList(Tree<String> explanationTree) {
        final List<String> result = new ArrayList<>();

        addNode(result, explanationTree);

        return result;
    }

    /**
     * @return the row index for the explanation node
     */
    private int addNode(List<String> result, Tree<String> node) {
        final int rowIndex = result.size();
        if (node.isLeaf()) {
            result.add(node.content());
        } else {
            result.add(PLACEHOLDER);
            final StringJoiner inputsJoiner = new StringJoiner(", ");
            for (Tree<String> child : node.children()) {
                inputsJoiner.add(Integer.toString(addNode(result, child)));
            }
            result.set(rowIndex, node.content() + " inputs:[" +inputsJoiner + ']');
        }
        return rowIndex;
    }
}

package fr.awildelephant.rdbms.ast.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indique qu'un noeud n'existe qu'après transformation de l'AST.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Intermediate {
}

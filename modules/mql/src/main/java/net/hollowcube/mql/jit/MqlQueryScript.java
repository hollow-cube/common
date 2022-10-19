package net.hollowcube.mql.jit;

/**
 * An example MQL script interface. May be used if only a query object is required, otherwise serves as documentation.
 * <p>
 * A script interface is a java interface with the following properties:
 * <ul>
 *     <li>Must be public, non-sealed.</li>
 *     <li>Must have a single abstract method which returns a double ({@link FunctionalInterface} can enforce this at compile time, but is not required)</li>
 *     <li>The abstract method may have 1 or more parameters, but all must be annotated with {@link MqlEnv}.</li>
 *     <li>The abstract method may not have generic parameters.</li>
 *     <li>The interface may use generic parameters, but they must be passed to the {@link MqlCompiler}</li>
 * </ul>
 */
@FunctionalInterface
public interface MqlQueryScript<Query> {
    double evaluate(@MqlEnv({"query", "q"}) Query query);
}

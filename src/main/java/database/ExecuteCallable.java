package database;

import javax.persistence.EntityManager;

/**
 * Represent query that can returns a result.
 * Implementors define a single method with single
 * argument {@link EntityManager} called {@code execute}.
 *
 * @param <T> type of return result
 *
 * @autor  Alex
 */
@FunctionalInterface
public interface ExecuteCallable<T> {
    /**
     * Use for implements query from client code, can return a result.
     *
     * @param entityManager {@link EntityManager} use for query
     *
     * @return T type of result query
     */
    T execute(EntityManager entityManager);
}

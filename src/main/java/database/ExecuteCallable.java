package database;

import javax.persistence.EntityManager;

/**
 * Represent query that can returns a result.
 * Implementors define a single method with single
 * argument {@link EntityManager} called {@code execute}.
 *
 * @autor Alex
 * @param <T> type of return query result
 */
@FunctionalInterface
public interface ExecuteCallable<T> {
    /**
     * Use for implements query from client code, can return a result.
     *
     * @param entityManager {@link EntityManager} used to query
     * @return <T> type of return query result
     */
    T execute(EntityManager entityManager);
}

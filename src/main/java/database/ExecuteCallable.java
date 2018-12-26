package database;

import javax.persistence.EntityManager;

/**
 * Represent query that can returns a result and may throw an exception.
 * Implementors define a single method with single
 * argument {@link EntityManager} called {@code execute}.
 *
 * @param <T> type of return result
 */
@FunctionalInterface
public interface ExecuteCallable<T> {
    /**
     * Use for implements query from client code, can return a result,
     * or throws an exception if unable to do so.
     *
     * @param entityManager {@link EntityManager} use for query
     *
     * @return T type of result query
     *
     * @throws Exception if unable to execute query
     */
    T execute(EntityManager entityManager) throws Exception;
}

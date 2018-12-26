package database;

import javax.persistence.EntityManager;

//todo: разделить на два? Один для транзакций, другой чисто для запросов?
@FunctionalInterface
public interface ExecuteCallable<T> {
    T execute(EntityManager entityManager);
}

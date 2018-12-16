package database;

import org.hibernate.HibernateException;
import org.hibernate.Session;

public interface ExecuteCallable<T> {
    T execute(Session session) throws HibernateException;
}

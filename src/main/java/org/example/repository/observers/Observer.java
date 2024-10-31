package org.example.repository.observers;

public interface Observer<T> {
    void update(T entity);
}
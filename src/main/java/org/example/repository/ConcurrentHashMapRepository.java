package org.example.repository;

import java.util.List;


public interface ConcurrentHashMapRepository<Id, E> {

    E get(Id id);

    E save(E e);

    List<E> getAll();

    E update(E e);

    E delete(Id id);
}

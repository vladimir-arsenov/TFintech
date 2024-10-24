package org.example.repository.hashMapRepository;

import java.util.List;


public interface ConcurrentHashMapRepository<Id, E> {

    E get(Id id);

    void add(E e);

    List<E> getAll();

    E update(E e);

    E delete(Id id);
}

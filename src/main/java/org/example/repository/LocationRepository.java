package org.example.repository;

import org.example.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findBySlug(String slug);

    Location deleteBySlug(String slug);
}

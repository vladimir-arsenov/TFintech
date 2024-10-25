package org.example.repository;

import org.example.model.Event;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAll(Specification<Event> spec);


    static Specification<Event> eventSpecification(String name, Long location, LocalDate fromDate, LocalDate toDate) {
        List<Specification<Event>> specs = new ArrayList<>();
        if (name != null) {
            specs.add((event, query, cb) -> cb.equal(event.get("name"), name));
        }

        if (location != null) {
            specs.add((event, query, cb) -> cb.equal(event.get("location").get("id"), location));
        }

        if (fromDate != null && toDate != null) {
            specs.add((event, query, cb) -> cb.between(event.get("date"), fromDate, toDate));
        } else if (fromDate != null) {
            specs.add((event, query, cb) -> {
                System.out.println(event.get("date") + " " + fromDate);
                return cb.greaterThanOrEqualTo(event.get("date"), fromDate);});
        } else if (toDate != null) {
            specs.add((event, query, cb) -> cb.lessThanOrEqualTo(event.get("date"), toDate));
        }

        return specs.stream().reduce(Specification::and).orElse(null);

    }
}

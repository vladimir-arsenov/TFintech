CREATE TABLE events
(
    id          BIGSERIAL PRIMARY KEY ,
    name        VARCHAR(255),
    date        date,
    location_id BIGINT,
    CONSTRAINT fk_event_location FOREIGN KEY (location_id) REFERENCES locations(id)
);


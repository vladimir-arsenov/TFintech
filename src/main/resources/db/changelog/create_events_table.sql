CREATE TABLE events
(
    id          BIGSERIAL PRIMARY KEY ,
    name        VARCHAR(255) NOT NULL ,
    date        date NOT NULL,
    location_id BIGINT NOT NULL ,
    CONSTRAINT fk_event_location FOREIGN KEY (location_id) REFERENCES locations(id)
);


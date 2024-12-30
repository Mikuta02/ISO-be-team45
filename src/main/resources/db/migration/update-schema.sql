CREATE TABLE rabbit_locations
(
    id        VARCHAR(255)     NOT NULL,
    name      VARCHAR(255),
    latitude  DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_rabbit_locations PRIMARY KEY (id)
);
--liquibase formatted sql

--changeset wall:1
CREATE TABLE gbc_wall_item(
    id Long NOT NULL PRIMARY KEY,
    file VARCHAR(100) NOT NULL,
    path VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    date_creation TIMESTAMP NOT NULL,
    nb_like INTEGER,
    ratio float,
);
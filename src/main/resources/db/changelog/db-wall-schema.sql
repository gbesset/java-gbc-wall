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


--TODO ??
CREATE TABLE gbc_wall_user
(
    id SERIAL NOT NULL PRIMARY KEY,
    login VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    date_creation TIMESTAMP NOT NULL,
    is_active boolean NOT NULL,
    last_connection TIMESTAMP,
    CONSTRAINT uk_ehgucdmpxnrqu4vlqi3u2ohhh UNIQUE (login)
)

INSERT INTO gbc_wall_user (id, login, password, is_active, role, date_creation, last_connection) VALUES
(1, 'guillaume.besset@gmail.com', 'wallDemo', true, 'ADMIN', '2018-09-04 16:05:00.0', null),
(2, 'karen.bimboes@gmail.com', 'wallDemo', true, 'ADMIN', '2018-09-04 16:05:00.0', null),
(3, 'demo@gbcreation.fr', 'wallDemo', true, 'ADVANCED', '2018-09-04 16:05:00.0', null);
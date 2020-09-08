--######### works with postgres > 8.0 versions
CREATE EXTENSION IF NOT EXISTS hstore;

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1;

CREATE TABLE IF NOT EXISTS file_data
(
    id         SERIAL PRIMARY KEY,
    filename   varchar(255),
    type       varchar(8),
    properties hstore
);

CREATE TABLE IF NOT EXISTS xml_data
(
    id         SERIAL PRIMARY KEY,
    filename   varchar(255),
    type       varchar(8),
    properties hstore
);

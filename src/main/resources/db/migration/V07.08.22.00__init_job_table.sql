alter table job
    add column properties hstore;

--######### for postgres > 8.0 versions

/* CREATE EXTENSION IF NOT EXISTS hstore;

CREATE TABLE IF NOT EXISTS Job
    ADD COLUMN id SERIAL PRIMARY KEY,
    ADD COLUMN name varchar(255),
    ADD COLUMN filename varchar(255),
    ADD COLUMN properties hstore
*/
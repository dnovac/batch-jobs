--######### for postgres > 8.0 versions
 --CREATE EXTENSION IF NOT EXISTS hstore;

CREATE TABLE IF NOT EXISTS import_data (
     id SERIAL PRIMARY KEY,
     filename varchar(255),
     properties hstore
)

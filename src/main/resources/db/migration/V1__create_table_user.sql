CREATE TABLE users(
    id serial PRIMARY KEY,
    name varchar(255),
    email varchar (255) UNIQUE,
    password varchar(255)
);
# car-rentals
building a java app to manage car rentals
https://grok.com/share/bGVnYWN5_e0538beb-e901-4da1-91a0-2010f42254fe

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE cars (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    model VARCHAR(50),
    year INTEGER,
    description TEXT
);

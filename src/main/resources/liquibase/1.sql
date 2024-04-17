create table employee (
id SERIAL PRIMARY KEY,
name VARCHAR(100),
password VARCHAR(100),
role VARCHAR(100)
);

create table report (
id SERIAL PRIMARY KEY,
title VARCHAR(100),
state VARCHAR(100),
author_id INTEGER,
last_updated TIMESTAMP,
file VARCHAR(100),
comment VARCHAR(100),
CONSTRAINT fk_author_id
      FOREIGN KEY(author_id)
        REFERENCES employee(id)
);

create table period_time (
id SERIAL PRIMARY KEY,
from_date TIMESTAMP,
to_date TIMESTAMP,
executed BOOLEAN
);


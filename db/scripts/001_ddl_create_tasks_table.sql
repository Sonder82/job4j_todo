CREATE TABLE if not exists tasks (
id SERIAL PRIMARY KEY,
description varchar not null,
created TIMESTAMP not null,
done BOOLEAN
);
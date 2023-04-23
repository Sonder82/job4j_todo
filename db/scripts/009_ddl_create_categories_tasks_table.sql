CREATE TABLE categories_tasks (
id serial PRIMARY KEY,
   task_id int not null REFERENCES tasks(id),
   category_id int not null REFERENCES categories(id)
);
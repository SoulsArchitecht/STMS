-- liquibase formatted sql

-- changeset serge_sh:15042025-03

insert into tasks (title, description, status, priority, assignee_id, author_id)
values ('First task','$Do nothing',
        'PENDING', 'LOW', 3, 2);

insert into tasks (title, description, status, priority, assignee_id, author_id)
values ('Second task','$Do nothing 2',
        'IN_PROGRESS', 'HIGH', 2, 1);

insert into tasks (title, description, status, priority, assignee_id, author_id)
values ('Third task','$Do nothing 3',
        'COMPLETED', 'MEDIUM', 2, 1);
-- liquibase formatted sql

-- changeset serge_sh:04042025-1

CREATE TABLE users (
    id BIGSERIAL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    role VARCHAR(64) NOT NULL CHECK (role IN ('ROLE_ADMIN', 'ROLE_USER')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE tasks (
    id BIGSERIAL,
    title VARCHAR(255) NOT NULL,
    description text,
    status VARCHAR(255) NOT NULL CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED')),
    priority VARCHAR(255) NOT NULL CHECK (priority IN ('HIGH', 'MEDIUM', 'LOW')),
    author_id BIGINT NOT NULL,
    assignee_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT  CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_tasks PRIMARY KEY (id),
    FOREIGN KEY (author_id) REFERENCES users(id),
    FOREIGN KEY (assignee_id) REFERENCES users(id)
);

CREATE TABLE comments (
    id BIGSERIAL,
    text VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    author_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (id),
    FOREIGN KEY (author_id) REFERENCES users(id),
    FOREIGN KEY (task_id) REFERENCES tasks(id)
);

CREATE INDEX idx_tasks_author_id ON tasks(author_id);
CREATE INDEX idx_tasks_assignee_id ON tasks(assignee_id);
CREATE INDEX idx_comments_task_id ON comments(task_id);
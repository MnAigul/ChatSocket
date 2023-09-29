DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS "group";

CREATE TABLE "user"(
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    photo VARCHAR(255),
    user_role VARCHAR(255) NOT NULL DEFAULT 'NOADMIN',
    status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE "group"(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    photo VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);


CREATE TABLE message(
    id SERIAL PRIMARY KEY,
    from_user_id INT NOT NULL,
    to_group_id INT NOT NULL,
    message_text VARCHAR(255) NOT NULL,
    sent_time TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (from_user_id) REFERENCES "user"(id),
    FOREIGN KEY (to_group_id) REFERENCES "group"(id)    
);

INSERT INTO "user" (username, password, user_role, status) VALUES ('admin', 'admin', 'ADMIN', 'ACTIVE');
INSERT INTO "user" (username, password, status) VALUES ('user', 'user', 'ACTIVE');
INSERT INTO "user" (username, password, status) VALUES ('user2', 'user2', 'ACTIVE');

INSERT INTO "group" (name, photo) VALUES ('group1', 'photo1');
INSERT INTO "group" (name, photo) VALUES ('group2', 'photo2');
INSERT INTO "group" (name, photo) VALUES ('group3', 'photo3');



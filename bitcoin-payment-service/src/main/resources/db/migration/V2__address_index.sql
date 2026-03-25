CREATE TABLE address_index (
    id SMALLINT PRIMARY KEY DEFAULT 1 CHECK (id = 1),
    next_index BIGINT NOT NULL DEFAULT 0
);

INSERT INTO address_index (id, next_index) VALUES (1, 0);

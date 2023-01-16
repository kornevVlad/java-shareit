CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS items (
    item_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_name VARCHAR (100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    available VARCHAR(50),
    owner_id BIGINT NOT NULL,
    request_id BIGINT,
    CONSTRAINT PK_ITEM PRIMARY KEY (item_id),
    CONSTRAINT FK_ITEM_FOR_OWNER FOREIGN KEY (owner_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (
    booking_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    item_id BIGINT NOT NULL,
    booker_id BIGINT NOT NULL,
    status VARCHAR (50),
    CONSTRAINT PK_BOOKING PRIMARY KEY (booking_id),
    CONSTRAINT FK_BOOKING_FOR_BOOKER FOREIGN KEY (booker_id) REFERENCES users (user_id),
    CONSTRAINT FK_BOOKING_FOR_ITEM FOREIGN KEY (item_id) REFERENCES items (item_id)
);

CREATE TABLE IF NOT EXISTS comments (
    comment_id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text      VARCHAR(1000)                           NOT NULL,
    item_id   BIGINT                                  NOT NULL,
    author_id BIGINT                                  NOT NULL,
    created   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT PK_COMMENTS PRIMARY KEY (comment_id),
    CONSTRAINT FK_COMMENT_FOR_ITEM FOREIGN KEY (item_id) REFERENCES items (item_id),
    CONSTRAINT FK_COMMENT_FOR_USER FOREIGN KEY (author_id) REFERENCES users (user_id)
);
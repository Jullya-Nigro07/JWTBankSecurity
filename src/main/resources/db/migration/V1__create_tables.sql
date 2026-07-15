CREATE TABLE users(
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(255) NOT NULL,
    email varchar (255) UNIQUE NOT NULL,
    password varchar(255) NOT NULL
);

CREATE TABLE accounts(
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    balance NUMERIC(15,2) NOT NULL DEFAULT 0,
    user_id BIGINT UNIQUE NOT NULL,

    CONSTRAINT fk_user_account FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE transactions(
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account_id BIGINT NOT NULL,
    type varchar(20) NOT NULL,
    amount NUMERIC(15,2) NOT NULL,
    date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_account_transaction FOREIGN KEY (account_id) REFERENCES accounts(id)
);

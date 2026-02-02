CREATE TABLE users(
   id serial PRIMARY KEY,
   name varchar(255),
   email varchar (255) UNIQUE,
   password varchar(255)
);

CREATE TABLE accounts(
    id serial PRIMARY KEY,
    balance NUMERIC(15,2) NOT NULL DEFAULT 0,
    user_id BIGINT UNIQUE NOT NULL,

    CONSTRAINT fk_user_account FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE transactions(
    id serial PRIMARY KEY,
    account_id BIGINT NOT NULL,
    type varchar(20) NOT NULL,
    amount NUMERIC(15,2) NOT NULL,
    time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_account_transaction FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE DATABASE IF NOT EXISTS rlacctdb;
USE rlacctdb;

DROP TABLE IF EXISTS address_table;
DROP TABLE IF EXISTS account_balance_table;
DROP TABLE IF EXISTS account_table;

--
-- Accounts table.
--
CREATE TABLE account_table
(
    id           BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    uid          VARCHAR(40)  NOT NULL UNIQUE, -- Only one account per user ID
    account_id   VARCHAR(40)  NOT NULL UNIQUE,
    name         VARCHAR(128) NOT NULL,
    email        VARCHAR(128) NOT NULL UNIQUE,
    phone_number VARCHAR(20)  NOT NULL UNIQUE,
    currency_cd  VARCHAR(3)   NOT NULL DEFAULT 'USD',
    active       BOOLEAN               DEFAULT TRUE
);

--
-- Account Balance table.
--
CREATE TABLE account_balance_table
(
    id              BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    account_id      VARCHAR(40) NOT NULL UNIQUE,
    account_balance FLOAT       NOT NULL DEFAULT 0.0,
    CONSTRAINT fk_acct_balance_table_acct_id_acct_table
        FOREIGN KEY (account_id)
            REFERENCES account_table (account_id)
);

--
-- Address table.
--
CREATE TABLE address_table
(
    id             BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    account_id     VARCHAR(40) NOT NULL,
    address_type   VARCHAR(40) NOT NULL DEFAULT 'BILLING',
    address_line_1 VARCHAR(40) NOT NULL,
    address_line_2 VARCHAR(40),
    city           VARCHAR(64) NOT NULL,
    state          VARCHAR(64) NOT NULL,
    postal_code    VARCHAR(20) NOT NULL,
    country        VARCHAR(64),
    CONSTRAINT fk_address_table_acct_id_acct_table
        FOREIGN KEY (account_id)
            REFERENCES account_table (account_id)
);


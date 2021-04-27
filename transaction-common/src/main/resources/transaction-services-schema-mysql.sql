CREATE DATABASE IF NOT EXISTS rltxndb;
USE rltxndb;

DROP TABLE IF EXISTS money_reload_txn_table;
DROP TABLE IF EXISTS airtime_send_txn_table;
DROP TABLE IF EXISTS transaction_table;
--
-- Transaction table.
--
CREATE TABLE transaction_table
(
    id         BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    uid        VARCHAR(40)                             NOT NULL,
    txn_id     VARCHAR(40)                             NOT NULL UNIQUE,
    txn_type   VARCHAR(16)                             NOT NULL,
    txn_status VARCHAR(16)                             NOT NULL
);

CREATE TABLE money_reload_txn_table
(
    id     BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    txn_id VARCHAR(40)                             NOT NULL UNIQUE,
    amount FLOAT                                   NOT NULL,
    CONSTRAINT fk_money_reload_txn_table_txn_id
        FOREIGN KEY (txn_id)
            REFERENCES transaction_table (txn_id)
);

CREATE TABLE airtime_send_txn_table
(
    id           BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    txn_id       VARCHAR(40)                             NOT NULL UNIQUE,
    amount       FLOAT                                   NOT NULL,
    phone_number VARCHAR(16)                             NOT NULL,
    CONSTRAINT fk_airtime_send_txn_table_txn_id
        FOREIGN KEY (txn_id)
            REFERENCES transaction_table (txn_id)
);
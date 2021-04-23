--
-- Transaction table.
--
CREATE TABLE transaction_table
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    txn_id VARCHAR(40)                             NOT NULL UNIQUE
);
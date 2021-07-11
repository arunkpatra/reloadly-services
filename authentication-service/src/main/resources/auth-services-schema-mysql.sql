CREATE DATABASE IF NOT EXISTS rlauthdb;
USE rlauthdb;

DROP TABLE IF EXISTS api_key_table;
DROP TABLE IF EXISTS username_password_table;
DROP TABLE IF EXISTS authority_table;
DROP TABLE IF EXISTS role_table;
DROP TABLE IF EXISTS user_table;

--
-- User table. Contains unique UIDs.
--
CREATE TABLE user_table
(
    id     BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    uid    VARCHAR(40) NOT NULL UNIQUE,
    active BOOLEAN DEFAULT true
);

--
-- Valid roles that may be assigned to a user.
--
create table role_table
(
    id        BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    role_name VARCHAR(32) NOT NULL UNIQUE
);

--
-- Authorities assigned to a user. Authorities are equivalent to roles in the current context.
--
CREATE TABLE authority_table
(
    id        BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    uid       VARCHAR(40) NOT NULL,
    authority VARCHAR(32) NOT NULL,
    CONSTRAINT unique_user_id_authority UNIQUE (uid, authority),
    CONSTRAINT fk_igc_authority_user_name_igc_user_user_name
        FOREIGN KEY (uid)
            REFERENCES user_table (uid),
    CONSTRAINT fk_igc_authority_authority_igc_role_role_name
        FOREIGN KEY (authority)
            REFERENCES role_table (role_name)
);

--
-- Table supporting a username/password based authentication. Passwords are encrypted.
--
CREATE TABLE username_password_table
(
    id        BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_name VARCHAR(64)  NOT NULL UNIQUE,
    password  VARCHAR(128) NOT NULL,
    uid       VARCHAR(40)  NOT NULL,
    CONSTRAINT fk_username_password_table_user_id_user_table
        FOREIGN KEY (uid)
            REFERENCES user_table (uid)
);

--
-- Client ID table
--
create TABLE client_id_table
(
    id        BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    uid       VARCHAR(40) NOT NULL UNIQUE,
    client_id VARCHAR(40) NOT NULL UNIQUE,
    CONSTRAINT fk_client_id_table_uid_user_table
        FOREIGN KEY (uid)
            REFERENCES user_table (uid)
);

--
-- API Key table
--
create TABLE api_key_table
(
    id           BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    client_id    VARCHAR(40)  NOT NULL,
    api_key      VARCHAR(128) NOT NULL UNIQUE,
    api_key_desc VARCHAR(128) NOT NULL,
    active       BOOLEAN DEFAULT true,
    CONSTRAINT fk_api_key_table_client_id_client_id_table
        FOREIGN KEY (client_id)
            REFERENCES client_id_table (client_id)
);
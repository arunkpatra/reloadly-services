--
-- Data goes into the Auth Database
--
USE rlauthdb;

--
-- These roles must exist in the system, for application to work.
--
INSERT INTO role_table(role_name)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');

--
-- This is for a Reloadly Service Account, which the system needs.
--
INSERT INTO user_table(uid, active)
VALUES ('c1fe6f0d-420e-4161-a134-9c2342e36c95', true);

--
-- Service Account authorities.
--
INSERT INTO authority_table(uid, authority)
VALUES ('c1fe6f0d-420e-4161-a134-9c2342e36c95', 'ROLE_USER'),
       ('c1fe6f0d-420e-4161-a134-9c2342e36c95', 'ROLE_ADMIN');

--
-- Service Account credentials.
--
INSERT INTO username_password_table(user_name, password, uid)
VALUES ('reloadly_svc_acct', '$2a$10$OwuE74o7m2qCj7yTVgleGOhwuqrvbJZT2qwYCGtyUi6ITSjSqHEZy',
        'c1fe6f0d-420e-4161-a134-9c2342e36c95');

--
-- Client ID for service account
--
INSERT INTO client_id_table(uid, client_id)
VALUES ('c1fe6f0d-420e-4161-a134-9c2342e36c95', 'bafa4494-40dd-4b0c-b42e-623399e70533');

--
-- API Key issued to 'reloadly_svc_acct' service account. Un-encrypted API key is 'test-api-key'
--
INSERT INTO api_key_table(client_id, api_key, api_key_desc, active)
VALUES ('bafa4494-40dd-4b0c-b42e-623399e70533', '$2a$10$C3nQIBaKAixaJHQtTUj/8eoT487qd7vwq4ZRHFQYVla6Z.fQOx0YG', 'Test API key', true);

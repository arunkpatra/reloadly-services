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
-- API Key issued to 'reloadly_svc_acct' service account.
--
INSERT INTO api_key_table(uid, api_key, active)
VALUES ('c1fe6f0d-420e-4161-a134-9c2342e36c95', 'd3fe6f0d-120e-4161-a134-8c2342e36ca6', true);
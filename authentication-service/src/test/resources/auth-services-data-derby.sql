INSERT INTO user_table(uid, active)
VALUES ('c1fe6f0d-420e-4161-a134-9c2342e36c95', true),
       ('504a2de5-b525-4a74-b2f6-03c7bda719b9', true),
       ('d273e6f9-bbf5-4126-bd7b-643ffc601291', true),
       ('e273e6f9-bbf5-4126-bd7b-643ffc601800', true),
       ('e273e6f9-bbf5-4126-bd7b-643ffc601801', true),
       ('e273e6f9-bbf5-4126-bd7b-643ffc601802', true),
       ('f433e6f9-ccdd-e2d4-1278-643ffc601457', false),
       ('2244d807-d77e-4e42-b81c-22f8b0f95632', true);

INSERT INTO role_table(role_name)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');

INSERT INTO authority_table(uid, authority)
VALUES ('c1fe6f0d-420e-4161-a134-9c2342e36c95', 'ROLE_USER'),
       ('c1fe6f0d-420e-4161-a134-9c2342e36c95', 'ROLE_ADMIN'),
       ('d273e6f9-bbf5-4126-bd7b-643ffc601291', 'ROLE_USER'),
       ('d273e6f9-bbf5-4126-bd7b-643ffc601291', 'ROLE_ADMIN'),
       ('e273e6f9-bbf5-4126-bd7b-643ffc601800', 'ROLE_USER'),
       ('e273e6f9-bbf5-4126-bd7b-643ffc601801', 'ROLE_USER'),
       ('e273e6f9-bbf5-4126-bd7b-643ffc601802', 'ROLE_USER');

-- Encrypted password value is `password`
INSERT INTO username_password_table(user_name, password, uid)
VALUES ('reloadly', '$2a$10$OwuE74o7m2qCj7yTVgleGOhwuqrvbJZT2qwYCGtyUi6ITSjSqHEZy',
        'c1fe6f0d-420e-4161-a134-9c2342e36c95'),
       ('admin', '$2a$10$OwuE74o7m2qCj7yTVgleGOhwuqrvbJZT2qwYCGtyUi6ITSjSqHEZy',
        'd273e6f9-bbf5-4126-bd7b-643ffc601291'),
       ('inactiveuser', '$2a$10$OwuE74o7m2qCj7yTVgleGOhwuqrvbJZT2qwYCGtyUi6ITSjSqHEZy',
        'f433e6f9-ccdd-e2d4-1278-643ffc601457'),
       ('banneduser', '$2a$10$OwuE74o7m2qCj7yTVgleGOhwuqrvbJZT2qwYCGtyUi6ITSjSqHEZy',
        '2244d807-d77e-4e42-b81c-22f8b0f95632');


INSERT INTO api_key_table(uid, api_key, active)
VALUES ('c1fe6f0d-420e-4161-a134-9c2342e36c95', 'd3fe6f0d-120e-4161-a134-8c2342e36ca6', true),
       ('c1fe6f0d-420e-4161-a134-9c2342e36c95', 'b2fe6f0a-120e-1234-a134-8c2342e36c72', true);
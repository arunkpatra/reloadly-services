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

INSERT INTO client_id_table(uid, client_id)
VALUES ('c1fe6f0d-420e-4161-a134-9c2342e36c95', 'bafa4494-40dd-4b0c-b42e-623399e70533');

INSERT INTO api_key_table(client_id, api_key, api_key_desc, active)
VALUES ('bafa4494-40dd-4b0c-b42e-623399e70533', '$2a$10$C3nQIBaKAixaJHQtTUj/8eoT487qd7vwq4ZRHFQYVla6Z.fQOx0YG', 'Test API key', true),
       ('bafa4494-40dd-4b0c-b42e-623399e70533', '$2a$10$Ha8J/MTO5j91FfzfxiRh9uRNgXR3N0xwwKsWd9B6XnoY/hYQNOI.C', 'Test API key', true),
       ('bafa4494-40dd-4b0c-b42e-623399e70533', '$2a$10$DeMMslFZX3ZH6rrOW12c0um/Z5as4mEkEgXInlt9G8y/QRy6nNd2O', 'Test API key', false);
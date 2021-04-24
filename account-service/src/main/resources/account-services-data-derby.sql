INSERT INTO account_balance_table(account_balance_id, account_balance)
VALUES ('d1fe6f0d-420e-4161-a134-9c2342e36c88', 200.5);

INSERT INTO account_table(uid, account_id, account_balance_id,  name, email, phone_number, currency_cd, active)
VALUES ('c1fe6f0d-420e-4161-a134-9c2342e36c95', 'f1fe6f0d-420e-4161-a134-9c2342e36c99', 'd1fe6f0d-420e-4161-a134-9c2342e36c88', 'John Doe', 'email@email.com', '+19999999999', 'USD', true);

INSERT INTO address_table(account_id, address_line_1, address_line_2, city, state, postal_code, country)
VALUES ('f1fe6f0d-420e-4161-a134-9c2342e36c99', '1946 Sunset Blvd','', 'Houston', 'Texas', '12345', 'USA');
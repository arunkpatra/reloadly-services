INSERT INTO transaction_table(uid, txn_id, txn_type, txn_status)
VALUES ('c1fe6f0d-420e-4161-a134-9c2342e36c95', 'e7fe6f0d-420e-5161-a134-9c2342e36c32', 'ADD_MONEY', 'ACCEPTED'),
       ('c1fe6f0d-420e-4161-a134-9c2342e36c95', 'b7fe6f0d-420e-5121-a134-9c2342e36c64', 'SEND_AIRTIME', 'ACCEPTED');

INSERT INTO money_reload_txn_table(txn_id, amount)
VALUES ('e7fe6f0d-420e-5161-a134-9c2342e36c32', 150.0);

INSERT INTO airtime_send_txn_table(txn_id, amount, phone_number)
VALUES ('b7fe6f0d-420e-5121-a134-9c2342e36c64', 10.0, '+919999999999');

INSERT INTO account (accountnumber, alias, balance) VALUES ('0', 'First account', 100.0);
INSERT INTO account (accountnumber, alias, balance) VALUES ('1', 'Second account', 0.0);

INSERT INTO transfer (sourceaccount, targetaccount, amount, status) VALUES (1, 2, 10, 'PENDING');
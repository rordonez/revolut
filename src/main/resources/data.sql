INSERT INTO account (accountnumber, alias) VALUES ('0', 'First account');
INSERT INTO account (accountnumber, alias) VALUES ('1', 'Second account');

INSERT INTO transfer (sourceaccount, targetaccount, amount, status) VALUES (0, 1, 10, 'PENDING');
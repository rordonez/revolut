INSERT INTO users (name) VALUES ('Rafa');
INSERT INTO users (name) VALUES ('Peter');

INSERT INTO account (owner, accountnumber, alias, balance) VALUES (1, '0', 'First account', 100.0);
INSERT INTO account (owner, accountnumber, alias, balance) VALUES (2, '1', 'Second account', 0.0);

INSERT INTO transfer (sourceaccount, targetaccount, amount, status) VALUES (1, 2, 10, 'PENDING');


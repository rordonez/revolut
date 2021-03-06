DROP TABLE IF EXISTS users;

create TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT,
  name VARCHAR2(20),
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS account;

CREATE TABLE if not EXISTS account (
  id BIGINT AUTO_INCREMENT,
  owner BIGINT NOT NULL,
  alias VARCHAR2(255) NOT NULL,
  accountnumber VARCHAR2(50) NOT NULL,
  balance DECIMAL(20,2) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (owner) REFERENCES users,
  UNIQUE (accountnumber)
);

DROP TABLE IF EXISTS transfer;

create TABLE IF NOT EXISTS transfer (
  id BIGINT AUTO_INCREMENT,
  sourceaccount BIGINT NOT NULL,
  targetaccount BIGINT NOT NULL,
  amount BIGINT NOT NULL,
  status VARCHAR2(10),
  PRIMARY KEY (id)
);

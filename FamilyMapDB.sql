DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS persons;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS auth_tokens;


CREATE TABLE users
(
	username VARCHAR(42) NOT NULL UNIQUE,
	password VARCHAR(42) NOT NULL,
	email VARCHAR(255) NOT NULL,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	gender VARCHAR(1) NOT NULL,
	person_id VARCHAR(255) NOT NULL
);

CREATE TABLE persons
(
	person_id VARCHAR(255) NOT NULL UNIQUE,
	a_UN VARCHAR(42) NOT NULL, --associated user name
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	gender VARCHAR(1) NOT NULL,
	father_id VARCHAR(255),
	mother_id VARCHAR(255),
	spouse_id VARCHAR(255)
);

CREATE TABLE events
(
	event_id VARCHAR(255) UNIQUE NOT NULL,
	a_UN VARCHAR(42) NOT NULL, --associated user name
	person_ID VARCHAR(255) NOT NULL,
	lat INTEGER NOT NULL,
	lon INTEGER NOT NULL,
	country VARCHAR(255) NOT NULL,
	city VARCHAR(255) NOT NULL,
	event_type VARCHAR(255) NOT NULL,
	year INTEGER NOT NULL
);

CREATE TABLE auth_tokens
(
	auth_token VARCHAR(255) NOT NULL UNIQUE,
	a_UN VARCHAR(42) NOT NULL, --associated user name
	password VARCHAR(42) NOT NULL
);
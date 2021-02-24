CREATE TABLE IF NOT EXISTS messages(
	id SERIAL PRIMARY KEY,
	valueset CHARACTER VARYING(300) UNIQUE,
	timestamp timestamp default current_timestamp
	);
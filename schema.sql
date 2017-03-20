CREATE TABLE COMPANY (
	id serial PRIMARY KEY,
    name VARCHAR (255) NOT NULL
);

CREATE TABLE COMPANYADMIN (
    id serial PRIMARY KEY,
    company_id serial REFERENCES COMPANY(id),
	username VARCHAR (255) NOT NULL
);

CREATE TABLE SIPCLIENT (
    id serial PRIMARY KEY,
    company_id serial REFERENCES COMPANY(id),
	phonenumber VARCHAR (255) NOT NULL,
    label VARCHAR (255) NOT NULL
);

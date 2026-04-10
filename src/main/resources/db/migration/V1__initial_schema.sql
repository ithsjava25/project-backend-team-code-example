CREATE TABLE company(
    id SMALLINT PRIMARY KEY AUTO_INCREMENT,
    companyName VARCHAR(100) NOT NULL,
    email VARCHAR(200),

    #Address
    city VARCHAR(100) NOT NULL,
    street VARCHAR(100) NOT NULL,
    streetNumber SMALLINT,
    zipcode SMALLINT NOT NULL
);

CREATE TABLE project(
    id SMALLINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    releaseDate DATE,
    producerId SMALLINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    imageURL VARCHAR(200)
);

CREATE TABLE task(
    id SMALLINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(300),
    status VARCHAR(50) NOT NULL,
    deadline TIMESTAMP,
    projectId SMALLINT NOT NULL,
    userId SMALLINT NOT NULL
);

CREATE TABLE user(
    id SMALLINT PRIMARY KEY AUTO_INCREMENT,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    email VARCHAR(200) NOT NULL,
    role VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE comment(
    id SMALLINT PRIMARY KEY AUTO_INCREMENT,
    content VARCHAR(300),
    private Long writerId;
    createdAt TIME


);

CREATE TABLE logger(
   id SMALLINT PRIMARY KEY AUTO_INCREMENT

);
CREATE TABLE Users (
    id int auto_increment,
    username varchar(255),
    password varchar(255)
);

CREATE TABLE Login (
    id int auto_increment,
    userId int,
    loginDate datetime,
    success bit,
    uri varchar(255),
    ip varchar(15)
);

CREATE TABLE Logout (
    id int auto_increment,
    userId int,
    logoutDate datetime,
    uri varchar(255),
    ip varchar(15)
);

CREATE TABLE Request (
    id int auto_increment,
    userId int,
    requestDate datetime,
    uri varchar(255),
    ip varchar(15),
    method varchar(6)
);
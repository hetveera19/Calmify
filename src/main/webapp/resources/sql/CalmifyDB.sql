#
# SQL Script to Create and Populate the Articles Table
# in the Calmify Database (CalmifyDB)
# Created by Anubhav Nanda
# ---------------------------------------------------

DROP TABLE IF EXISTS User, UserPhoto, Blog, Comment;

/* The User table contains attributes of interest of a User. */
CREATE TABLE User
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR(32) NOT NULL,
    password VARCHAR(256) NOT NULL,          /* To store Salted and Hashed Password Parts */
    first_name VARCHAR(32) NOT NULL,
    middle_name VARCHAR(32),
    last_name VARCHAR(32) NOT NULL,
    address1 VARCHAR(128) NOT NULL,
    address2 VARCHAR(128),
    city VARCHAR(64) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zipcode VARCHAR(10) NOT NULL,            /* e.g., 24060-1804 */
    security_question_number INT NOT NULL,   /* Refers to the number of the selected security question */
    security_answer VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL,
    PRIMARY KEY (id)
);

/* The UserPhoto table contains attributes of interest of a user's photo. */
CREATE TABLE UserPhoto
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    extension ENUM('jpeg', 'jpg', 'png', 'gif') NOT NULL,
    user_id INT UNSIGNED,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);


CREATE TABLE Blog
(
    id              int unsigned NOT NULL auto_increment,
    publicationDate date NOT NULL,
    title           varchar(255) NOT NULL,
    summary         text NOT NULL,
    content         mediumtext NOT NULL,
    user_id         INT UNSIGNED,
    extension ENUM('jpeg', 'jpg', 'png', 'gif'),
    PRIMARY KEY     (id),
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Comment
(
    id              int unsigned NOT NULL auto_increment,
    blog_id         int unsigned NOT NULL,
    publicationDate date NOT NULL,
    content         text NOT NULL,
    user_id         INT UNSIGNED,
    rating          FLOAT UNSIGNED,
    PRIMARY KEY     (id),
    FOREIGN KEY (blog_id) REFERENCES Blog(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

create table ShopItems
(
    id              int unsigned NOT NULL auto_increment,
    name            varchar(256)     not null,
    description     varchar(2048)    not null,
    category        varchar(64)      not null,
    image_url       varchar(256)     null,
    price           double default 0 not null,
    PRIMARY KEY     (id)
);

create table UserItems
(
    Id          int unsigned auto_increment primary key,
    Name        varchar(256)     not null,
    description varchar(2048)    not null,
    category    varchar(64)      not null,
    image_url   varchar(256)     null,
    price       double default 0 not null,
    user_id     int unsigned     null,
    PRIMARY KEY     (id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
);
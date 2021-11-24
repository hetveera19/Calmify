# 
# SQL Script to Create and Populate the Articles Table
# in the Calmify Database (CalmifyDB)
# Created by Anubhav Nanda
# ---------------------------------------------------

DROP TABLE IF EXISTS Articles;

CREATE TABLE Articles
(
	id              smallint unsigned NOT NULL auto_increment,
	publicationDate date NOT NULL,                              
	title           varchar(255) NOT NULL,                      
	summary         text NOT NULL,
	content         mediumtext NOT NULL,
	PRIMARY KEY     (id)
);


CREATE TABLE Comment
(
    id              smallint unsigned NOT NULL auto_increment,
    article__id     smallint NOT NULL,
    publicationDate date NOT NULL,
    content         text NOT NULL,
    PRIMARY KEY     (id)
);